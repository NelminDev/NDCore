package dev.nelmin.minecraft.menus

import dev.nelmin.minecraft.builders.TextBuilder
import dev.nelmin.minecraft.events.MenuClickEvent
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class Menu(
    private val title: Component,
    rows: Rows,
) : MenuInterface {
    private val actions = mutableMapOf<Int, (Player) -> Unit>()
    private val inventory: Inventory = Bukkit.createInventory(this, rows.size, title)

    constructor(title: String, rows: Int) : this(TextBuilder(title).colorize(), Rows.of(rows))

    override fun click(player: Player, slot: Int) {
        val action = actions[slot] ?: return
        action.invoke(player)
        Bukkit.getServer().pluginManager.callEvent(MenuClickEvent(player, title, slot, this))
    }

    override fun setItem(slot: Int, item: ItemStack) = setItem(slot, item) { }

    override fun setItem(
        slot: Int,
        item: ItemStack,
        action: (Player) -> Unit
    ) {
        this.actions[slot] = action
        inventory.setItem(slot, item)
    }

    override fun onSetItem() {
    }

    override fun getInventory(): Inventory = inventory

    enum class Rows(rows: Int) {
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5);

        val size: Int = rows * 9

        companion object {
            private val sizeToRowsMap = values().associateBy { it.size }

            fun of(rows: Int): Rows = sizeToRowsMap.getValue(rows * 9)
        }
    }
}