package dev.nelmin.ndcore;

import dev.nelmin.ndcore.listener.InventoryClickListener;
import dev.nelmin.ndcore.logger.NDLogger;
import lombok.Getter;

import java.util.Objects;

/**
 * Core plugin providing persistent property management and custom menu functionality.
 * <p>
 * Handles player data persistence and cleanup of offline players.
 * This is the main entry point for the NDCore plugin.
 * <p>
 * Provides functionality for:
 * <ul>
 *   <li>Managing player data persistence</li>
 *   <li>Cleaning up data for offline players</li>
 *   <li>Custom menu system</li>
 *   <li>Logging via NDLogger</li>
 * </ul>
 *
 * @implNote Registers InventoryClickListener for menu functionality
 */
@Getter
public final class NDCore extends NDPlugin {

    /**
     * Enables the NDCore plugin.
     * <p>
     * Sets up the logger, registers event listeners, and performs other initialization tasks:
     * <ul>
     *   <li>Initializes NDLogger for plugin logging</li>
     *   <li>Registers InventoryClickListener for menu handling</li>
     * </ul>
     *
     * @throws NullPointerException if any required dependencies are null
     */
    @Override
    public void enable() {
        super.logger(new NDLogger("NDCore", this));

        Objects.requireNonNull(logger(), "logger cannot be null");
        Objects.requireNonNull(getPluginMeta(), "plugin meta cannot be null");
        Objects.requireNonNull(getServer(), "server cannot be null");

        logger().info("Loading NDCore", getPluginMeta().getVersion(), "by NelminDev");
        logger().info("Registering event listeners...");
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        logger().info("Successfully registered event listeners!");

        logger().info("Successfully loaded NDCore!");
    }

    /**
     * Disables the NDCore plugin.
     * <p>
     * Performs cleanup tasks before the plugin is disabled:
     * <ul>
     *   <li>Logs shutdown messages</li>
     * </ul>
     *
     * @throws NullPointerException if logger is null
     */
    @Override
    public void disable() {
        Objects.requireNonNull(logger(), "logger cannot be null");
        logger().info("Disabling NDCore...");
        logger().info("Goodbye, have a nice day! - NelminDev");
    }
}