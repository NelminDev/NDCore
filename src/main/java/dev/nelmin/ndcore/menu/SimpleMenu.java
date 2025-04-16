package dev.nelmin.ndcore.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Simple implementation of Menu interface for creating interactive inventories
 */
public class SimpleMenu implements Menu {
    private final Map<Integer, Consumer<Player>> actions = new HashMap<>();
    private final Inventory inventory;

    public SimpleMenu(@NotNull String title, @NotNull Rows rows) {
        this.inventory = Bukkit.createInventory(this, rows.size, Component.text(title));
    }

    @Override
    public void click(@NotNull Player player, int slot) {
        final Consumer<Player> action = actions.get(slot);
        if (action == null) return;
        action.accept(player);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack item) {
        setItem(slot, item, player -> {
        });
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack item, @NotNull Consumer<Player> action) {
        this.actions.put(slot, action);
        this.inventory.setItem(slot, item);
    }

    @Override
    public void onSetItems() {
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public enum Rows {
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5);

        private final int size;

        Rows(int rows) {
            this.size = rows * 9;
        }
    }
}