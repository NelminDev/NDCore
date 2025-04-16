# Events Usage Guide

## Overview

The Events package provides custom Bukkit events for player state changes in NDCore. These events allow developers to respond to specific player actions or state changes within their plugins.

## Components

### PlayerFreezeEvent

An event that is triggered when a player is frozen (prevented from moving).

#### Key Features

- Extends PlayerEvent
- Implements Cancellable
- Can be fired asynchronously

#### Usage Examples

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

### PlayerUnfreezeEvent

An event that is triggered when a player is unfrozen (allowed to move again).

#### Key Features

- Extends PlayerEvent
- Implements Cancellable
- Can be fired asynchronously

#### Usage Examples

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