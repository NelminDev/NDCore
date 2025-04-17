# Listener Package - Introduction

## Overview

The `listener` package in NDCore provides event listeners that handle various Bukkit events. These listeners are automatically registered by NDCore and provide core functionality for features like custom menus.

## Purpose

The primary purpose of the listener package is to:

- Provide pre-configured event handlers for common plugin needs
- Handle events related to NDCore's features (like menus)
- Reduce boilerplate code in plugins that use NDCore
- Ensure consistent behavior across plugins using NDCore

## Components

The listener package includes the following key classes:

1. **InventoryClickListener** - Handles inventory click events for custom menus created with the Menu system

## How It Works

The InventoryClickListener is automatically registered by NDCore when the plugin is enabled. It listens for InventoryClickEvent and checks if the inventory holder is an instance of the Menu interface. If it is, the listener:

1. Cancels the vanilla click event to prevent item movement
2. Delegates the click handling to the menu's `click()` method
3. Passes the player and slot information to the menu

This allows custom menus to handle clicks without each plugin having to implement its own click listener.

## Usage Example

Since the listeners are automatically registered, you don't need to register them yourself. Instead, you just need to create menus that implement the Menu interface:

```java
import dev.nelmin.ndcore.menu.SimpleMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MyCustomMenu extends SimpleMenu {
    
    public MyCustomMenu() {
        super("My Custom Menu", Rows.THREE); // 3 rows of inventory space
    }
    
    @Override
    public void onSetItems() {
        // Set up your menu items
        setItem(0, new ItemBuilder(Material.DIAMOND)
            .name("&bInformation")
            .lore("&7Click for info")
            .build(),
            player -> player.sendMessage("This is information!")
        );
            
        setItem(13, new ItemBuilder(Material.ENDER_PEARL)
            .name("&aTeleport to Spawn")
            .lore("&7Click to teleport")
            .build(),
            player -> {
                player.teleport(player.getWorld().getSpawnLocation());
                player.sendMessage("Teleported to spawn!");
            }
        );
            
        setItem(26, new ItemBuilder(Material.BARRIER)
            .name("&cClose")
            .lore("&7Click to close menu")
            .build(),
            Player::closeInventory
        );
    }
}

// To open your custom menu for a player:
public void openMenu(Player player) {
    MyCustomMenu menu = new MyCustomMenu();
    menu.open(player);
}
```

The InventoryClickListener will automatically handle clicks in this menu and call the appropriate action based on which slot was clicked.

## Integration with Menu System

The listener package works in conjunction with the menu package to provide a complete custom inventory solution:

1. The menu package provides the interfaces and classes for creating menus
2. The listener package provides the event handling for those menus

This separation of concerns allows for a clean, modular design.

## Best Practices

- Use the SimpleMenu class for most menu needs
- Let the InventoryClickListener handle click events automatically
- Focus on defining the menu structure and click actions
- Keep click handlers simple and focused
- Consider using constants for slot positions to improve readability
- Close inventories when appropriate to prevent player confusion