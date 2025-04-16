package dev.nelmin.ndcore;

import dev.nelmin.ndcore.persistence.PersistentPropertyManager;
import dev.nelmin.ndcore.players.BasicNDPlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class NDPlugin extends JavaPlugin {
    public static <T extends JavaPlugin> NDPlugin getNDPlugin(Class<T> clazz) {
        return (NDPlugin) JavaPlugin.getPlugin(clazz);
    }

    public abstract @NotNull List<BasicNDPlayer> getBasicNDPlayers();

    public abstract @NotNull Map<UUID, PersistentPropertyManager> getPlayerPropertyManagers();
}