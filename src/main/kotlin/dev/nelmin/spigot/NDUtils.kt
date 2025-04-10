package dev.nelmin.spigot

import dev.nelmin.logger.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bukkit.NamespacedKey
import org.bukkit.plugin.Plugin

/**
 * Represents the result of checking for an update.
 *
 * This data class is used to indicate whether an update is needed, the type of update (if applicable),
 * and any errors encountered during the update check process.
 *
 * @property needsUpdate A boolean indicating whether an update is necessary.
 * @property updateType An optional string representing the type of update, such as "Stable", "Snapshot", or "Alpha".
 * @property error An optional error message in case the update check encountered issues.
 */
data class UpdateCheckResult(
    val needsUpdate: Boolean,
    val updateType: String? = null,
    val error: String? = null
)

/**
 * Represents a semantic version number, which typically follows the format `MAJOR.MINOR.PATCH[-PRERELEASE]`.
 * This class provides mechanisms for parsing version strings and extracting their components.
 *
 * @property version A string representing the semantic version (e.g., "1.0.0", "2.1.4-alpha").
 * @property major The major version component, indicating breaking changes.
 * @property minor The minor version component, indicating backward-compatible functionality additions.
 * @property patch The patch version component, indicating backward-compatible bug fixes.
 * @property preRelease The optional pre-release component, indicating an unstable version (e.g., "alpha", "SNAPSHOT").
 *
 * @throws IllegalArgumentException If the provided version string is not in a valid semantic version format.
 */
data class SemanticVersion(val version: String) {
    val major: Int
    val minor: Int
    val patch: Int
    val preRelease: String?

    init {
        val (base, pre) = version.split("-", limit = 2).let {
            it.first() to it.getOrNull(1)
        }

        val numbers = base.split(".")
            .mapNotNull { it.toIntOrNull() }
            .takeIf { it.size in 1..3 }
            ?: throw IllegalArgumentException("Invalid version format: $version")

        major = numbers.getOrNull(0) ?: 0
        minor = numbers.getOrNull(1) ?: 0
        patch = numbers.getOrNull(2) ?: 0
        preRelease = pre
    }

    fun isPreRelease(): Boolean = preRelease?.let {
        it.contains("SNAPSHOT", true) || it.contains("ALPHA", true)
    } ?: false
}

/**
 * Utility object for handling various operations related to namespaces, HTTP communication, and version updates.
 */
