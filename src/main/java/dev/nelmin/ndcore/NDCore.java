package dev.nelmin.ndcore;

import dev.nelmin.ndcore.listener.InventoryClickListener;
import dev.nelmin.ndcore.logger.NDLogger;
import lombok.Getter;

/**
 * Core plugin providing persistent property management and custom menu functionality
 * <p>
 * Handles player data persistence and cleanup of offline players
 */
@Getter
public final class NDCore extends NDPlugin {

    @Override
    public void enable() {
        super.logger(new NDLogger("NDCore", this));
        logger().info("Loading NDCore", getPluginMeta().getVersion(), "by NelminDev");
        logger().info("Registering event listeners...");
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        logger().info("Successfully registered event listeners!");

        logger().info("Successfully loaded NDCore!");
    }

    @Override
    public void disable() {
        logger().info("Disabling NDCore...");
        logger().info("Goodbye, have a nice day! - NelminDev");
    }
}