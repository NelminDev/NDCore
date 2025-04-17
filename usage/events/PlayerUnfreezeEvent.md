# PlayerUnfreezeEvent

## Overview

An event that is triggered when a player is unfrozen (allowed to move again).

## Key Features

- Extends PlayerEvent
- Implements Cancellable
- Can be fired asynchronously

## Usage Examples

```java
// Registering a listener for PlayerUnfreezeEvent
@EventHandler
public void onPlayerUnfreeze(PlayerUnfreezeEvent event) {
    Player player = event.getPlayer();
    
    // Check if the event is cancelled
    if (event.isCancelled()) {
        return;
    }
    
    // Cancel the event under certain conditions
    if (player.hasPermission("myserver.admin.freeze") && adminFrozenPlayers.contains(player.getUniqueId())) {
        event.setCancelled(true);
        player.sendMessage("You cannot be unfrozen by regular means!");
        return;
    }
    
    // Notify the player
    player.sendMessage("You have been unfrozen!");
    
    // Apply additional effects
    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_PLACE, 1.0f, 1.0f);
}

// Firing a PlayerUnfreezeEvent
public void unfreezePlayer(Player player) {
    PlayerUnfreezeEvent event = new PlayerUnfreezeEvent(player);
    Bukkit.getPluginManager().callEvent(event);
    
    if (!event.isCancelled()) {
        // Apply unfreeze logic here
        // For example, remove the player from a frozen players list
        frozenPlayers.remove(player.getUniqueId());
    }
}
```

## Best Practices

- Always check if the event is cancelled before proceeding with your logic
- Use event priorities appropriately to ensure your handler runs at the right time
- Consider using async events for operations that don't modify the game state
- Provide clear feedback to players when their state changes
- Implement permission checks to allow certain players to bypass freezing