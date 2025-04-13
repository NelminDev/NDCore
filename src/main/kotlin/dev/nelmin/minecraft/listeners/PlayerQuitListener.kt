package dev.nelmin.minecraft.listeners

import dev.nelmin.minecraft.players.toNDPlayer

class PlayerQuitListener : org.bukkit.event.Listener {

    @org.bukkit.event.EventHandler
    fun onPlayerQuit(event: org.bukkit.event.player.PlayerQuitEvent) {
        val ndPlayer = event.player.toNDPlayer()

        ndPlayer.updatePlayTimeOnQuit()
    }
}