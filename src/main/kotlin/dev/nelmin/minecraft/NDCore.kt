package dev.nelmin.minecraft

import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory
import dev.nelmin.logger.Logger
import dev.nelmin.minecraft.listeners.PlayerFreezeListener
import dev.nelmin.minecraft.listeners.PlayerJoinListener
import dev.nelmin.minecraft.listeners.PlayerQuitListener
import dev.nelmin.minecraft.listeners.PlayerUnfreezeListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.sync.Mutex
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin

/**
 * NDCore is the main plugin class for managing the core functionalities of the plugin.
 * It extends the `JavaPlugin` class and provides lifecycle management, event registration,
 * and configuration management for the plugin.
 */
class NDCore : JavaPlugin() {

    /**
     * Companion object for the NDCore class.
     * Provides shared functionality, properties, and configuration
     * management for the NDCore plugin.
     */
    companion object {
        /**
         * Singleton instance of the NDCore plugin.
         * Initialized during the plugin's `onEnable` lifecycle method.
         */
        private lateinit var instance: NDCore

        /**
         * Returns the current instance of the `NDCore` plugin.
         * This method allows access to the singleton instance of the plugin.
         *
         * @return The `NDCore` instance.
         */
        fun instance() = instance

        /**
         * A shared [CoroutineScope] instance backed by a [SupervisorJob].
         * This scope can be used to launch coroutines that are lifecycle-aware
         * and tied to the [NDCore] plugin lifecycle.
         * Coroutines launched in this scope will not be cancelled if one of its child
         * coroutines fails, due to the behavior of [SupervisorJob].
         */
        val coroutineScope = CoroutineScope(SupervisorJob())

        /**
         * A mutex used to synchronize access to shared resources or critical sections
         * within the application. This prevents race conditions and ensures thread-safe
         * operations where concurrent access could occur.
         */
        val mutex = Mutex()

        private val argon2: Argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)

        fun hashPassword(password: String): String = hashPassword(password.toCharArray())
        fun hashPassword(passwordCharArray: CharArray): String = argon2.hash(4, 65536, 1, passwordCharArray)
        fun verifyPassword(password: String, hash: String): Boolean = verifyPassword(password.toCharArray(), hash)
        fun verifyPassword(passwordCharArray: CharArray, hash: String): Boolean = argon2.verify(hash, passwordCharArray)

        /**
         * Represents the configuration data for the plugin.
         * This variable is expected to hold the parsed YAML configuration file.
         * It can be null if the configuration has not been loaded or initialized.
         */
        var config: YamlConfiguration? = null

        /**
         * The prefix used for messages within the plugin. It is retrieved from the configuration file using the key "prefix".
         * If the value is missing or empty in the configuration, it defaults to "&9&lNelmin &8&l»&r".
         */
        var prefix: String = config?.getString("prefix").takeIf { !it.isNullOrEmpty() } ?: "&9&lNelmin &8&l»&r"
    }

    /**
     * Provides access to the server's plugin manager, allowing for registration
     * and management of plugins and their related operations within the server.
     */
    val pluginManager = server.pluginManager

    /**
     * Handles the initialization of the plugin when it is enabled.
     * Sets up the plugin's instance, logging configuration, and event listeners. Also checks for plugin updates.
     *
     * Key functionalities include:
     * - Setting the singleton instance of the plugin.
     * - Defining the logger's name to identify logs from the plugin.
     * - Registering event listeners for managing custom player actions such as freezing and unfreezing.
     * - Printing log messages to confirm successful plugin loading.
     * - Initiating a check to verify if the plugin is up-to-date by interacting with a GitHub repository,
     *   and logging relevant information about update availability.
     */
    override fun onEnable() {
        instance = this

        Logger.setName("NDCore")
        Logger.coroutineScope = coroutineScope

        Logger.startListeningForLogMessages(coroutineScope)

        pluginManager.registerEvents(PlayerFreezeListener(), this)
        pluginManager.registerEvents(PlayerUnfreezeListener(), this)
        pluginManager.registerEvents(PlayerJoinListener(), this)
        pluginManager.registerEvents(PlayerQuitListener(), this)

        // pluginManager.registerEvents(EventListenerForLogging(), this)

        Logger.queueInfo("Plugin enabled!")

        Logger.queueInfo("Checking for updates...")
        NDUtils.checkForPluginUpdates(callback = { (hasUpdate, updateType) ->
            if (hasUpdate) {
                Logger.queueWarn("An update is available for NDCore (${instance.description.version} -> $updateType)")
            } else {
                Logger.queueInfo("You are running the latest version of NDCore (${instance.description.version})")
            }
        })
    }

    /**
     * Called when the plugin is disabled by the server.
     * This method is triggered as part of the plugin lifecycle and is commonly used to release
     * resources, save plugin state, or perform cleanup operations before the plugin fully unloads.
     */
    override fun onDisable() {
        Logger.queueInfo("Plugin disabled!")
        Logger.stopListeningForLogMessages()
    }
}
