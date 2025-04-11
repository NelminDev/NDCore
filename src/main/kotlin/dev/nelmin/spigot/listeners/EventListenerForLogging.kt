package dev.nelmin.spigot.listeners

import dev.nelmin.logger.Logger
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerGameModeChangeEvent
import org.bukkit.event.player.PlayerLevelChangeEvent

class EventListenerForLogging : Listener {

    @EventHandler
    fun onPlayerJoin(event: org.bukkit.event.player.PlayerJoinEvent) {
        if (!event.player.hasPlayedBefore())
            Logger.infoSilent("Player ${event.player.name} joined the game the first time.")
        else
            Logger.infoSilent("Player ${event.player.name} joined the game.")
    }

    @EventHandler
    fun onPlayerQuit(event: org.bukkit.event.player.PlayerQuitEvent) {
        Logger.infoSilent("Player ${event.player.name} left the game.")
    }

    @EventHandler
    fun onPlayerKick(event: org.bukkit.event.player.PlayerKickEvent) {
        Logger.infoSilent("Player ${event.player.name} was kicked from the game for ${event.reason}.")
    }

    @EventHandler
    fun onPlayerChat(event: io.papermc.paper.event.player.AsyncChatEvent) {
        Logger.infoSilent("${event.player.name} said: ${event.message()}")
    }

    @EventHandler
    fun onPlayerCommand(event: org.bukkit.event.player.PlayerCommandPreprocessEvent) {
        Logger.infoSilent("${event.player.name} executed command: ${event.message}")
    }

    @EventHandler
    fun onPlayerDeath(event: org.bukkit.event.entity.PlayerDeathEvent) {
        Logger.infoSilent("${event.player.name} died.")
    }

    @EventHandler
    fun onPlayerRespawn(event: org.bukkit.event.player.PlayerRespawnEvent) {
        Logger.infoSilent("${event.player.name} respawned.")
    }

    @EventHandler
    fun onOpenInventory(event: org.bukkit.event.inventory.InventoryOpenEvent) {
        Logger.infoSilent("${event.player.name} opened a inventory of type ${event.inventory.type} at ${event.inventory.location ?: event.player.location}.")
    }

    @EventHandler
    fun onInventoryClick(event: org.bukkit.event.inventory.InventoryClickEvent) {
        Logger.infoSilent("${event.whoClicked.name} clicked in inventory of type ${event.inventory.type} at ${event.inventory.location ?: event.whoClicked.location}.")
    }

    @EventHandler
    fun onAdvancementDone(event: org.bukkit.event.player.PlayerAdvancementDoneEvent) {
        Logger.infoSilent("${event.player.name} completed an advancement: ${event.advancement.key.key}.")
    }

    @EventHandler
    fun onBlockBreak(event: org.bukkit.event.block.BlockBreakEvent) {
        Logger.infoSilent("${event.player.name} broke a block at ${event.block.location}.")
    }

    @EventHandler
    fun onBlockPlace(event: org.bukkit.event.block.BlockPlaceEvent) {
        Logger.infoSilent("${event.player.name} placed a block at ${event.block.location}.")
    }

    @EventHandler
    fun onPlayerGameModeChange(event: PlayerGameModeChangeEvent) {
        Logger.infoSilent("${event.player.name} changed their game mode to ${event.newGameMode}.")
    }

    @EventHandler
    fun onPlayerInteract(event: org.bukkit.event.player.PlayerInteractEvent) {
        Logger.infoSilent("${event.player.name} interacted with a block at ${event.clickedBlock?.location ?: event.player.location}.")
    }

    @EventHandler
    fun onPlayerLevelChange(event: PlayerLevelChangeEvent) {
        Logger.infoSilent("${event.player.name}'s level was changed ${event.newLevel}.")
    }

    @EventHandler
    fun onEntityDamage(event: org.bukkit.event.entity.EntityDamageEvent) {
        Logger.infoSilent("${event.entity.name} was damaged by ${event.cause}.")
    }

    @EventHandler
    fun onEntityDamageByEntity(event: org.bukkit.event.entity.EntityDamageByEntityEvent) {
        Logger.infoSilent("${event.entity.name} was damaged by ${event.damager.name}.")
    }

    @EventHandler
    fun onPlayerFish(event: org.bukkit.event.player.PlayerFishEvent) {
        Logger.infoSilent("${event.player.name} fished at ${event.player.location}.")
    }
}