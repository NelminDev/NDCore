package dev.nelmin.spigot.listeners

import dev.nelmin.spigot.players.NDPlayer

class PlayerQuitListener : org.bukkit.event.Listener {

    @org.bukkit.event.EventHandler
    fun onPlayerQuit(event: org.bukkit.event.player.PlayerQuitEvent) {
        val ndPlayer = NDPlayer(event.player)

        ndPlayer.updatePlayTimeOnQuit()
    }
}