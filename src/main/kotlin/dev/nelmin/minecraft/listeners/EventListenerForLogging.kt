package dev.nelmin.minecraft.listeners

import dev.nelmin.logger.Logger
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerGameModeChangeEvent
import org.bukkit.event.player.PlayerLevelChangeEvent

class EventListenerForLogging : Listener {
    @EventHandler
    fun onPayerJoin(event: org.bukkit.event.player.PlayerJoinEvent) {
        val message = if (!event.player.hasPlayedBefore())
            "Player ${event.player.name} joined the game the first time."
        else
            "Player ${event.player.name} joined the game."
        Logger.queueInfo(message)
    }

    @EventHandler
    fun onPlayerQuit(event: org.bukkit.event.player.PlayerQuitEvent) {
        Logger.queueInfo("Player ${event.player.name} left the game.", logToConsole = false)
    }

    @EventHandler
    fun onPlayerKick(event: org.bukkit.event.player.PlayerKickEvent) {
        Logger.queueInfo("Player ${event.player.name} was kicked from the game for ${event.reason}.")
    }

    @EventHandler
    fun onPlayerChat(event: io.papermc.paper.event.player.AsyncChatEvent) {
        Logger.queueInfo("${event.player.name} said: ${event.message()}")
    }

    @EventHandler
    fun onPlayerCommand(event: org.bukkit.event.player.PlayerCommandPreprocessEvent) {
        Logger.queueInfo("${event.player.name} executed command: ${event.message}")
    }

    @EventHandler
    fun onPlayerDeath(event: org.bukkit.event.entity.PlayerDeathEvent) {
        Logger.queueInfo("${event.player.name} died.")
    }

    @EventHandler
    fun onPlayerRespawn(event: org.bukkit.event.player.PlayerRespawnEvent) {
        Logger.queueInfo("${event.player.name} respawned.")
    }

    @EventHandler
    fun onOpenInventory(event: org.bukkit.event.inventory.InventoryOpenEvent) {
        Logger.queueInfo("${event.player.name} opened a inventory of type ${event.inventory.type} at ${event.inventory.location ?: event.player.location}.")
    }

    @EventHandler
    fun onInventoryClick(event: org.bukkit.event.inventory.InventoryClickEvent) {
        Logger.queueInfo("${event.whoClicked.name} clicked in inventory of type ${event.inventory.type} at ${event.inventory.location ?: event.whoClicked.location}.")
    }

    @EventHandler
    fun onAdvancementDone(event: org.bukkit.event.player.PlayerAdvancementDoneEvent) {
        Logger.queueInfo("${event.player.name} completed an advancement: ${event.advancement.key.key}.")
    }

    @EventHandler
    fun onBlockBreak(event: org.bukkit.event.block.BlockBreakEvent) {
        Logger.queueInfo("${event.player.name} broke a block at ${event.block.location}.")
    }

    @EventHandler
    fun onBlockPlace(event: org.bukkit.event.block.BlockPlaceEvent) {
        Logger.queueInfo("${event.player.name} placed a block at ${event.block.location}.")
    }

    @EventHandler
    fun onPlayerGameModeChange(event: PlayerGameModeChangeEvent) {
        Logger.queueInfo("${event.player.name} changed their game mode to ${event.newGameMode}.")
    }

    @EventHandler
    fun onPlayerInteract(event: org.bukkit.event.player.PlayerInteractEvent) {
        Logger.queueInfo("${event.player.name} interacted with a block at ${event.clickedBlock?.location ?: event.player.location}.")
    }

    @EventHandler
    fun onPlayerLevelChange(event: PlayerLevelChangeEvent) {
        Logger.queueInfo("${event.player.name}'s level was changed ${event.newLevel}.")
    }

    @EventHandler
    fun onEntityDamage(event: org.bukkit.event.entity.EntityDamageEvent) {
        Logger.queueInfo("${event.entity.name} was damaged by ${event.cause}.")
    }

    @EventHandler
    fun onEntityDamageByEntity(event: org.bukkit.event.entity.EntityDamageByEntityEvent) {
        Logger.queueInfo("${event.entity.name} was damaged by ${event.damager.name}.")
    }

    @EventHandler
    fun onPlayerFish(event: org.bukkit.event.player.PlayerFishEvent) {
        Logger.queueInfo("${event.player.name} fished at ${event.player.location}.")
    }
}