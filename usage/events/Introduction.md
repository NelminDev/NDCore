# Events Package - Introduction

## Overview

The `events` package in NDCore provides custom events that extend Bukkit's event system. These events allow plugins to respond to specific actions or state changes related to player interactions that aren't covered by the standard Bukkit events.

## Purpose

The primary purpose of the events package is to:

- Provide a standardized way to handle specialized game events
- Allow plugins to communicate and interact through a well-defined event system
- Enable modular code that can respond to specific game situations
- Extend Bukkit's event system with additional functionality

## Components

The events package includes the following key classes:

1. **PlayerFreezeEvent** - Triggered when a player is frozen (prevented from moving)
2. **PlayerUnfreezeEvent** - Triggered when a player is unfrozen (allowed to move again)

Both events extend PlayerEvent and implement Cancellable, allowing other plugins to intercept and potentially cancel these actions.

## Usage Example

Here's an example of how to listen for and handle these custom events:

```java
import dev.nelmin.ndcore.events.PlayerFreezeEvent;
import dev.nelmin.ndcore.events.PlayerUnfreezeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FreezeEventListener implements Listener {
    
    @EventHandler
    public void onPlayerFreeze(PlayerFreezeEvent event) {
        // Check if the event is cancelled
        if (event.isCancelled()) {
            return;
        }
        
        // Get the player
        Player player = event.getPlayer();
        
        // Notify the player
        player.sendMessage("You have been frozen!");
        
        // Apply additional effects
        player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
    }
    
    @EventHandler
    public void onPlayerUnfreeze(PlayerUnfreezeEvent event) {
        // Check if the event is cancelled
        if (event.isCancelled()) {
            return;
        }
        
        // Get the player
        Player player = event.getPlayer();
        
        // Notify the player
        player.sendMessage("You have been unfrozen!");
        
        // Apply additional effects
        player.playSound(player.getLocation(), Sound.BLOCK_GLASS_PLACE, 1.0f, 1.0f);
    }
}
```

## Firing Custom Events

To trigger these events from your code:

```java
// Freeze a player
public void freezePlayer(Player player) {
    PlayerFreezeEvent event = new PlayerFreezeEvent(player);
    Bukkit.getPluginManager().callEvent(event);
    
    if (!event.isCancelled()) {
        // Apply freeze logic here
        // For example, store the player in a frozen players list
        frozenPlayers.add(player.getUniqueId());
    }
}

// Unfreeze a player
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

- Always check if events are cancelled before proceeding with your logic
- Use event priorities appropriately to ensure your handler runs at the right time
- Consider using async events for operations that don't modify the game state
- Provide clear feedback to players when their state changes
- Implement permission checks to allow certain players to bypass restrictions