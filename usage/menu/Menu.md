# Menu Interface

## Overview

The core interface that defines the contract for creating interactive menus in NDCore.

## Key Features

- Extends Bukkit's InventoryHolder
- Provides click handling functionality
- Supports setting items with or without click actions
- Includes a default open method for displaying the menu to players

## Usage Examples

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

## Integration with InventoryClickListener

The Menu system works in conjunction with the InventoryClickListener to handle click events. The listener automatically:

1. Detects when a player clicks in a menu inventory
2. Cancels the vanilla click event to prevent item movement
3. Calls the menu's `click()` method with the player and slot information

## Best Practices

- Implement the Menu interface for complete control over menu behavior
- Use SimpleMenu for most use cases to reduce boilerplate code
- Keep click handlers simple and focused
- Consider using constants for slot positions to improve readability
- Close inventories when appropriate to prevent player confusion