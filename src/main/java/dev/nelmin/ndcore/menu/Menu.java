package dev.nelmin.ndcore.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a menu interface for creating interactive inventories
 * <p>
 * This interface extends InventoryHolder to provide menu functionality with click handling and item management
 */
public interface Menu extends InventoryHolder {

    /**
     * Handles click events in the menu
     * <p>
     *
     * @param player The player who clicked
     * @param slot   The slot that was clicked
     */
    void click(@NotNull Player player, int slot);

    /**
     * Sets an item in the menu without click action
     * <p>
     *
     * @param slot The slot to place the item in
     * @param item The item to set
     */
    void setItem(int slot, @NotNull ItemStack item);

    /**
     * Sets an item in the menu with click action
     * <p>
     *
     * @param slot   The slot to place the item in
     * @param item   The item to set
     * @param action The action to perform when clicked
     */
    void setItem(int slot, @NotNull ItemStack item, @NotNull Consumer<Player> action);

    /**
     * Called when items need to be set in the menu
     */
    void onSetItems();

    /**
     * Opens the menu for a player
     * <p>
     *
     * @param player The player to open the menu for
     */
    default void open(@NotNull Player player) {
        onSetItems();
        player.openInventory(this.getInventory());
    }
}