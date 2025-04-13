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
 * Represents the result of a plugin update check operation.
 *
 * @property needsUpdate Indicates whether an update is required for the plugin.
 * @property updateType Specifies the type of update available, such as "Stable", "Snapshot", or "Alpha".
 *                      Can be null if no update is needed or if the type is unspecified.
 * @property error Provides details about any error encountered during the update check.
 *                 Can be null if the operation completes successfully.
 */
data class UpdateCheckResult(
    val currentVersion: String,
    val needsUpdate: Boolean,
    val updateType: String? = null,
    val error: String? = null
)

/**
 * Represents a semantic versioning implementation for parsing and handling version strings.
 * The semantic versioning format is typically `MAJOR.MINOR.PATCH` with an optional pre-release identifier.
 *
 **/
data class SemanticVersion(val version: String) {
    /**
     * Represents the major version number in a semantic versioning scheme.
     *
     * This value corresponds to the first segment of a version string in the format `x.y.z`,
     * where `x` is the major version. A higher major version generally indicates significant
     * changes, breaking backward compatibility with previous versions.
     *
     * Example:
     * For the version string `2.5.1`, the `major` version is `2`.
     */
    val major: Int

    /**
     * Represents the minor version component of a semantic version.
     * It is a non-negative integer that indicates backward-compatible new features
     * or enhancements introduced in the software since the last major version increment.
     *
     * For example, in a version string "1.2.3", the value of `minor` would be 2.
     * It is extracted from the version string during the initialization of the `SemanticVersion` class.
     */
    val minor: Int

    /**
     * The patch version number of the semantic version.
     * This variable represents the third segment in the semantic version format (major.minor.patch).
     *
     * For instance, in a version "1.2.3", `patch` would be `3`.
     * It defaults to `0` if the patch segment is absent in the version string.
     */
    val patch: Int

    /**
     * Represents the pre-release metadata associated with a semantic version.
     * This property holds an optional string that indicates whether the version
     * is a pre-release and specifies the type or stage*/
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
     * Determines whether the version is a pre-release based on the presence of specific keywords
     * in the `preRelease` identifier. A version is considered a pre-release if the `preRelease`
     * identifier contains "SNAPSHOT" or "ALPHA" (case-insensitive).
     *
     * @return `true` if the version is a pre-release, otherwise `false`.
     */
    fun isPreRelease(): Boolean = preRelease?.let {
        it.contains("SNAPSHOT", true) || it.contains("ALPHA", true)
    } == true
}

/**
 * Utility object containing various helper methods for plugin and namespace management
 * including features such as namespaced key generation, version checks, and bulk item removal.
 */
object NDUtils {
    /**
     * A configured instance of the `HttpClient` utilizing the `CIO` engine.
     *
     * This client is set up with the following features:
     * - `ContentNegotiation` with JSON support for seamless serialization and deserialization of JSON payloads.
     * - `HttpTimeout` with a request timeout of 10 seconds to handle request operations gracefully under time constraints.
     *
     * This instance is utilized for making HTTP requests, such as fetching the latest plugin release information
     * from external sources, e.g., GitHub API.
     */
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
        install(HttpTimeout) { requestTimeoutMillis = 10000 }
    }

    /**
     * Creates a new `NamespacedKey` using the specified key name and plugin.
     *
     * @param keyName The name of the key to be used for the `NamespacedKey`.
     * @param plugin The plugin associated with the `NamespacedKey`. Defaults to the singleton instance of `NDCore`.
     * @return A new `NamespacedKey` instance associated with the specified key name and plugin.
     */
    fun getNamespacedKey(keyName: String, plugin: Plugin = JavaPlugin.getPlugin(NDCore::class.java)) =
        NamespacedKey(plugin, keyName)

    /**
     * Checks for plugin updates by comparing the current version of the plugin with the latest
     * version available on GitHub. The comparison is performed asynchronously within a coroutine
     * on the IO dispatcher.
     *
     * @param name The repository name of the plugin. Defaults to "NDCore".
     * @param organizationOrUser The organization or username that owns the repository. Defaults to "NelminDev".
     * @param callback A function that receives the result of the update check. The result is an instance
     * of `UpdateCheckResult`, which contains information about whether an update is needed and any
     * associated error messages.
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
     * Compares two semantic version objects to determine if an update is needed.
     *
     * The function compares the major, minor, and patch versions of the provided `current` and `latest`
     * versions. If `latest` is greater in terms of versioning, it returns an `UpdateCheckResult` indicating
     * that an update is required. If*/
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
     * Removes the specified item from the inventory of all online players on the server.
     *
     * @param material The material representing the type of item to be removed from players' inventories.
     */
    fun removeItemFromAllPlayers(material: Material) {
        for (player in Bukkit.getOnlinePlayers()) {
            player.inventory.remove(material)
        }
    }

    /**
     * Removes a specified material item from the inventory of each player in the provided player list.
     *
     * @param playerList A list of players whose inventories will be checked for the specified material.
     * @param material The material item to be removed from each player's inventory.
     */
    fun removeItemFromPlayers(playerList: List<Player>, material: Material) {
        for (player in playerList) {
            player.inventory.remove(material)
        }
    }
}