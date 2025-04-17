package dev.nelmin.ndcore.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents a menu interface for creating interactive inventories
 * <p>
 * This interface extends InventoryHolder to provide menu functionality with click handling and item management.
 * All implementations must ensure thread-safety when manipulating inventory contents.
 */
public interface Menu extends InventoryHolder {

    /**
     * Handles click events in the menu
     * <p>
     * This method is called when a player clicks a slot in the menu.
     * The implementation should handle the click event appropriately based on the slot.
     *
     * @param player The player who clicked, must not be null
     * @param slot   The slot that was clicked (0-based index)
     * @throws IllegalArgumentException if slot is negative or greater than inventory size
     * @throws NullPointerException     if player is null
     */
    void click(@NotNull Player player, int slot);

    /**
     * Sets an item in the menu without click action
     * <p>
     * Places an item in the specified slot without associating any click behavior.
     *
     * @param slot The slot to place the item in (0-based index)
     * @param item The item to set, must not be null
     * @throws IllegalArgumentException if slot is negative or greater than inventory size
     * @throws NullPointerException     if item is null
     */
    void setItem(int slot, @NotNull ItemStack item);

    /**
     * Sets an item in the menu with click action
     * <p>
     * Places an item in the specified slot and associates a click action that will be executed
     * when a player clicks the item.
     *
     * @param slot   The slot to place the item in (0-based index)
     * @param item   The item to set, must not be null
     * @param action The action to perform when clicked, must not be null
     * @throws IllegalArgumentException if slot is negative or greater than inventory size
     * @throws NullPointerException     if item or action is null
     */
    void setItem(int slot, @NotNull ItemStack item, @NotNull Consumer<Player> action);

    /**
     * Called when items need to be set in the menu
     * <p>
     * This method is invoked before opening the menu to allow implementation
     * to populate the inventory with items.
     */
    void onSetItems();

    /**
     * Opens the menu for a player
     * <p>
     * Initializes the menu contents by calling onSetItems() and opens the inventory
     * for the specified player.
     *
     * @param player The player to open the menu for, must not be null
     * @throws NullPointerException if player is null
     */
    default void open(@NotNull Player player) {
        Objects.requireNonNull(player, "Player cannot be null");
        onSetItems();
        player.openInventory(this.getInventory());
    }
}