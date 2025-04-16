package dev.nelmin.ndcore.listener;

import dev.nelmin.ndcore.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Handles inventory click events for custom menus
 * <p>
 */
public class InventoryClickListener implements Listener {

    /**
     * Processes clicks in custom menu inventories
     * <p>
     * Cancels the event and delegates handling to the menu implementation
     *
     * @param event The click event to process
     */
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        final Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;
        if (!(clickedInventory.getHolder() instanceof Menu menu)) return;

        event.setCancelled(true);
        menu.click((Player) event.getWhoClicked(), event.getSlot());
    }
}