# Persistence Package - Introduction

## Overview

The `persistence` package in NDCore provides a robust system for managing persistent data with type safety and default values. It leverages Bukkit's PersistentDataContainer API to store and retrieve data that persists across server restarts.

## Purpose

The primary purpose of the persistence package is to:

- Provide a type-safe way to store and retrieve persistent data
- Simplify working with Bukkit's PersistentDataContainer API
- Support default values for properties that haven't been set
- Enable asynchronous data operations
- Provide error handling for data operations
- Allow for custom value merging behavior

## Components

The persistence package includes the following key classes:

1. **PersistentDataTypeHelper** - A helper class for managing PersistentDataType conversions using the Singleton pattern
2. **PersistentProperty** - A class that manages individual persistent data properties with type safety and default values
3. **PersistentPropertyManager** - A class that manages persistent properties for players, handling creation and validation

## How It Works

The persistence system works as follows:

1. The PersistentPropertyManager creates and manages PersistentProperty instances
2. Each PersistentProperty represents a single piece of data (like a player's level or settings)
3. The PersistentDataTypeHelper handles the conversion between Java types and PersistentDataTypes
4. Data is stored in the PersistentDataContainer of entities (usually players)
5. The system provides type safety, default values, and error handling

## Usage Example

Here's an example of how to use the persistence system:

```java
import dev.nelmin.ndcore.persistence.PersistentProperty;
import dev.nelmin.ndcore.persistence.PersistentPropertyManager;

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
}

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
        loginCountProperty.set(1);
    }
}
```

## Best Practices

- Use descriptive, namespaced key names for properties (e.g., "player.stats.level")
- Always provide sensible default values for properties
- Use custom merge functions for properties that need to combine values
- Handle errors in the error handler consumer
- Group related properties under a common prefix
- Cache frequently accessed property values when appropriate
- Consider the performance impact of frequent property access
- Use asynchronous operations for non-critical property updates
