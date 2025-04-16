package dev.nelmin.ndcore;

import dev.nelmin.ndcore.listener.InventoryClickListener;
import dev.nelmin.ndcore.logger.NDLogger;
import dev.nelmin.ndcore.persistence.PersistentPropertyManager;
import dev.nelmin.ndcore.players.BasicNDPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Core plugin providing persistent property management and custom menu functionality
 * <p>
 * Handles player data persistence and cleanup of offline players
 */
@Getter
public final class NDCore extends NDPlugin {
    private final @NotNull Map<UUID, PersistentPropertyManager> playerPropertyManagers = new ConcurrentHashMap<>();
    private final @NotNull List<BasicNDPlayer> basicNDPlayers = new ArrayList<>();
    private NDLogger ndLogger;

    @Override
    public void onEnable() {
        ndLogger = new NDLogger("NDCore", this);
        ndLogger.info("Loading NDCore", getPluginMeta().getVersion(), "by NelminDev");
        ndLogger.info("Registering event listeners...");
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        ndLogger.info("Successfully registered event listeners!");

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

        ndLogger.info("Successfully loaded NDCore!");
    }

    @Override
    public void onDisable() {
        ndLogger.info("Disabling NDCore...");
        playerPropertyManagers.clear();
        ndLogger.info("Goodbye, have a nice day! - NelminDev");
    }
}