# Menu Package - Introduction

## Overview

The `menu` package in NDCore provides a framework for creating interactive inventory-based menus and GUIs in Minecraft. It simplifies the process of creating custom inventories with clickable items and actions.

## Purpose

The primary purpose of the menu package is to:

- Provide a standardized way to create interactive menus
- Handle the boilerplate code for inventory creation and management
- Manage click events and actions for menu items
- Enable clean, modular menu code with separation of concerns
- Support both simple and complex menu designs

## Components

The menu package includes the following key classes:

1. **Menu** - An interface that defines the contract for creating interactive menus
2. **SimpleMenu** - A convenient implementation of the Menu interface that handles most of the boilerplate code

## How It Works

The menu system works as follows:

1. You create a class that extends SimpleMenu or implements the Menu interface
2. You define the items that should appear in the menu and their positions
3. You specify the actions that should occur when each item is clicked
4. The InventoryClickListener (from the listener package) automatically handles click events
5. When a player clicks an item, the corresponding action is executed

## Usage Example

Here's an example of how to create and use a custom menu:

```java
import dev.nelmin.ndcore.menu.SimpleMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WarpMenu extends SimpleMenu {
    
    public WarpMenu() {
        super("Teleport Menu", Rows.THREE); // 3 rows (27 slots)
    }
    
    @Override
    public void onSetItems() {
        // Spawn warp
        setItem(10, new ItemBuilder(Material.BEACON)
            .name("&aSpawn")
            .lore("&7Click to teleport to spawn")
            .build(),
            player -> {
                player.teleport(player.getWorld().getSpawnLocation());
                player.sendMessage("Teleported to spawn!");
                player.closeInventory();
            }
        );
        
        // Mine warp
        setItem(12, new ItemBuilder(Material.IRON_PICKAXE)
            .name("&eMining World")
            .lore("&7Click to teleport to the mining world")
            .build(),
            player -> {
                World miningWorld = Bukkit.getWorld("mining");
                if (miningWorld != null) {
                    player.teleport(miningWorld.getSpawnLocation());
                    player.sendMessage("Teleported to mining world!");
                    player.closeInventory();
                }
            }
        );
        
        // Close button
        setItem(26, new ItemBuilder(Material.BARRIER)
            .name("&cClose Menu")
            .build(),
            Player::closeInventory
        );
    }
}

// To open your custom menu for a player:
public void openWarpMenu(Player player) {
    WarpMenu warpMenu = new WarpMenu();
    warpMenu.open(player);
}
```

## Advanced Menu Types

The menu system can be extended to create more complex menu types:

### Paginated Menus

For displaying large collections of items:

```java
public class PaginatedMenu extends SimpleMenu {
    private final List<ItemStack> items;
    private int page = 0;
    private final int itemsPerPage = 45; // 5 rows of items
    
    public PaginatedMenu(List<ItemStack> items) {
        super("Items - Page 1", Rows.SIX); // 6 rows
        this.items = items;
    }
    
    @Override
    public void onSetItems() {
        // Clear previous items
        for (int i = 0; i < 54; i++) {
            inventory.clear(i);
        }
        
        // Calculate start and end indices
        int start = page * itemsPerPage;
        int end = Math.min(start + itemsPerPage, items.size());
        
        // Set items for current page
        int slot = 0;
        for (int i = start; i < end; i++) {
            setItem(slot++, items.get(i));
        }
        
        // Navigation buttons
        if (page > 0) {
            setItem(45, new ItemBuilder(Material.ARROW)
                .name("&aPrevious Page")
                .build(),
                player -> {
                    page--;
                    onSetItems();
                }
            );
        }
        
        if (end < items.size()) {
            setItem(53, new ItemBuilder(Material.ARROW)
                .name("&aNext Page")
                .build(),
                player -> {
                    page++;
                    onSetItems();
                }
            );
        }
    }
}
```

## Integration with Listener Package

The menu package works in conjunction with the listener package:

1. The menu package provides the interfaces and classes for creating menus
2. The listener package provides the InventoryClickListener that handles click events
3. The listener automatically detects when a player clicks in a menu and calls the appropriate action

This separation of concerns allows for a clean, modular design.

## Best Practices

- Extend SimpleMenu for most menu needs to reduce boilerplate code
- Override onSetItems() to set up your menu items
- Use ItemBuilder to create visually consistent menu items
- Keep click handlers simple and focused
- Consider using constants for slot positions to improve readability
- Close inventories when appropriate to prevent player confusion
- For complex menus, create a dedicated class that extends SimpleMenu
- Use the Rows enum for clear inventory size specification