package dev.nelmin.spigot.listeners

import dev.nelmin.logger.Logger
import dev.nelmin.spigot.events.PlayerUnfreezeEvent
import org.bukkit.event.EventHandler
import org.bukkit.potion.PotionEffectType

/**
 * Handles actions to be performed when a player is unfrozen within the game.
 * This listener removes specific potion effects applied during the freeze and optionally sends a message to the player.
 */
class PlayerUnfreezeListener : org.bukkit.event.Listener {

    /**
     * Handles the unfreezing of a player by removing specific potion effects
     * and optionally sending a message to the player.
     *
     * @param event The PlayerUnfreezeEvent containing details about the player being unfrozen,
     *              including the player instance and an optional message to display.
     */
    @EventHandler
    fun onPlayerUnfreeze(event: PlayerUnfreezeEvent) {
        event.player.apply {
            removePotionEffect(
                PotionEffectType.BLINDNESS
            )
            removePotionEffect(
                PotionEffectType.SLOWNESS
            )
            removePotionEffect(
                PotionEffectType.WEAKNESS
            )
        }

        if (!event.message.isNullOrEmpty()) {
            event.player.sendMessage(event.message)
        }

        Logger.queueInfo("The player ${event.player.name} has been unfrozen.")
    }
}