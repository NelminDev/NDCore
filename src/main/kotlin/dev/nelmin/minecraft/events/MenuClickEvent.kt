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

class MenuClickEvent(
    player: Player,
    val title: Component,
    val slot: Int,
    val menu: Menu,
    val item: ItemStack? = null,
    val ndPlayer: NDPlayer = player.toNDPlayer()
) : Event(), Cancellable {
    /**
     * Flag indicating whether the event has been cancelled.
     *
     * When set to `true`, the event is considered cancelled, and the associated actions will not proceed.
     * This flag is used by the `setCancelled` and `isCancelled` methods to manage the event's cancellation state.
     */
    private var isCancelled = false

    /**
     * Retrieves the list of handlers associated with this event.
     *
     * @return The HandlerList for this event.
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
     *               If true, the event is considered cancelled and any subsequent actions
     *               or behaviors associated with the event will be halted.
     */
    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }

    /**
     * Companion object for `PlayerFreezeEvent` that provides access to the associated `HandlerList`.
     *
     * The companion contains static methods and properties required by the Bukkit event system.
     */
    companion object {
        /**
         * Holds a static reference to the `HandlerList` for managing event handlers
         * associated with this event type.
         *
         * This property is used to register and retrieve handlers for the event,
         * enabling the Bukkit event system to correctly process the event when triggered.
         */
        private val handlerList = HandlerList()

        /**
         * Retrieves the handler list for this event type.
         * The handler list is a static, shared object that manages the registration
         * and execution of event handlers for this specific event.
         *
         * @return The static HandlerList instance associated with this event type.
         */
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}