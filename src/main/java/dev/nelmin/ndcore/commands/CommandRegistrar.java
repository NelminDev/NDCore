package dev.nelmin.ndcore.commands;

import dev.nelmin.ndcore.NDPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

public class CommandRegistrar {
    private Field bukkitCommandMap;
    private CommandMap commandMap;

    public CommandRegistrar(NDPlugin plugin) {
        try {
            this.bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            this.bukkitCommandMap.setAccessible(true);
            this.commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            plugin.logger().error("Failed to create Command Registrar:", e.getMessage());
        }
    }

    public CommandRegistrar register(NDCommand command) {
        commandMap.register(command.getName(), command);
        return this;
    }

    public void cleanup() {
        this.commandMap = null;
        this.bukkitCommandMap.setAccessible(false);
        this.bukkitCommandMap = null;
    }
}
