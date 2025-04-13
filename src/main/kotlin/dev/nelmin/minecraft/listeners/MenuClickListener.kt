package dev.nelmin.minecraft.listeners

import dev.nelmin.minecraft.events.MenuClickEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MenuClickListener : Listener {

    @EventHandler
    fun onMenuClick(event: MenuClickEvent) {
        event.isCancelled = true
        event.menu.click(event.ndPlayer, event.slot)
    }
}