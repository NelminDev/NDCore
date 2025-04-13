package dev.nelmin.minecraft

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
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

/**
 * Represents the result of checking for plugin updates. This class encapsulates
 * the information about whether an update is needed, the type of update, any errors
 * encountered during the update check, and the current version of the plugin.
 *
 * @property needsUpdate Indicates if an update is required for the plugin.
 * @property updateType Specifies the type or severity of the update (e.g., major, minor, patch).
 *                      Null if no update is necessary or the type could not be determined.
 * @property error Describes any error encountered while checking for updates.
 *                 Null if no error occurred during the process.
 * @property currentVersion The current version of the plugin as retrieved from the plugin's metadata.
 */
data class UpdateCheckResult(
    val needsUpdate: Boolean,
    val updateType: String? = null,
    val error: String? = null,
    val currentVersion: String = JavaPlugin.getPlugin(NDCore::class.java).pluginMeta.version
)

/**
 * Represents a semantic version number adhering to the format "MAJOR.MINOR.PATCH[-PRERELEASE]".
 * The version string is parsed into its individual components: major, minor, patch, and an optional pre-release identifier.
 *
 * Semantic versioning is a standard for version numbers that conveys meaning about the underlying changes.
 */
data class SemanticVersion(val version: String) {
    /**
     * Represents the major version number of a semantic version.
     *
     * This value is typically the first number in a version string following the semantic versioning format (e.g., "1" in "1.0.0").
     * It signifies backward-incompatible changes or significant updates in the software.
     */
    val major: Int

    /**
     * Represents the minor version component of the semantic version.
     *
     * This value corresponds to the second segment of the version number in the format `MAJOR.MINOR.PATCH`.
     * It indicates backward-compatible functionality additions or enhancements within the version.
     *
     * For example, in version `1.2.3`, the minor version is `2`.
     */
    val minor: Int

    /**
     * Represents the patch version number in a semantic versioning scheme.
     *
     * The patch version is intended to increment for backward-compatible bug fixes.
     * It is extracted from the version string provided during the initialization
     * of the `SemanticVersion` class.
     */
    val patch: Int

    /**
     * Represents the optional pre-release information of a semantic version.
     *
     * This value is derived from a version string when a pre-release segment exists.
     * The pre-release segment is generally denoted following a hyphen, e.g., "1.0.0-alpha".
     * It may include identifiers such as "SNAPSHOT", "ALPHA", or other custom labels,
     * and can be used to determine whether the version is a pre-release.
     *
     * If no pre-release segment is specified in the version string, this value will be `null`.
     */
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

    /**
     * Determines whether the current Semantic Version instance represents a pre-release version.
     *
     * A version is considered a pre-release if the `preRelease` component of the version contains
     * either "SNAPSHOT" or "ALPHA", case-insensitively.
     *
     * @return true if the version is a pre-release, false otherwise
     */
    fun isPreRelease(): Boolean = preRelease?.let {
        it.contains("SNAPSHOT", true) || it.contains("ALPHA", true)
    } == true
}

/**
 * Utility object providing a set of functions commonly used in plugin development.
 * Includes operations for managing items in player inventories, namespace key generation,
 * plugin update checks, and more.
 */
object NDUtils {
    /**
     * Represents an HTTP client configured with the CIO engine.
     *
     * This `httpClient` is set up with the following features:
     * - **Content Negotiation**: Automatically handles JSON serialization and deserialization.
     * - **Timeout configuration**: The request timeout is set to 10 seconds (10,000 milliseconds).
     *
     * Used for making HTTP requests, such as fetching data from external APIs like GitHub.
     * It is initialized with built-in plugins for efficient network communication and data handling.
     */
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
        install(HttpTimeout) { requestTimeoutMillis = 10000 }
    }

    /**
     * Generates a `NamespacedKey` for the specified key name using the provided plugin or the default NDCore plugin.
     *
     * @param keyName The name of the key to be used for generating the `NamespacedKey`.
     * @param plugin The plugin instance used for creating the `NamespacedKey`. Defaults to the NDCore plugin instance.
     * @return A `NamespacedKey` object that combines the plugin namespace with the provided key name.
     */
    fun getNamespacedKey(keyName: String, plugin: Plugin = JavaPlugin.getPlugin(NDCore::class.java)) =
        NamespacedKey(plugin, keyName)

    /**
     * Checks for updates to a specified plugin using the GitHub API and notifies the result via a callback.
     *
     * @param name The name of the plugin repository. Defaults to "NDCore".
     * @param organizationOrUser The organization or user that owns the plugin repository. Defaults to "NelminDev".
     * @param currentVersion The current version of the plugin in use.
     * @param callback A callback function that receives the result of the update check as an instance of [UpdateCheckResult].
     */
    fun checkForPluginUpdates(
        name: String = "NDCore",
        organizationOrUser: String = "NelminDev",
        currentVersion: String,
        callback: (UpdateCheckResult) -> Unit
    ) = NDCore.coroutineScope.launch(Dispatchers.IO) {
        runCatching {
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
            callback(compareVersions(SemanticVersion(currentVersion), latestVersion))
        }.onFailure { e ->
            val errorMsg = "Update check failed: ${e.message}"
            Logger.error(errorMsg)
            Logger.stacktrace(e)
            callback(UpdateCheckResult(needsUpdate = false, error = errorMsg))
        }
    }

    /**
     * Compares two semantic versions to determine if an update is needed.
     *
     * @param current the current version of the application.
     * @param latest the latest available version to compare against.
     * @return an instance of [UpdateCheckResult], indicating whether an update is needed,
     *         the type of update if applicable, or an error message if the comparison fails.
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
            Logger.queueError(errorMsg)
            UpdateCheckResult(needsUpdate = false, error = errorMsg)
        }
    }

    /**
     * Removes the specified item from the inventory of all online players.
     *
     * @param material the item material to be removed from all player inventories
     */
    fun removeItemFromAllPlayers(material: Material) {
        for (player in Bukkit.getOnlinePlayers()) {
            player.inventory.remove(material)
        }
    }

    /**
     * Removes a specified material from the inventory of all players in the provided list.
     *
     * @param playerList The list of players whose inventories will be updated.
     * @param material The material to be removed from the players' inventories.
     */
    fun removeItemFromPlayers(playerList: List<Player>, material: Material) {
        for (player in playerList) {
            player.inventory.remove(material)
        }
    }
}