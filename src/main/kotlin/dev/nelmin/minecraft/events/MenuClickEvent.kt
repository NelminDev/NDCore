package dev.nelmin.minecraft.events

import dev.nelmin.minecraft.menus.Menu
import dev.nelmin.minecraft.players.NDPlayer
import dev.nelmin.minecraft.players.toNDPlayer
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

/**
 * Represents an event triggered when a player clicks on a slot in a custom menu.
 * This event contains detailed information about the click action, including the player
 * involved, the menu instance, the slot clicked, and the item present in the slot.
 *
 * The event extends the `Event` class and implements the `Cancellable` interface, allowing
 * the ability to cancel the event and prevent the associated action from being executed.
 *
 * @param player The player who triggered the menu click event.
 * @param title The title of the menu as a `Component`.
 * @param slot The slot index within the menu that was clicked.
 * @param menu The custom `Menu` instance where the click occurred.
 * @param item The `ItemStack` present in the clicked slot, or null if the slot was empty.
 * @param ndPlayer The custom representation of the player as an `NDPlayer` instance.
 */
class MenuClickEvent(
    player: Player,
    val title: Component,
    val slot: Int,
    val menu: Menu,
    val item: ItemStack? = null,
    val ndPlayer: NDPlayer = player.toNDPlayer()
) : Event(), Cancellable {
    /**
     * Indicates whether the event has been cancelled.
     *
     * This property is used to manage the cancellation state of the event. When set to `true`,
     * the event is considered cancelled, preventing any further actions or behaviors associated
     * with the event from being executed. The value of this property is typically managed
     * via the `isCancelled` and `setCancelled` methods.
     */
    private var isCancelled = false

    /**
     * Retrieves the list of handlers for this event.
     *
     * This method is required for all Bukkit events to allow the registration
     * and management of event handlers associated with this specific event type.
     *
     * @return The HandlerList instance managing the handlers for this event.
     */
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    /**
     * Checks whether the event has been cancelled.
     *
     * @return true if the event is cancelled, false otherwise.
     */
    override fun isCancelled(): Boolean {
        return isCancelled
    }

    /**
     * Sets the cancellation state of the event.
     *
     * @param cancel A boolean value indicating whether the event should be cancelled.
     *               If true, the event is considered cancelled, and any associated actions will be halted.
     */
    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }

    /**
     * Companion object for `MenuClickEvent` that provides access to the associated `HandlerList`.
     *
     * This companion object contains static properties and methods required by the Bukkit event system to
     * register and manage event handlers for this specific event type.
     */
    companion object {
        /**
         * A static `HandlerList` instance used to manage the registration and retrieval
         * of event handlers for this specific event type.
         *
         * This property is required by the Bukkit event system to handle events appropriately.
         * It ensures that all registered event listeners are properly invoked when the event is triggered.
         */
        private val handlerList = HandlerList()

        /**
         * Retrieves the handler list for this event type.
         * The handler list is a static object used by the Bukkit event system
         * to manage the registration and invocation of event handlers associated
         * with this specific event.
         *
         * @return The static HandlerList instance associated with this event type.
         */
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}