# NDPlugin Usage Guide

## Overview

The NDPlugin class is an abstract base class that extends JavaPlugin and provides additional functionality for plugins built on NDCore. It handles lifecycle management, enhanced logging, and player data handling.

## Key Features

- Enhanced lifecycle management (load, enable, disable)
- Automatic cleanup of offline player data
- Persistent property management
- Enhanced logging with NDLogger
- Player data management

## Components

### Lifecycle Methods

NDPlugin provides a structured lifecycle with clear methods to override:

```java
public class MyPlugin extends NDPlugin {
    @Override
    public void load() {
        // Called during the load phase
        // Use for early initialization that doesn't depend on other plugins
        // Optional, you don't have to overwrite it
        // equivalent to onLoad()
    }
    
    @Override
    public void enable() {
        // Called during the enable phase
        // This is where most of your initialization should happen
        // equivalent to onEnable()
        logger().info("MyPlugin has been enabled!");
        
        // Register commands
        getCommand("mycommand").setExecutor(new MyCommandExecutor());
        
        // Register event listeners
        getServer().getPluginManager().registerEvents(new MyEventListener(), this);
    }
    
    @Override
    public void disable() {
        // Called during the disable phase
        // Clean up resources here
        // equivalent to onDisable()
        logger().info("MyPlugin has been disabled!");
    }
}
```

### Enhanced Logging

NDPlugin provides an enhanced logging system through [NDLogger](05-logger.md):

```java
public class MyPlugin extends NDPlugin {
    @Override
    public void enable() {
        // Basic logging
        logger().info("Plugin enabled!");
        
        // Warning messages
        logger().warn("This is a warning message");
        
        // Error messages
        logger().error("This is an error message");
        
        // Fatal messages
        logger().fatal("This is a fatal error message");
        
        // Silent logging (logs to file but not console)
        logger().infoSilent("This message only goes to the log file");
    }
}
```

### Player Data Management

NDPlugin provides built-in player data management:

```java
public class MyPlugin extends NDPlugin {
    @Override
    public void enable() {
        // Register player join event to initialize player data
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }
    
    private class PlayerListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            
            // Get or create a property manager for the player
            PersistentPropertyManager propertyManager = getPlayerPropertyManagers()
                .computeIfAbsent(player.getUniqueId(), 
                    uuid -> new PersistentPropertyManager(
                        MyPlugin.this, 
                        player.getPersistentDataContainer()
                    )
                );
            
            // Create a BasicNDPlayer instance
            BasicNDPlayer ndPlayer = new BasicNDPlayer(player, propertyManager);
            
            // Add to the list of managed players
            getBasicNDPlayers().add(ndPlayer);
        }
        
        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            UUID playerUUID = event.getPlayer().getUniqueId();
            
            // Remove from the list of managed players
            getBasicNDPlayers().removeIf(p -> p.getUUID().equals(playerUUID));
            
            // Note: The property manager will be automatically cleaned up by NDPlugin
        }
    }
}
```

### Persistent Property Management

NDPlugin provides built-in persistent property management:

```java
PersistentProperty<Integer> intProperty = propertyManager.create(
        "NAME",
        DEFAULT_VALUE, // The default value for this property
        (prev, cur) -> {
            // Custom logic -> what should happen when someone changes this property via PersistentProperty#set
            // You have the previous (prev) and current (cur) value
            // You can do anything you want.
        }
);
```

## Complete Example

Here's a complete example of a plugin that extends NDPlugin:

```java
public class MyPlugin extends NDPlugin {
    private final Map<UUID, BasicNDPlayer> players = new HashMap<>();
    
    @Override
    public void load() {
        // Early initialization
        logger().info("Loading configuration...");
        saveDefaultConfig();
    }
    
    @Override
    public void enable() {
        // Main initialization
        logger().info("Initializing MyPlugin...");
        
        // Register commands
        getCommand("hello").setExecutor((sender, command, label, args) -> {
            sender.sendMessage("Hello from MyPlugin!");
            return true;
        });
        
        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        
        logger().info("MyPlugin has been enabled!");
    }
    
    @Override
    public void disable() {
        // Cleanup
        logger().info("Saving player data...");
        
        // Clear player data
        players.clear();
        
        logger().info("MyPlugin has been disabled!");
    }
    
    private class PlayerListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            
            // Create a BasicNDPlayer instance
            BasicNDPlayer ndPlayer = BasicNDPlayer.of(player, MyPlugin.this);
            players.put(player.getUniqueId(), ndPlayer);
            
            // Send welcome message
            LocalizedMessage welcomeMessage = new LocalizedMessage()
                .add(LanguageCode.ENGLISH, "Welcome to the server!")
                .add(LanguageCode.GERMAN, "Willkommen auf dem Server!");
                
            ndPlayer.sendLocalizedMessage(welcomeMessage, e -> {
                logger().error("Error sending welcome message", e);
            });
        }
        
        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            players.remove(event.getPlayer().getUniqueId());
        }
    }
}
```

## Relationship with NDCore

NDPlugin is the base class that NDCore extends. While NDCore is the concrete implementation that runs as a plugin, NDPlugin provides the framework that both NDCore and your custom plugins can use. This separation allows for:

1. **Consistent Plugin Structure**: All plugins built on NDCore follow the same lifecycle and structure
2. **Shared Functionality**: Common features like logging and player data management are available to all plugins
3. **Simplified Development**: You don't need to reimplement common functionality in each plugin

When you create a plugin that depends on NDCore, you'll typically extend NDPlugin rather than JavaPlugin directly, giving you access to all the enhanced features.

## Best Practices

- Always override the `enable()` and `disable()` methods, not `onEnable()` and `onDisable()`
- Use the enhanced `logger()` method instead of the deprecated `getLogger()`
- Take advantage of the automatic cleanup of offline player data
- Use the built-in persistent property management for player data
- Implement proper error handling in your plugins
- Follow the lifecycle methods for proper initialization and cleanup
- Use BasicNDPlayer for enhanced player functionality