package dev.nelmin.ndcore.listener;

import dev.nelmin.ndcore.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Handles inventory click events for custom menus
 * <p>
 * This listener manages clicks in custom menu inventories by cancelling the default behavior
 * and delegating the click handling to the appropriate menu implementation.
 */
public class InventoryClickListener implements Listener {

    /**
     * Processes clicks in custom menu inventories
     * <p>
     * When a player clicks in a custom menu inventory:
     * <ul>
     *   <li>The event is cancelled to prevent default behavior</li>
     *   <li>The click is delegated to the menu implementation for handling</li>
     * </ul>
     *
     * @param event The inventory click event to process
     * @throws NullPointerException if event is null
     */
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        Objects.requireNonNull(event, "event cannot be null");
        final Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;
        if (!(clickedInventory.getHolder() instanceof Menu menu)) return;

        event.setCancelled(true);
        menu.click((Player) event.getWhoClicked(), event.getSlot());
    }
}