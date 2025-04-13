package dev.nelmin.minecraft.listeners

import dev.nelmin.minecraft.players.toNDPlayer
import kotlinx.datetime.Clock
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerJoinListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: org.bukkit.event.player.PlayerJoinEvent) {
        val ndPlayer = event.player.toNDPlayer()

        if (ndPlayer.hasPlayedBefore()) ndPlayer.firstJoinTimestamp = Clock.System.now().toEpochMilliseconds()
    }

}