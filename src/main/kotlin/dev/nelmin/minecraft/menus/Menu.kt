package dev.nelmin.minecraft.menus

import dev.nelmin.minecraft.builders.TextBuilder
import dev.nelmin.minecraft.events.MenuClickEvent
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * Represents a customizable menu that implements the `MenuInterface` to handle inventory-based interactions
 * in a Minecraft plugin. This class provides functionalities for managing menu items, player clicks,
 * and row-based inventory configurations.
 *
 * @property title The title of the inventory, represented as a `Component`.
 * @constructor Creates a menu with the specified title and number of rows.
 * @param rows An instance of `Rows` enum defining the number of rows for the inventory.
 */
open class Menu(
    private val title: Component,
    rows: Rows,
) : MenuInterface {
    /**
     * A mutable map that associates inventory slot indices with their respective click actions.
     * The actions are functions executed when a player interacts with the corresponding slot in the menu.
     *
     * Keys in this map represent the slot indices within the inventory, while the values are lambda functions
     * that accept a `Player` parameter. These lambdas define the behavior that should occur when a player clicks
     * the corresponding slot in the menu.
     *
     * This property is primarily utilized in the `click` and `setItem` methods to manage interactions
     * within the menu.
     */
    private val actions = mutableMapOf<Int, (Player) -> Unit>()

    /**
     * Represents the inventory associated with this menu instance.
     * The inventory is created with the specified number of rows and a title,
     * and is used to manage the items and interactions within the menu.
     *
     * This inventory is the core of the menu's functionality, allowing developers
     * to customize and control the behavior of items and slots for players.
     */
    private val inventory: Inventory = Bukkit.createInventory(this, rows.size, title)

    /**
     * Secondary constructor for the `Menu` class that allows instantiation
     * using a plain string for the menu title and an integer for the number of rows.
     * Converts the provided title into a formatted `Component` and maps the integer
     * into the corresponding `Rows` enum value.
     *
     * @param title The title of the menu as a plain string.
     * @param rows The number of rows in the menu, which will be mapped to a `Rows` enum.
     */
    constructor(title: String, rows: Int) : this(TextBuilder(title).colorize(), Rows.of(rows))

    /**
     * Handles the click action performed by a player on a specific menu slot.
     * Executes the action mapped to the clicked slot, if any, and triggers a `MenuClickEvent`.
     *
     * @param player The player performing the click action.
     * @param slot The slot index that was clicked within the menu.
     */
    override fun click(player: Player, slot: Int) {
        val action = actions[slot] ?: return
        action.invoke(player)
        Bukkit.getServer().pluginManager.callEvent(MenuClickEvent(player, title, slot, this))
    }

    /**
     * Sets an item in the specified slot of the menu's inventory. If an action is associated
     * with the slot, it will be executed when the item in the slot is clicked.
     *
     * @param slot The index of the slot where the item should be placed.
     * @param item The ItemStack to place in the specified slot.
     */
    override fun setItem(slot: Int, item: ItemStack) = setItem(slot, item) { }

    /**
     * Sets an item in the specified inventory slot and assigns an action to be executed
     * when the item is interacted with by a player.
     *
     * @param slot The slot index in the inventory where the item should be placed.
     * @param item The ItemStack to be placed in the specified slot.
     * @param action The action to be executed when a player interacts with the item.
     */
    override fun setItem(
        slot: Int,
        item: ItemStack,
        action: (Player) -> Unit
    ) {
        this.actions[slot] = action
        inventory.setItem(slot, item)
    }

    /**
     * Called prior to setting items in the menu's inventory.
     * This method is designed to provide a hook for preparations or custom logic
     * before items are placed into the inventory slots.
     *
     * Intended to be overridden in subclasses to define specific behavior or setup
     * that needs to occur when the menu's inventory is initialized or updated.
     */
    override fun onSetItem() {
    }

    /**
     * Retrieves the inventory associated with this menu.
     *
     * @return The inventory instance managed by this menu.
     */
    override fun getInventory(): Inventory = inventory

    /**
     * Represents the number of rows in a menu or inventory and provides
     * utility methods for interacting with row sizes.
     *
     * Each row corresponds to a specific number of inventory slots, calculated
     * as rows multiplied by 9. The class also includes a companion object
     * for convenient mapping and retrieval of Rows instances.
     *
     * @property size The total number of slots in the inventory for the given number of rows.
     */
    enum class Rows(rows: Int) {
        /**
         * Represents a single-row inventory layout.
         *
         * This enum constant is part of the `Rows` enumeration and is used to define a menu
         * layout with one row. Each row consists of 9 slots.
         *
         * @param rows The number of rows in the inventory layout.
         */
        ONE(1),

        /**
         * Represents a row configuration with two rows in the inventory system.
         * Each row contains 9 slots, resulting in a total of 18 slots for this configuration.
         */
        TWO(2),

        /**
         * Represents the third row configuration in a menu system.
         * Equivalent to three rows or a total of 27 slots.
         */
        THREE(3),

        /**
         * Represents a row configuration with four rows in the inventory menu.
         * Each row consists of nine slots, making the total size of this configuration 36 slots.
         */
        FOUR(4),

        /**
         * Represents an option specifying a menu with five rows. This constant can be used
         * to define inventories or menus that require exactly five rows, equating to a size
         * of 45 slots.
         *
         * @property size The size of the inventory, calculated as the number of rows multiplied by 9.
         */
        FIVE(5);

        /**
         * Represents the total size of the menu inventory, defined as the number of rows multiplied by 9.
         * This value determines the capacity of the inventory in terms of slots.
         */
        val size: Int = rows * 9

        /**
         * Companion object for the `Rows` enum, providing utility functions for mapping
         * and retrieving `Rows` instances based on defined sizes.
         */
        companion object {
            /**
             * A map that associates the size of a row (in slots) to its corresponding [Rows] enum instance.
             * Designed to enable quick lookup of a [Rows] instance based on the size of the inventory.
             *
             * Key: The size of the inventory (calculated as `rows * 9`).
             * Value: The corresponding [Rows] enum instance.
             */
            private val sizeToRowsMap = entries.associateBy { it.size }

            /**
             * Fetches a Rows object corresponding to the given number of rows.
             *
             * @param rows The number of rows for which the Rows object is to be retrieved.
             * @return The Rows object corresponding to the given number of rows.
             */
            fun of(rows: Int): Rows = sizeToRowsMap.getValue(rows * 9)
        }
    }
}