package dev.nelmin.ndcore;

import dev.nelmin.ndcore.logger.NDLogger;
import dev.nelmin.ndcore.persistence.PersistentPropertyManager;
import dev.nelmin.ndcore.players.BasicNDPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Base plugin class providing core functionality for NelminDev plugins.
 * <p>
 * Handles player data persistence, cleanup of offline players, and enhanced logging.
 * Subclasses must implement {@link #enable()} and {@link #disable()} methods.
 *
 * @implNote Uses async cleanup task for offline player data
 */
public abstract class NDPlugin extends JavaPlugin {
    @Getter
    private final @NotNull Map<UUID, PersistentPropertyManager> playerPropertyManagers = new ConcurrentHashMap<>(512);
    @Getter
    private final @NotNull List<BasicNDPlayer> basicNDPlayers = new ArrayList<>(512);
    /**
     * Creates a new NDPlugin instance with a default logger.
     * The logger name will be the simple class name with " is loading..." appended.
     */
    protected NDPlugin() {
        ndLogger = new NDLogger(getClass().getSimpleName() + " is loading...", this);
    }
    private @NotNull NDLogger ndLogger;

    /**
     * Gets an NDPlugin instance from a JavaPlugin class.
     *
     * @param <T>   The type of JavaPlugin
     * @param clazz The class of the plugin to get instance for
     * @return The NDPlugin instance for the specified plugin class
     * @throws ClassCastException   if the plugin is not an NDPlugin
     * @throws NullPointerException if clazz is null
     */
    public static <T extends JavaPlugin> @NotNull NDPlugin getNDPlugin(@NotNull Class<T> clazz) throws ClassCastException {
        Objects.requireNonNull(clazz, "clazz cannot be null");
        return (NDPlugin) JavaPlugin.getPlugin(clazz);
    }

    /**
     * Called when the plugin is loaded by the server.
     * Creates a new logger with the plugin's name and calls {@link #load()}.
     */
    @Override
    public void onLoad() {
        ndLogger = new NDLogger(
                getPluginMeta().getName(),
                this
        );

        load();
    }

    /**
     * Called when the plugin is loaded.
     * Override this method to add custom load logic.
     */
    public void load() {
    }

    /**
     * Called when the plugin is enabled by the server.
     * Sets up periodic cleanup of offline player data and calls {@link #enable()}.
     */
    @Override
    public void onEnable() {
        // Schedule cleanup task for offline players
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            Set<UUID> onlineUUIDs = Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getUniqueId)
                    .collect(Collectors.toSet());
            playerPropertyManagers.forEach((playerUUID, propertyManager) -> {
                if (!onlineUUIDs.contains(playerUUID))
                    playerPropertyManagers.remove(playerUUID);
            });
        }, 0, 6000L); // Runs every 5 Minutes

        enable();
    }

    /**
     * Called when the plugin is enabled.
     * Subclasses must implement this method to add custom enable logic.
     */
    public abstract void enable();

    /**
     * Called when the plugin is disabled by the server.
     * Clears player data and calls {@link #disable()}.
     */
    @Override
    public void onDisable() {
        playerPropertyManagers.clear();
        disable();
    }

    /**
     * Called when the plugin is disabled.
     * Subclasses must implement this method to add custom disable logic.
     */
    public abstract void disable();

    /**
     * @return The default Bukkit logger for this plugin
     * @deprecated Replaced by {@link #logger()} since NDCore v3.0 to provide enhanced logging functionality.
     * It uses {@link NDLogger} instead of {@link Logger}.
     * You can use this ({@link #getLogger}) if you do not like {@link NDLogger}
     */
    @Override
    @Deprecated(since = "NDCore v3.0")
    public @NotNull Logger getLogger() {
        return super.getLogger();
    }

    /**
     * Gets the NDLogger instance for this plugin.
     *
     * @return The NDLogger instance
     */
    public @NotNull NDLogger logger() {
        return this.ndLogger;
    }

    /**
     * Sets the NDLogger instance for this plugin.
     *
     * @param ndLogger The NDLogger instance to set
     * @throws NullPointerException if ndLogger is null
     */
    public void logger(@NotNull NDLogger ndLogger) throws NullPointerException {
        this.ndLogger = Objects.requireNonNull(ndLogger, "ndLogger cannot be null");
    }
}