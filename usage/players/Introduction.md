# Players Package - Introduction

## Overview

The `players` package in NDCore provides enhanced player management capabilities, extending Bukkit's Player class with additional functionality like persistent property management and localized messaging.

## Purpose

The primary purpose of the players package is to:

- Provide a wrapper for Bukkit's Player class with additional functionality
- Manage player-specific persistent data
- Handle player language preferences
- Simplify sending localized messages to players
- Support player state management (like freezing/unfreezing)
- Integrate with NDCore's persistence system

## Components

The players package includes the following key classes:

1. **BasicNDPlayer** - A wrapper class for Bukkit's Player that adds persistent property management and localized messaging capabilities

## How It Works

The player system works as follows:

1. You create a BasicNDPlayer instance for each Bukkit Player
2. The BasicNDPlayer provides access to persistent properties for that player
3. It manages the player's language preference
4. It provides methods for sending localized messages
5. It handles player state like freezing/unfreezing

## Usage Example

Here's an example of how to use the player system:

```java
import dev.nelmin.ndcore.players.BasicNDPlayer;
import dev.nelmin.ndcore.objects.LanguageCode;
import dev.nelmin.ndcore.objects.LocalizedMessage;

// Creating a BasicNDPlayer instance
public BasicNDPlayer getOrCreatePlayer(Player player) {
    return BasicNDPlayer.of(player, this); // 'this' refers to your NDPlugin instance
}

// Using a BasicNDPlayer to send localized messages
public void sendWelcomeMessage(Player player) {
    BasicNDPlayer ndPlayer = getOrCreatePlayer(player);
    
    // Create a localized message
    LocalizedMessage welcomeMessage = new LocalizedMessage()
        .add(LanguageCode.ENGLISH, "Welcome to the server!")
        .add(LanguageCode.GERMAN, "Willkommen auf dem Server!")
        .add(LanguageCode.FRENCH, "Bienvenue sur le serveur!")
        .add(LanguageCode.SPANISH, "¡Bienvenido al servidor!");
    
    // Send the message in the player's preferred language
    ndPlayer.sendLocalizedMessage(welcomeMessage, e -> {
        getLogger().severe("Error sending localized message: " + e.getMessage());
    });
}

// Changing a player's language preference
public void setPlayerLanguage(Player player, String languageCode) {
    BasicNDPlayer ndPlayer = getOrCreatePlayer(player);
    
    try {
        // Convert string to LanguageCode enum
        LanguageCode language = LanguageCode.valueOf(languageCode.toUpperCase());
        
        // Update the player's language preference
        ndPlayer.getLanguageCode().set(language.get(), () -> {
            player.sendMessage("Language set to: " + language.name());
        }, e -> {
            getLogger().severe("Error setting player language: " + e.getMessage());
        });
    } catch (IllegalArgumentException e) {
        player.sendMessage("Invalid language code: " + languageCode);
    }
}

// Freezing and unfreezing a player
public void freezePlayer(Player player, boolean freeze) {
    BasicNDPlayer ndPlayer = getOrCreatePlayer(player);
    
    ndPlayer.getIsFrozen().set(freeze, () -> {
        if (freeze) {
            player.sendMessage("You have been frozen!");
        } else {
            player.sendMessage("You have been unfrozen!");
        }
    }, e -> {
        getLogger().severe("Error " + (freeze ? "freezing" : "unfreezing") + " player: " + e.getMessage());
    });
}
```

## Managing Player Collections

When working with multiple players, you'll typically want to maintain a collection of BasicNDPlayer instances:

```java
public class PlayerManager {
    private final Map<UUID, BasicNDPlayer> players = new HashMap<>();
    private final NDPlugin plugin;
    
    public PlayerManager(NDPlugin plugin) {
        this.plugin = plugin;
        
        // Register event listeners for player join/quit
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), plugin);
    }
    
    public BasicNDPlayer getPlayer(Player player) {
        return players.computeIfAbsent(
            player.getUniqueId(), 
            uuid -> BasicNDPlayer.of(player, plugin)
        );
    }
    
    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }
    
    public Collection<BasicNDPlayer> getAllPlayers() {
        return Collections.unmodifiableCollection(players.values());
    }
    
    // Example inner class for handling player events
    private class PlayerListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            BasicNDPlayer ndPlayer = getPlayer(player);
            
            // Send welcome message
            LocalizedMessage welcomeMessage = new LocalizedMessage()
                .add(LanguageCode.ENGLISH, "Welcome to the server!")
                .add(LanguageCode.GERMAN, "Willkommen auf dem Server!");
                
            ndPlayer.sendLocalizedMessage(welcomeMessage, e -> {
                plugin.getLogger().severe("Error: " + e.getMessage());
            });
        }
        
        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            removePlayer(event.getPlayer().getUniqueId());
        }
    }
}
```

## Integration with NDPlugin

The player system integrates with NDPlugin:

```java
public class MyPlugin extends NDPlugin {
    private final Map<UUID, BasicNDPlayer> players = new HashMap<>();
    
    @Override
    public void enable() {
        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }
    
    @Override
    public void disable() {
        // Clear player data
        players.clear();
    }
    
    private class PlayerListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            BasicNDPlayer ndPlayer = BasicNDPlayer.of(player, MyPlugin.this);
            players.put(player.getUniqueId(), ndPlayer);
            
            // Welcome the player
            ndPlayer.sendMessage("Welcome to the server!");
        }
        
        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            players.remove(event.getPlayer().getUniqueId());
        }
    }
}
```

## Best Practices

- Create BasicNDPlayer instances on player join and store them in a map
- Remove BasicNDPlayer instances on player quit to prevent memory leaks
- Use the player's language preference for all messages
- Handle errors in the error handler consumer
- Consider caching frequently accessed player data
- Use the freeze/unfreeze functionality with appropriate events
- Implement custom player properties by extending BasicNDPlayer
- Ensure thread safety when accessing player collections from async tasks