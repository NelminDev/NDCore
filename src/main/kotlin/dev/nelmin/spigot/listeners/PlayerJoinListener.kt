package dev.nelmin.spigot.listeners

import dev.nelmin.spigot.players.NDPlayer
import kotlinx.datetime.Clock
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class PlayerJoinListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: org.bukkit.event.player.PlayerJoinEvent) {
        val ndPlayer = NDPlayer(event.player)

        if (ndPlayer.hasPlayedBefore()) ndPlayer.firstJoinTimestamp = Clock.System.now().toEpochMilliseconds()
    }

}