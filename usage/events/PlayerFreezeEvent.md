# PlayerFreezeEvent

## Overview

An event that is triggered when a player is frozen (prevented from moving).

## Key Features

- Extends PlayerEvent
- Implements Cancellable
- Can be fired asynchronously

## Usage Examples

```java
// Registering a listener for PlayerFreezeEvent
@EventHandler
public void onPlayerFreeze(PlayerFreezeEvent event) {
    Player player = event.getPlayer();
    
    // Check if the event is cancelled
    if (event.isCancelled()) {
        return;
    }
    
    // Cancel the event if the player has a permission
    if (player.hasPermission("myserver.freeze.bypass")) {
        event.setCancelled(true);
        return;
    }
    
    // Notify the player
    player.sendMessage("You have been frozen!");
    
    // Apply additional effects
    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
}

// Firing a PlayerFreezeEvent
public void freezePlayer(Player player) {
    PlayerFreezeEvent event = new PlayerFreezeEvent(player);
    Bukkit.getPluginManager().callEvent(event);
    
    if (!event.isCancelled()) {
        // Apply freeze logic here
        // For example, store the player in a frozen players list
        frozenPlayers.add(player.getUniqueId());
    }
}
```

## Best Practices

- Always check if the event is cancelled before proceeding with your logic
- Use event priorities appropriately to ensure your handler runs at the right time
- Consider using async events for operations that don't modify the game state
- Provide clear feedback to players when their state changes
- Implement permission checks to allow certain players to bypass freezing