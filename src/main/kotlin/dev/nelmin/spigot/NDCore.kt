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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.bukkit.plugin.java.JavaPlugin
import java.io.InputStreamReader
import java.util.*

/**
 * The NDCore class serves as the main entry point for the plugin, extending the Bukkit API class JavaPlugin.
 * It manages the plugin's initialization and teardown processes through the `onEnable` and `onDisable` lifecycle methods.
 *
 * This class also provides a singleton instance which can be accessed globally during the plugin's execution.
 */
class NDCore : JavaPlugin() {

    /**
     * Companion object for the NDCore class, providing access to a shared, globally available instance
     * of the NDCore plugin. Facilitates interactions with the plugin instance from other parts of the codebase.
     */
    companion object {
        /**
         * Singleton instance of the `NDCore` plugin class.
         * This property is initialized when the plugin is enabled and is used to provide
         * access to the core functionalities of the plugin throughout the application.
         * It must be referenced only after being properly initialized within the `onEnable()` method.
         */
        lateinit var instance: NDCore
    }

    /**
     * Handles the initialization process when the plugin is enabled.
     *
     * This function sets the static `instance` property to the current plugin instance,
     * allowing other parts of the application to access the main plugin class.
     * It also configures the Logger with a specific name for log entries
     * related to this plugin and outputs an informational message indicating
     * that the plugin has been successfully enabled.
     */
    override fun onEnable() {
        instance = this

        Logger.setName("NDCore")
        Logger.info("Plugin enabled!")

        Logger.info("Checking for updates...")
        checkForUpdates(callback = { (hasUpdate, updateType) ->
            if (hasUpdate) {
                Logger.warn("An update is available for NDCore (${instance.description.version} -> $updateType)")
            } else {
                Logger.info("You are running the latest version of NDCore (${instance.description.version})")
            }
        })
    }

    /**
     * This method is called when the plugin is disabled.
     * It performs any necessary shutdown tasks and logs the disabling of the plugin.
     */
    override fun onDisable() {
        Logger.info("Plugin disabled!")
    }

    fun checkForUpdates(
        name: String = "NDCore",
        organizationOrUser: String = "NelminDev",
        callback: (Pair<Boolean, String?>) -> Unit
    ) =
        CoroutineScope(Dispatchers.Default).launch {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json()
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 10000
                }
            }

            try {
                // Use javaClass instead of this::class.java
                val pluginYml = javaClass.classLoader.getResourceAsStream("plugin.yml")?.use { stream ->
                    Properties().apply { load(InputStreamReader(stream, Charsets.UTF_8)) }
                } ?: run {
                    Logger.warn("Could not load plugin.yml")
                    return@launch callback(Pair(false, null))
                }

                val currentVersion = pluginYml.getProperty("version") ?: run {
                    Logger.warn("Version not found in plugin.yml")
                    return@launch callback(Pair(false, null))
                }

                val githubApiUrl = "https://api.github.com/repos/$organizationOrUser/$name/releases/latest"

                val response: JsonObject = try {
                    client.get(githubApiUrl) {
                        headers {
                            append(HttpHeaders.Accept, "application/vnd.github.v3+json")
                        }
                    }.body()
                } catch (e: ClientRequestException) {
                    when (e.response.status) {
                        HttpStatusCode.NotFound -> Logger.warn("No releases found for $organizationOrUser/$name")
                        else -> Logger.error("GitHub API request failed: ${e.message}")
                    }
                    return@launch callback(Pair(false, null))
                } catch (e: Exception) {
                    Logger.error("Failed to fetch release info: ${e.message}")
                    return@launch callback(Pair(false, null))
                }

                val latestVersion = response["tag_name"]?.jsonPrimitive?.content ?: run {
                    Logger.warn("No tag_name found in GitHub response")
                    return@launch (callback(Pair(false, null)))
                }

                compareVersions(currentVersion, latestVersion.removePrefix("v"))
            } catch (e: Exception) {
                Logger.error("Update check failed: ${e.message}")
                Logger.stacktrace(e)
                callback(Pair(false, null))
            } finally {
                try {
                    client.close()
                } catch (e: Exception) {
                    Logger.error("Failed to close HTTP client: ${e.message}")
                    Logger.stacktrace(e)
                }
            }
        }

    private fun compareVersions(current: String, latest: String): Pair<Boolean, String?> {
        try {
            val currentParts = current.split("-")[0].split(".")
            val latestParts = latest.split("-")[0].split(".")

            val normalizedCurrentParts = currentParts.take(3).map { it.toIntOrNull() ?: 0 }
            val normalizedLatestParts = latestParts.take(3).map { it.toIntOrNull() ?: 0 }

            for (i in 0..2) {
                val currentNum = normalizedCurrentParts.getOrNull(i) ?: 0
                val latestNum = normalizedLatestParts.getOrNull(i) ?: 0

                when {
                    latestNum > currentNum -> return Pair(true, null)
                    currentNum > latestNum -> return Pair(false, null)
                }
            }

            return when {
                isPreRelease(current) && !isPreRelease(latest) -> Pair(true, "Stable")
                !isPreRelease(current) && latest.contains("SNAPSHOT", ignoreCase = true) -> Pair(true, "Snapshot")
                !isPreRelease(current) && latest.contains("ALPHA", ignoreCase = true) -> Pair(true, "Alpha")
                else -> Pair(false, null)
            }
        } catch (e: Exception) {
            Logger.error("Version comparison failed: ${e.message}")
            return Pair(false, null)
        }
    }

    private fun isPreRelease(version: String): Boolean =
        version.contains("SNAPSHOT", ignoreCase = true) ||
                version.contains("ALPHA", ignoreCase = true)
}
