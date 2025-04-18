package dev.nelmin.ndcore.commands;

import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class NDCommand extends Command {

    public NDCommand() {
        this("");
    }

    protected NDCommand(@NotNull String name) {
        super(name);
    }

    protected NDCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }
}