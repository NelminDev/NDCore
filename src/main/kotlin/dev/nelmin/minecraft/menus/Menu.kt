package dev.nelmin.minecraft.menus

import dev.nelmin.minecraft.builders.TextBuilder
import dev.nelmin.minecraft.events.MenuClickEvent
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * Represents a custom menu interface that extends the functionality of `MenuInterface`.
 * This class is designed to manage an interactive inventory menu with a given title and specified number of rows.
 * It provides functionality to handle click actions, set items in inventory slots, and associate actions with specific slots.
 *
 * @constructor Creates a `Menu` instance with a `Component` title and specified rows.
 * @param title The title of the inventory menu as a `Component`.
 * @param rows The number of rows in the inventory menu, constrained by the `Rows` enum.
 */
class Menu(
    private val title: Component,
    rows: Rows,
) : MenuInterface {
    /**
     * A map that associates inventory slot indices with actions to be executed when the slot is interacted with.
     * The key represents the slot index, while the value is a function that takes a Player as a parameter and
     * defines the action to be executed upon interaction.
     *
     * This property allows dynamic definition and execution of slot-specific behaviors within a menu system.
     * It is used in conjunction with methods such as `setItem` to associate actions with specific items in the inventory.
     */
    private val actions = mutableMapOf<Int, (Player) -> Unit>()

    /**
     * Represents the inventory associated with the menu, initialized with the specified number of rows
     * and title. The inventory is created using the Bukkit API and serves as the underlying storage
     * mechanism for the menu's items.
     */
    private val inventory: Inventory = Bukkit.createInventory(this, rows.size, title)

    /**
     * Secondary constructor for the `Menu` class.
     * Initializes a `Menu` instance by accepting a title as a `String` and the number of rows as an `Int`.
     *
     * @param title The title of the menu provided as a `String`. This will be transformed into a `Component` via `TextBuilder.colorize()`.
     * @param rows The number of rows for the menu provided as an `Int`. This will be mapped to a `Rows` enumeration using `Rows.of()`.
     */
    constructor(title: String, rows: Int) : this(TextBuilder(title).colorize(), Rows.of(rows))

    /**
     * Handles a player's click action within the menu at a specific slot.
     * Executes the corresponding action assigned to the slot, if any,
     * and triggers a `MenuClickEvent`.
     *
     * @param player The player who performed the click action.
     * @param slot The index of the slot within the menu that the player clicked.
     */
    override fun click(player: Player, slot: Int) {
        val action = actions[slot] ?: return
        action.invoke(player)
        Bukkit.getServer().pluginManager.callEvent(MenuClickEvent(player, title, slot, this))
    }

    /**
     * Sets an item in the specified inventory slot.
     * This implementation invokes an overload with an empty action.
     *
     * @param slot The inventory slot where the item will be placed.
     * @param item The item to be placed in the specified slot.
     */
    override fun setItem(slot: Int, item: ItemStack) = setItem(slot, item) { }

    /**
     * Sets an item in the specified inventory slot and associates an action
     * to be executed when the item is interacted with by a player.
     *
     * @param slot The index of the inventory slot where the item will be placed.
     * @param item The item to be placed in the specified slot.
     * @param action The action to be executed when a player clicks the item.
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
     * Invoked to execute custom logic or preparations before items are added to the inventory.
     * This method is typically used to configure the state of the menu, populate inventory slots,
     * or associate actions with specific items prior to the inventory being opened to players.
     *
     * It is recommended to override this method in subclasses of `Menu` to define specific behavior
     * for item setup in different menu implementations.
     */
    override fun onSetItem() {
    }

    /**
     * Retrieves the inventory associated with this menu.
     *
     * @return The current inventory instance linked to this menu.
     */
    override fun getInventory(): Inventory = inventory

    /**
     * Represents the number of rows in a menu, with each row containing 9 slots.
     * This class is used to define and manage the size of an inventory menu.
     *
     * @param rows The number of rows represented by this instance.
     */
    enum class Rows(rows: Int) {
        /**
         * Represents a single row configuration with a predefined constant.
         * This enumeration value corresponds to a row count of 1.
         */
        ONE(1),

        /**
         * Represents the second row configuration in a menu system.
         * Each row corresponds to nine slots, and this enum constant represents two rows, equating to 18 slots.
         */
        TWO(2),

        /**
         * Represents an enumeration value corresponding to a menu with three rows.
         */
        THREE(3),

        /**
         * Represents the fourth defined row configuration in the menu system.
         * Each row consists of a defined number of inventory slots, calculated by multiplying the row count by 9.
         */
        FOUR(4),

        /**
         * Enum class FIVE represents a numeric constant with the value 5.
         *
         * This enumeration can be used where numerical constants are
         * represented as predefined named values for improved readability
         * and maintainability within the codebase.
         *
         * @property value The numerical value associated with the enum constant FIVE.
         */
        FIVE(5);

        /**
         * Represents the calculated size based on the number of rows.
         * This value is determined by multiplying the number of rows by 9.
         */
        val size: Int = rows * 9

        /**
         * Companion object providing helper methods related to the Rows enum.
         */
        companion object {
            /**
             * Maps the calculated `size` property of `Rows` enum constants to their corresponding `Rows` instance.
             *
             * This mapping is constructed by associating each `Rows` enum constant with its `size` property.
             * It is used to efficiently retrieve a `Rows` instance based on a specific inventory size.
             */
            private val sizeToRowsMap = entries.associateBy { it.size }

            /**
             * Retrieves the corresponding Rows object based on the given number of rows.
             *
             * @param rows The number of rows to lookup and calculate the corresponding Rows object.
             * @return The Rows object associated with the given number of rows.
             */
            fun of(rows: Int): Rows = sizeToRowsMap.getValue(rows * 9)
        }
    }
}