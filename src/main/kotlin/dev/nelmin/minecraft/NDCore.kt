package dev.nelmin.minecraft

import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory
import dev.nelmin.logger.Logger
import dev.nelmin.minecraft.listeners.MenuClickListener
import dev.nelmin.minecraft.listeners.PlayerFreezeListener
import dev.nelmin.minecraft.listeners.PlayerJoinListener
import dev.nelmin.minecraft.listeners.PlayerQuitListener
import dev.nelmin.minecraft.listeners.PlayerUnfreezeListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.sync.Mutex
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

/**
 * The NDCore class is a custom implementation of a JavaPlugin for Bukkit/Spigot servers,
 * providing core functionalities, event handling, password hashing, and update checks.
 * This plugin serves as the central framework for managing player events and utilities
 * in the server environment.
 */
class NDCore : JavaPlugin() {

    /**
     * Companion object for the NDCore class, providing utility functions and shared resources.
     */
    companion object {
        /**
         * A `CoroutineScope` associated with the `SupervisorJob` that manages the execution of coroutines within the scope.
         * This scope allows the creation and structured management of coroutines in a way that failures in child coroutines
         * do not propagate to sibling coroutines. It is primarily used to handle asynchronous tasks in a controlled lifecycle.
         */
        val coroutineScope = CoroutineScope(SupervisorJob())

        /**
         * A mutual exclusion lock used to protect shared resources from being accessed by multiple threads concurrently.
         *
         * This `Mutex` instance ensures that only one coroutine at a time can operate on the protected resource,
         * allowing for proper synchronization and avoiding race conditions.
         */
        val mutex = Mutex()

        /**
         * Instance of the Argon2 password hashing algorithm using the Argon2id variant.
         * This variable is utilized for securely hashing passwords with a balance
         * of computational effort and resistance to side-channel attacks.
         */
        private val argon2: Argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)

        /**
         * Hashes the given password using a secure hashing algorithm.
         *
         * @param password the plain text password to be hashed
         * @return the hashed representation of the password
         */
        fun hashPassword(password: String): String = hashPassword(password.toCharArray())

        /**
         * Hashes a password using the Argon2 algorithm.
         *
         * @param passwordCharArray The password represented as a character array that will be hashed.
         * @return A hashed representation of the input password as a String.
         */
        fun hashPassword(passwordCharArray: CharArray): String = argon2.hash(4, 65536, 1, passwordCharArray)

        /**
         * Verifies whether the provided password matches the given hash.
         *
         * @param password the plain text password to verify.
         * @param hash the hashed password to compare against.
         * @return true if the password matches the hash, false otherwise.
         */
        fun verifyPassword(password: String, hash: String): Boolean = verifyPassword(password.toCharArray(), hash)

        /**
         * Verifies if the provided password matches the given hash using the Argon2 algorithm.
         *
         * @param passwordCharArray The password provided as a character array.
         * @param hash The hashed value to compare the password against.
         * @return True if the password matches the hash, otherwise false.
         */
        fun verifyPassword(passwordCharArray: CharArray, hash: String): Boolean = argon2.verify(hash, passwordCharArray)

        /**
         * Represents the YAML configuration used within the NDCore class.
         * This variable holds the parsed data from a YAML file, allowing access to configuration settings.
         * It is nullable and may be uninitialized. Ensure proper initialization before utilizing its data.
         */
        var config: YamlConfiguration? = null

        /**
         * Represents the prefix used within the application, which is retrieved from the configuration or defaults
         * to a predefined format if no valid value is provided.
         *
         * This prefix is used as a marker or identifier in specific parts of the application, often for logging,
         * message formatting, or similar purposes.
         *
         * The default value is set to "&9&lNelmin &8&l»&r" when the configuration does not provide a non-empty value.
         */
        var prefix: String = config?.getString("prefix").takeIf { !it.isNullOrEmpty() } ?: "&9&lNelmin &8&l»&r"
    }

    /**
     * Provides access to the server's plugin management system, enabling interactions
     * with plugins such as loading, enabling, disabling, and retrieving plugin details.
     */
    val pluginManager = server.pluginManager

    /**
     * Handles the initialization logic for the plugin when it is enabled in the server.
     *
     * This method performs the following tasks:
     * - Configures the Logger with the appropriate name and coroutine scope.
     * - Starts listening for incoming log messages.
     * - Registers various event listeners for handling player and menu-related events:
     *   - `PlayerFreezeListener`: Handles events related to freezing players.
     *   - `PlayerUnfreezeListener`: Handles events related to unfreezing players.
     *   - `PlayerJoinListener`: Manages player join-related actions.
     *   - `PlayerQuitListener`: Manages player quit-related actions.
     *   - `MenuClickListener`: Handles menu interaction events.
     * - Logs the enablement of the plugin.
     * - Initiates a version check to determine if a newer version of the plugin is available.
     */
    @ApiStatus.Experimental
    override fun onEnable() {
        Logger.setName("NDCore")
        Logger.coroutineScope = coroutineScope

        Logger.startListeningForLogMessages(coroutineScope)

        pluginManager.registerEvents(PlayerFreezeListener(), this)
        pluginManager.registerEvents(PlayerUnfreezeListener(), this)
        pluginManager.registerEvents(PlayerJoinListener(), this)
        pluginManager.registerEvents(PlayerQuitListener(), this)

        pluginManager.registerEvents(MenuClickListener(), this)

        // pluginManager.registerEvents(EventListenerForLogging(), this)

        Logger.queueInfo("Plugin enabled!")

        Logger.queueInfo("Checking for updates...")
        NDUtils.checkForPluginUpdates(
            currentVersion = getPlugin(this::class.java).pluginMeta.version,
            callback = { (hasUpdate, updateType, _, currentVersion) ->
                if (hasUpdate) {
                    Logger.queueWarn("An update is available for NDCore ($currentVersion -> $updateType)")
                } else {
                    Logger.queueInfo("You are running the latest version of NDCore ($currentVersion)")
                }
            })
    }

    /**
     * Called when the plugin is disabled.
     *
     * This method performs cleanup operations when the plugin is stopped,
     * such as logging the plugin's disabled state and stopping the log message listener.
     */
    override fun onDisable() {
        Logger.queueInfo("Plugin disabled!")
        Logger.stopListeningForLogMessages()
    }
}