object NDUtils {
    /**
     * Represents an HTTP client instance configured with specific plugins and timeout settings
     * for use in the NDCore plugin.
     *
     * This HttpClient is built using the CIO engine for asynchronous and efficient HTTP operations.
     * The client includes the following configurations:
     * - `ContentNegotiation` plugin with Kotlinx Serialization JSON support, allowing seamless
     *   serialization and deserialization of JSON payloads.
     * - `HttpTimeout` plugin with a request timeout of 10,000 milliseconds to ensure timely
     *   responses or failures for HTTP requests.
     *
     * The HttpClient is primarily used for interacting with external APIs or endpoints, such as
     * retrieving release information in the plugin's update checking functionality.
     *
     * The configuration ensures compliance with appropriate headers and serialization requirements
     * for communicating with REST APIs like GitHub's API.
     */
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
        install(HttpTimeout) { requestTimeoutMillis = 10000 }
    }

    /**
     * Constructs a namespaced key for a given key name, associated with a specified plugin.
     *
     * @param keyName The name of the key to namespace. This serves as the unique identifier for the key.
     * @param plugin The plugin instance associated with the namespaced key. Defaults to the singleton instance of NDCore.
     * @return A NamespacedKey object representing the namespaced key created for the specified key name and plugin.
     */
    fun getNamespacedKey(keyName: String, plugin: Plugin = NDCore.instance()) =
        NamespacedKey(plugin, keyName)

    /**
     * Checks for updates by comparing the current version of the application with the latest version
     * available on the provided GitHub repository. The result of the update check is returned via a callback.
     *
     * @param name The name of the GitHub repository to check for updates. Default is "NDCore".
     * @param organizationOrUser The GitHub organization or user that owns the repository. Default is "NelminDev".
     * @param callback A function to handle the result of the update check. It receives an instance of
     * `UpdateCheckResult` indicating whether an update is needed, the type of update (if any), or any errors
     * encountered during the check.
     */
    fun checkForPluginUpdates(
        name: String = "NDCore",
        organizationOrUser: String = "NelminDev",
        callback: (UpdateCheckResult) -> Unit
    ) = NDCore.coroutineScope.launch(Dispatchers.IO) {
        runCatching {
            val currentVersion = NDCore.instance().description.version.let {
                SemanticVersion(it)
            }

            val response: JsonObject = httpClient
                .get("https://api.github.com/repos/$organizationOrUser/$name/releases/latest") {
                    headers { append(HttpHeaders.Accept, "application/vnd.github.v3+json") }
                }
                .body()

            val latestVersionStr = response["tag_name"]?.jsonPrimitive?.content
                ?: return@launch callback(
                    UpdateCheckResult(
                        needsUpdate = false,
                        error = "No tag_name found in GitHub response"
                    )
                ).also {
                    Logger.warn("No tag_name found in GitHub response")
                }

            val latestVersion = SemanticVersion(latestVersionStr.removePrefix("v"))
            callback(compareVersions(currentVersion, latestVersion))
        }.onFailure { e ->
            val errorMsg = "Update check failed: ${e.message}"
            Logger.error(errorMsg)
            Logger.stacktrace(e)
            callback(UpdateCheckResult(needsUpdate = false, error = errorMsg))
        }
    }

    /**
     * Compares the current semantic version of software with the latest available version to determine
     * if an update is needed and the type of update (if applicable). It takes into account version components
     * such as major, minor, patch, and pre-release status.
     *
     * The comparison is performed as follows:
     * 1. A higher major version indicates a breaking change and requires an update.
     * 2. A higher minor version indicates backward-compatible functionality additions and requires an update.
     * 3. A higher patch version indicates backward-compatible bug fixes and requires an update.
     * 4. If the versions are equal, pre-release status is checked to prioritize stable versions over snapshots or alphas.
     *
     * Exceptions during the comparison are handled, and an appropriate error message is returned if something goes wrong.
     *
     * @param current The current semantic version in use.
     * @param latest The latest semantic version available.
     * @return An instance of UpdateCheckResult indicating whether an update is needed, the type of update (if applicable),
     *         and any errors encountered during the comparison process.
     */
    private fun compareVersions(
        current: SemanticVersion,
        latest: SemanticVersion
    ): UpdateCheckResult {
        return try {
            when {
                latest.major > current.major -> UpdateCheckResult(true)
                latest.major < current.major -> UpdateCheckResult(false)
                latest.minor > current.minor -> UpdateCheckResult(true)
                latest.minor < current.minor -> UpdateCheckResult(false)
                latest.patch > current.patch -> UpdateCheckResult(true)
                latest.patch < current.patch -> UpdateCheckResult(false)
                // Version numbers are equal, check pre-release status
                current.isPreRelease() && !latest.isPreRelease() ->
                    UpdateCheckResult(true, "Stable")

                !current.isPreRelease() && latest.preRelease?.contains("SNAPSHOT", true) == true ->
                    UpdateCheckResult(true, "Snapshot")

                !current.isPreRelease() && latest.preRelease?.contains("ALPHA", true) == true ->
                    UpdateCheckResult(true, "Alpha")

                else -> UpdateCheckResult(false)
            }
        } catch (e: Exception) {
            val errorMsg = "Version comparison failed: ${e.message}"
            Logger.error(errorMsg)
            UpdateCheckResult(needsUpdate = false, error = errorMsg)
        }
    }
}