# PersistentPropertyManager

## Overview

A class that manages persistent properties for players, handling creation and validation of persistent properties in NDCore.

## Key Features

- Type-safe property creation
- Default value support
- Custom value merging behavior
- Automatic validation of property names and values
- Integration with NDPlugin system

## Usage Examples

```java
// Getting a property manager for a player
public void setupPlayerData(Player player) {
    PersistentPropertyManager propertyManager = PersistentPropertyManager.of(player, this);
    
    // Create properties for the player
    setupPlayerProperties(propertyManager);
}

// Creating properties with default values
private void setupPlayerProperties(PersistentPropertyManager propertyManager) {
    // Create a simple integer property with default value
    PersistentProperty<Integer> levelProperty = propertyManager.create(
        "player.level", 
        1
    );
    
    // Create a string property with default value
    PersistentProperty<String> rankProperty = propertyManager.create(
        "player.rank", 
        "Novice"
    );
    
    // Create a boolean property with custom merge behavior
    PersistentProperty<Boolean> vipProperty = propertyManager.create(
        "player.vip",
        false,
        (newValue, currentValue) -> newValue || (currentValue != null && currentValue)
    );
    
    // Create a long property for timestamps
    PersistentProperty<Long> lastLoginProperty = propertyManager.create(
        "player.last_login",
        System.currentTimeMillis()
    );
}
```

## Integration with NDPlugin

The persistence system is designed to work seamlessly with the NDPlugin system:

```java
public class MyPlugin extends NDPlugin {
    private final Map<UUID, PersistentPropertyManager> propertyManagers = new HashMap<>();
    
    @Override
    public void enable() {
        // Plugin initialization
    }
    
    @Override
    public void disable() {
        // Plugin shutdown
    }
    
    @Override
    public @NotNull Map<UUID, PersistentPropertyManager> getPlayerPropertyManagers() {
        return propertyManagers;
    }
    
    // Example event handler that uses persistence
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PersistentPropertyManager propertyManager = PersistentPropertyManager.of(player, this);
        
        // Create or get a login count property
        PersistentProperty<Integer> loginCountProperty = propertyManager.create(
            "player.login_count",
            0,
            (newValue, currentValue) -> (currentValue != null ? currentValue : 0) + 1
        );
        
        // Increment login count
        loginCountProperty.set(1, () -> {
            int count = loginCountProperty.get(e -> logger().error("Error getting login count", e));
            player.sendMessage("Welcome back! This is your " + count + " login.");
        }, e -> {
            logger().error("Error updating login count", e);
        });
    }
}
```

## Best Practices

- Use descriptive, namespaced key names for properties (e.g., "player.stats.level")
- Always provide sensible default values for properties
- Use custom merge functions for properties that need to combine values
- Group related properties under a common prefix
- Cache frequently accessed property values when appropriate
- Use the type-safe methods rather than direct PersistentDataContainer access