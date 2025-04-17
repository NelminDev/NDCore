# PersistentProperty

## Overview

A class that manages individual persistent data properties with type safety and default values in NDCore.

## Key Features

- Type-safe get and set operations
- Default value fallback
- Asynchronous data operations
- Error handling support
- Custom value merging functions

## Usage Examples

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

## Best Practices

- Always provide sensible default values for properties
- Handle errors in the error handler consumer
- Use custom merge functions for properties that need to combine values
- Consider the performance impact of frequent property access
- Use asynchronous operations for non-critical property updates
- Cache frequently accessed property values when appropriate
