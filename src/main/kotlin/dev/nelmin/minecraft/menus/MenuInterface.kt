package dev.nelmin.minecraft.menus

import org.bukkit.entity.Player
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

/**
 * Represents a generalized menu interface for managing inventory interactions.
 * This interface is primarily designed for customization and control of player interactions
 * within an inventory-based menu system.
 */
interface MenuInterface : InventoryHolder {
    /**
     * Handles the click action performed by a player on a specific slot in the menu.
     * Executes any action mapped to the clicked slot and triggers a corresponding event.
     *
     * @param player The player performing the click action.
     * @param slot The slot index that was clicked within the menu.
     */
    fun click(player: Player, slot: Int)

    /**
     * Sets an item in the specified inventory slot.
     *
     * @param slot The slot index where the item should be placed.
     * @param item The ItemStack to be placed in the given slot.
     */
    fun setItem(slot: Int, item: ItemStack)

    /**
     * Sets an item in the specified inventory slot and associates an action to be executed
     * when the item is clicked by a player.
     *
     * @param slot The inventory slot where the item will be placed.
     * @param item The item to be placed in the specified slot.
     * @param action The action to be executed when a player interacts with the item.
     */
    fun setItem(slot: Int, item: ItemStack, action: (Player) -> Unit)

    /**
     * This method is called to perform any actions or logic necessary before items are set in the inventory.
     * It is intended to be overridden in implementations of the `MenuInterface` to customize behavior.
     * Typically used in conjunction with the `open` function to prepare the inventory before it is displayed to a player.
     */
    fun onSetItem()

    /**
     * Opens the inventory associated with this menu for the specified player.
     * This method ensures that the inventory is prepared by calling `onSetItem`
     * before being opened for the player.
     *
     * @param player The player for whom the inventory should be opened.
     */
    fun open(player: Player) {
        onSetItem()
        player.openInventory(inventory)
    }
}