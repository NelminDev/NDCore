package dev.nelmin.minecraft.listeners

import dev.nelmin.logger.Logger
import dev.nelmin.minecraft.builders.PotionEffectBuilder
import dev.nelmin.minecraft.events.PlayerFreezeEvent
import dev.nelmin.minecraft.players.NDPlayer
import org.bukkit.event.EventHandler
import org.bukkit.potion.PotionEffectType

/**
 * Listener class for handling player freezing events and restricting frozen player actions.
 * Implements the Bukkit Listener interface to handle specific player-related events.
 */
class PlayerFreezeListener : org.bukkit.event.Listener {

    /**
     * Handles the PlayerFreezeEvent to apply specific potion effects to a player when they are frozen.
     * The effects include blindness, slowness, and weakness with maximum intensity and infinite duration.
     * Additionally, it sends a custom message to the player if the event contains a non-empty message.
     *
     * @param event The PlayerFreezeEvent that contains the player to whom the effects will be applied and an optional message.
     */
    @EventHandler
    fun onPlayerFreeze(event: PlayerFreezeEvent) {
        event.player.apply {
            addPotionEffect(
                PotionEffectBuilder(PotionEffectType.BLINDNESS).duration(Int.MAX_VALUE).amplifier(255).build()
            )
            addPotionEffect(
                PotionEffectBuilder(PotionEffectType.SLOWNESS).duration(Int.MAX_VALUE).amplifier(255).build()
            )
            addPotionEffect(
                PotionEffectBuilder(PotionEffectType.WEAKNESS).duration(Int.MAX_VALUE).amplifier(255).build()
            )
        }

        if (!event.message.isNullOrEmpty()) {
            event.player.sendMessage(event.message)
        }

        Logger.queueInfo("The player ${event.player.name} has been frozen.")
    }

    /**
     * Cancels the player's movement if they are currently frozen in place.
     *
     * This event handler listens for the `PlayerMoveEvent`, checks if the player is
     * frozen using the `NDPlayer` class, and cancels the movement if the player is frozen.
     *
     * @param event The PlayerMoveEvent triggered when a player attempts to move.
     */
    @EventHandler
    fun onPlayerMoveWhileFrozen(event: org.bukkit.event.player.PlayerMoveEvent) {
        event.isCancelled = NDPlayer(event.player).isFrozenInPlace
    }
}