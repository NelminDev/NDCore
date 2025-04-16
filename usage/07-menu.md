# Menu Usage Guide

## Overview

The Menu package provides a flexible system for creating interactive inventories (GUIs) in Minecraft. It simplifies the process of creating menus with clickable items and handling player interactions.

## Components

### Menu Interface

The core interface that defines the contract for creating interactive menus.

#### Key Features

- Extends Bukkit's InventoryHolder
- Provides click handling functionality
- Supports setting items with or without click actions
- Includes a default open method for displaying the menu to players

#### Usage Examples

```java
// Implementing a custom menu
public class ShopMenu implements Menu {
    private final Inventory inventory;
    private final Map<Integer, Consumer<Player>> actions = new HashMap<>();
    private final Economy economy;
    
    public ShopMenu(Economy economy) {
        this.economy = economy;
        this.inventory = Bukkit.createInventory(this, 27, Component.text("Shop Menu"));
    }
    
    @Override
    public void click(@NotNull Player player, int slot) {
        Consumer<Player> action = actions.get(slot);
        if (action != null) {
            action.accept(player);
        }
    }
    
    @Override
    public void setItem(int slot, @NotNull ItemStack item) {
        inventory.setItem(slot, item);
    }
    
    @Override
    public void setItem(int slot, @NotNull ItemStack item, @NotNull Consumer<Player> action) {
        inventory.setItem(slot, item);
        actions.put(slot, action);
    }
    
    @Override
    public void onSetItems() {
        // Set up shop items
        setItem(10, new ItemBuilder(Material.DIAMOND_SWORD)
            .name("&bDiamond Sword")
            .lore("&7Price: &a$1000")
            .build(), 
            player -> {
                if (economy.has(player, 1000)) {
                    economy.withdrawPlayer(player, 1000);
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
                    player.sendMessage("You purchased a Diamond Sword!");
                } else {
                    player.sendMessage("You don't have enough money!");
                }
            }
        );
        
        // Add more items...
    }
    
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
```

### SimpleMenu

A convenient implementation of the Menu interface that handles most of the boilerplate code.

#### Key Features

- Pre-implemented click handling
- Built-in action mapping
- Enum-based inventory size selection
- Simplified item setting

#### Usage Examples

```java
// Creating a basic menu with SimpleMenu
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
        
        // PvP arena warp
        setItem(14, new ItemBuilder(Material.IRON_SWORD)
            .name("&cPvP Arena")
            .lore("&7Click to teleport to the PvP arena")
            .build(),
            player -> {
                Location pvpArena = new Location(Bukkit.getWorld("world"), 100, 64, 100);
                player.teleport(pvpArena);
                player.sendMessage("Teleported to PvP arena!");
                player.closeInventory();
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

// Using the menu in your plugin
public void openWarpMenu(Player player) {
    WarpMenu warpMenu = new WarpMenu();
    warpMenu.open(player);
}
```

### Creating Paginated Menus

For larger collections of items, you can create paginated menus:

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
                    // Update title
                    // Note: In Paper, you'd need to recreate the inventory to change the title
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
                    // Update title
                    onSetItems();
                }
            );
        }
        
        // Page indicator
        setItem(49, new ItemBuilder(Material.PAPER)
            .name("&ePage " + (page + 1) + "/" + ((items.size() - 1) / itemsPerPage + 1))
            .build()
        );
    }
}
```

## Integration with InventoryClickListener

The Menu system works in conjunction with the [InventoryClickListener](04-listener.md) to handle click events. The listener automatically:

1. Detects when a player clicks in a menu inventory
2. Cancels the vanilla click event to prevent item movement
3. Calls the menu's `click()` method with the player and slot information

## Best Practices

- Extend SimpleMenu for most use cases to reduce boilerplate code
- Override onSetItems() to set up your menu items
- Use ItemBuilder to create visually consistent menu items
- Keep click handlers simple and focused
- Consider using constants for slot positions to improve readability
- Close inventories when appropriate to prevent player confusion
- For complex menus, create a dedicated class that extends SimpleMenu
- Use the Rows enum for clear inventory size specification