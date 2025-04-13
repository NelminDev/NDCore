package dev.nelmin.minecraft.menus

import org.bukkit.entity.Player
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

interface MenuInterface : InventoryHolder {
    fun click(player: Player, slot: Int)

    fun setItem(slot: Int, item: ItemStack)
    fun setItem(slot: Int, item: ItemStack, action: (Player) -> Unit)

    fun onSetItem()

    fun open(player: Player) {
        onSetItem()
        player.openInventory(inventory)
    }
}