# Persistence Usage Guide

## Overview

The Persistence package provides utilities for managing persistent data in Bukkit/Paper plugins. It offers a type-safe and convenient way to store and retrieve data that survives server restarts, using Bukkit's PersistentDataContainer API.

## Components

### PersistentPropertyManager

A class that manages persistent properties for players, handling creation and validation of persistent properties.

#### Key Features

- Type-safe property creation
- Default value support
- Custom value merging behavior
- Automatic validation of property names and values
- Integration with NDPlugin system

#### Usage Examples

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

### PersistentProperty

A class that manages individual persistent data properties with type safety and default values.

#### Key Features

- Type-safe get and set operations
- Default value fallback
- Asynchronous data operations
- Error handling support
- Custom value merging functions

#### Usage Examples

```java
// Getting a property value
public int getPlayerLevel(Player player) {
    PersistentPropertyManager propertyManager = PersistentPropertyManager.of(player, this);
    PersistentProperty<Integer> levelProperty = propertyManager.create("player.level", 1);
    
    return levelProperty.get(e -> {
        getLogger().severe("Error getting player level: " + e.getMessage());
    });
}

// Setting a property value
public void setPlayerLevel(Player player, int level) {
    PersistentPropertyManager propertyManager = PersistentPropertyManager.of(player, this);
    PersistentProperty<Integer> levelProperty = propertyManager.create("player.level", 1);
    
    levelProperty.set(level, () -> {
        // This runs after the value is set
        player.sendMessage("Your level has been updated to " + level);
    }, e -> {
        getLogger().severe("Error setting player level: " + e.getMessage());
        player.sendMessage("There was an error updating your level");
    });
}

// Using custom merge behavior
public void addPlayerCoins(Player player, int amount) {
    PersistentPropertyManager propertyManager = PersistentPropertyManager.of(player, this);
    PersistentProperty<Integer> coinsProperty = propertyManager.create(
        "player.coins",
        0,
        (newCoins, currentCoins) -> (currentCoins != null ? currentCoins : 0) + newCoins
    );
    
    // This will add to the current value rather than replacing it
    coinsProperty.set(amount, () -> {
        int totalCoins = coinsProperty.get(e -> getLogger().severe("Error: " + e.getMessage()));
        player.sendMessage("You now have " + totalCoins + " coins");
    }, e -> {
        getLogger().severe("Error adding player coins: " + e.getMessage());
    });
}

// Removing a property
public void resetPlayerRank(Player player) {
    PersistentPropertyManager propertyManager = PersistentPropertyManager.of(player, this);
    PersistentProperty<String> rankProperty = propertyManager.create("player.rank", "Novice");
    
    rankProperty.remove(e -> {
        getLogger().severe("Error removing player rank: " + e.getMessage());
    });
    
    player.sendMessage("Your rank has been reset");
}
```

### PersistentDataTypeHelper

A helper class for managing PersistentDataType conversions using the Singleton pattern.

#### Key Features

- Automatic conversion between primitive and complex types
- Singleton pattern for efficient reuse
- Support for all standard Bukkit PersistentDataTypes

#### Usage Examples

```java
// Getting the appropriate PersistentDataType for a class
public <T> PersistentDataType<?, T> getDataType(Class<T> clazz) {
    return PersistentDataTypeHelper.instance().type(clazz);
}

// Using the helper directly with a PersistentDataContainer
public void storeCustomData(PersistentDataContainer container, String key, Object value) {
    NamespacedKey namespacedKey = new NamespacedKey(this, key);
    
    if (value instanceof Integer intValue) {
        PersistentDataType<?, Integer> type = PersistentDataTypeHelper.instance().type(Integer.class);
        container.set(namespacedKey, type, intValue);
    } else if (value instanceof String stringValue) {
        container.set(namespacedKey, PersistentDataType.STRING, stringValue);
    } else if (value instanceof Boolean boolValue) {
        PersistentDataType<?, Boolean> type = PersistentDataTypeHelper.instance().type(Boolean.class);
        container.set(namespacedKey, type, boolValue);
    }
    // Add more type handling as needed
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
- Handle errors in the error handler consumer
- Use custom merge functions for properties that need to combine values
- Consider the performance impact of frequent property access
- Use asynchronous operations for non-critical property updates
- Group related properties under a common prefix
- Cache frequently accessed property values when appropriate
- Use the type-safe methods rather than direct PersistentDataContainer access