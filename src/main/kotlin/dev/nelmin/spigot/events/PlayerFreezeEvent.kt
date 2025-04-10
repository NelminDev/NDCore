package dev.nelmin.spigot.events

import dev.nelmin.spigot.players.NDPlayer
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

/**
 * Represents an event triggered when a player is frozen in the game.
 * This event allows for handling custom logic when a player is frozen,
 * such as notifying the player or applying additional effects. The event
 * can be cancelled to prevent the player from being frozen.
 *
 * @param player The `Player` instance associated with this event.
 * @param message An optional message providing context or explaining why
 *                the player is being frozen. This can be displayed to the
 *                player or used for logging purposes.
 * @param ndPlayer An `NDPlayer` instance representing the player, which
 *                 provides extended functionalities and properties for event handling.
 */
class PlayerFreezeEvent(player: Player, val message: String?, val ndPlayer: NDPlayer = NDPlayer(player)) :
    PlayerEvent(player), Cancellable {
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