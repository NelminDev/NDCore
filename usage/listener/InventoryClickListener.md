# InventoryClickListener

## Overview

A listener that handles inventory click events for custom menus created with the Menu system.

## Key Features

- Implements Bukkit's Listener interface
- Automatically cancels click events in custom menus
- Delegates click handling to the appropriate menu implementation

## Usage Examples

```java
// This listener is automatically registered by NDCore
// You don't need to register it manually in your plugin

// To create a custom menu that works with this listener:
public class MyCustomMenu extends SimpleMenu {
    
    public MyCustomMenu() {
        super("My Custom Menu", Rows.THREE); // 3 rows of inventory space
    }
    
    @Override
    public void click(Player player, int slot) {
        // Handle clicks in your menu
        switch (slot) {
            case 0 -> player.sendMessage("You clicked the first slot!");
            case 13 -> {
                // Middle slot - teleport the player
                player.teleport(player.getWorld().getSpawnLocation());
                player.sendMessage("Teleported to spawn!");
            }
            case 26 -> {
                // Last slot - close the menu
                player.closeInventory();
            }
        }
    }
    
    // Set up your menu items
    @Override
    public void setupItems() {
        inventory.setItem(0, new ItemBuilder(Material.DIAMOND)
            .name("&bInformation")
            .lore("&7Click for info")
            .build());
            
        inventory.setItem(13, new ItemBuilder(Material.ENDER_PEARL)
            .name("&aTeleport to Spawn")
            .lore("&7Click to teleport")
            .build());
            
        inventory.setItem(26, new ItemBuilder(Material.BARRIER)
            .name("&cClose")
            .lore("&7Click to close menu")
            .build());
    }
}

// To open your custom menu for a player:
public void openMenu(Player player) {
    MyCustomMenu menu = new MyCustomMenu();
    menu.setupItems(); // Don't forget to set up the items
    menu.open(player);
}
```

## Integration with Menu System

The InventoryClickListener works in conjunction with the Menu system to provide a complete custom inventory solution:

1. Create a custom menu by extending `SimpleMenu` or implementing the `Menu` interface
2. Override the `click(Player, int)` method to handle clicks
3. Implement `setupItems()` to populate your menu with items
4. Use `menu.open(player)` to display the menu to a player

The InventoryClickListener will automatically:
- Detect clicks in your custom menu
- Cancel the vanilla click event to prevent item movement
- Call your menu's `click()` method with the player and slot information

## Best Practices

- Keep menu click handlers simple and focused
- Use switch expressions (Java 21+) for clean slot handling
- Consider using constants for slot positions to improve readability
- Provide clear feedback to players when they interact with menu items
- Close inventories when appropriate to prevent player confusion
- Use the ItemBuilder to create consistent and visually appealing menu items