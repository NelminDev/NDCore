package dev.nelmin.ndcore.menu;

import dev.nelmin.ndcore.builders.TextBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Simple implementation of Menu interface for creating interactive inventories
 * <p>
 * This class provides a basic menu implementation with support for click actions and item management.
 * The menu size is determined by the number of rows specified during construction.
 */
public abstract class SimpleMenu implements Menu {
    private final Map<Integer, Consumer<Player>> actions = new HashMap<>();
    private final Inventory inventory;

    /**
     * Creates a new SimpleMenu with the specified title and number of rows
     *
     * @param title The title of the menu inventory
     * @param rows  The number of rows in the menu
     * @throws NullPointerException if title or rows is null
     */
    public SimpleMenu(@NotNull String title, @NotNull Rows rows) {
        this(new TextBuilder(title).colorize('&'), rows);
    }

    /**
     * Creates a new SimpleMenu with the specified title and number of rows
     *
     * @param title The title of the menu inventory
     * @param rows  The number of rows in the menu
     * @throws NullPointerException if title or rows is null
     */
    public SimpleMenu(@NotNull Component title, @NotNull Rows rows) {
        Objects.requireNonNull(title, "Title cannot be null");
        Objects.requireNonNull(rows, "Rows cannot be null");
        this.inventory = Bukkit.createInventory(this, rows.size, title);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException     if player is null
     * @throws IllegalArgumentException if slot is negative or greater than inventory size
     */
    @Override
    public void click(@NotNull Player player, int slot) {
        Objects.requireNonNull(player, "Player cannot be null");
        if (slot < 0 || slot >= inventory.getSize()) {
            throw new IllegalArgumentException("Invalid slot: " + slot);
        }
        final Consumer<Player> action = actions.get(slot);
        if (action == null) return;
        action.accept(player);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException     if item is null
     * @throws IllegalArgumentException if slot is negative or greater than inventory size
     */
    @Override
    public void setItem(int slot, @NotNull ItemStack item) {
        Objects.requireNonNull(item, "Item cannot be null");
        setItem(slot, item, player -> {
        });
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException     if item or action is null
     * @throws IllegalArgumentException if slot is negative or greater than inventory size
     */
    @Override
    public void setItem(int slot, @NotNull ItemStack item, @NotNull Consumer<Player> action) {
        Objects.requireNonNull(item, "Item cannot be null");
        Objects.requireNonNull(action, "Action cannot be null");
        if (slot < 0 || slot >= inventory.getSize()) {
            throw new IllegalArgumentException("Invalid slot: " + slot);
        }
        this.actions.put(slot, action);
        this.inventory.setItem(slot, item);
    }

    public abstract void onSetItems();

    /**
     * {@inheritDoc}
     *
     * @return The inventory instance for this menu, never null
     */
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    /**
     * Represents the number of rows available in the menu
     */
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