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
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class NDPlugin extends JavaPlugin {
    @Getter
    private final @NotNull Map<UUID, PersistentPropertyManager> playerPropertyManagers = new ConcurrentHashMap<>();
    @Getter
    private final @NotNull List<BasicNDPlayer> basicNDPlayers = new ArrayList<>();
    private @NotNull NDLogger ndLogger;

    protected NDPlugin() {
        ndLogger = new NDLogger(getClass().getSimpleName() + " is loading...", this);
    }

    @Override
    public void onLoad() {
        ndLogger = new NDLogger(
                getPluginMeta().getName(),
                this
        );

        load();
    }

    public void load() {
    }

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

    public abstract void enable();

    @Override
    public void onDisable() {
        playerPropertyManagers.clear();
        disable();
    }

    public abstract void disable();
    
    public static <T extends JavaPlugin> NDPlugin getNDPlugin(Class<T> clazz) {
        return (NDPlugin) JavaPlugin.getPlugin(clazz);
    }

    /**
     * @deprecated Replaced by {@link #logger()} since NDCore v3.0 to provide enhanced logging functionality.
     * It uses {@link NDLogger} instead of {@link Logger}.
     * You can use this ({@link #getLogger}) if you do not like {@link NDLogger}
     */
    @Override
    @Deprecated(since = "NDCore v3.0")
    public @NotNull Logger getLogger() {
        return super.getLogger();
    }

    public NDLogger logger() {
        return this.ndLogger;
    }

    public void logger(NDLogger ndLogger) {
        this.ndLogger = ndLogger;
    }
}