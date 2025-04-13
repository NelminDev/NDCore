package dev.nelmin.minecraft.events

import dev.nelmin.minecraft.players.NDPlayer
import dev.nelmin.minecraft.players.toNDPlayer
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

/**
 * Represents an event triggered when a player is unfrozen in the game.
 * This event is designed to handle specific behaviors or actions that
 * should occur when a player is no longer in a frozen state. It allows
 * for customized handling of the unfreezing process through event listeners.
 *
 * @param player The `Player` instance associated with this event.
 * @param message An optional message providing context or explanation for
 *                why the player is being unfrozen. This can be used as part
 *                of notifications or logging.
 * @param ndPlayer An `NDPlayer` instance representing the player with
 *                 extended functionality for handling persistent data beyond
 *                 standard Bukkit player operations.
 */
class PlayerUnfreezeEvent(player: Player, val message: String?, val ndPlayer: NDPlayer = player.toNDPlayer()) :
    PlayerEvent(player), Cancellable {
    /**
     * Indicates whether the event has been cancelled.
     *
     * When set to `true`, the event is marked as cancelled, preventing
     * its associated actions or behaviors from being executed. This property
     * acts as the internal state managed through the `isCancelled` and
     * `setCancelled` methods within the context of event handling.
     */
    private var isCancelled = false

    /**
     * Retrieves the list of handlers associated with this event.
     *
     * @return The HandlerList instance managing the event handlers for this event type.
     */
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    /**
     * Checks if the event has been cancelled.
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
     *               When set to true, the event is considered cancelled, and subsequent actions or processing
     *               related to the event will be halted.
     */
    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }

    /**
     * Companion object for `PlayerUnfreezeEvent` that provides access to the associated `HandlerList`.
     *
     * This companion object contains static methods and properties required by the Bukkit event system
     * to register and handle events of this type.
     */
    companion object {
        /**
         * Maintains a static reference to the `HandlerList` for the event.
         *
         * This property is used to register and manage handlers associated with the event type,
         * allowing the Bukkit event system to process the event when it is triggered.
         */
        private val handlerList = HandlerList()

        /**
         * Retrieves the handler list for the `PlayerUnfreezeEvent` type.
         * The handler list is a static object shared among all instances of this event
         * and is used to manage the registration and execution of event handlers.
         *
         * @return The static `HandlerList` instance associated with the `PlayerUnfreezeEvent` class.
         */
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }
}