# Triplet

## Overview

A record that holds three related values of different types in NDCore.

## Key Features

- Type-safe container for three related values
- Immutable data structure
- Null-safe value handling
- Compatible with Java's pattern matching

## Usage Examples

```java
// Creating triplets with different types
Triplet<String, Integer, Boolean> playerData = new Triplet<>("Steve", 100, true);
Triplet<UUID, Location, GameMode> playerState = new Triplet<>(
    player.getUniqueId(),
    player.getLocation(),
    player.getGameMode()
);

// Accessing values
String playerName = playerData.first();
int health = playerData.second();
boolean isOnline = playerData.third();

// Using in method returns
public Triplet<Boolean, String, Object> processCommand(String command, String[] args) {
    if (!hasPermission(sender, command)) {
        return new Triplet<>(false, "No permission", null);
    }
    
    try {
        Object result = executeCommand(command, args);
        return new Triplet<>(true, "Command executed successfully", result);
    } catch (Exception e) {
        return new Triplet<>(false, "Error: " + e.getMessage(), null);
    }
}

// Using the command processor
Triplet<Boolean, String, Object> result = processCommand("teleport", args);
if (result.first()) {
    player.sendMessage(result.second());
    // Use the result object if needed
    Object commandResult = result.third();
    if (commandResult instanceof Location) {
        // Do something with the location
    }
} else {
    player.sendMessage(result.second());
}

// Storing complex data
Map<UUID, Triplet<String, Integer, List<String>>> playerStats = new HashMap<>();
playerStats.put(
    player.getUniqueId(),
    new Triplet<>(
        player.getName(),
        playerPoints,
        playerAchievements
    )
);

// Retrieving and using the triplet
Triplet<String, Integer, List<String>> stats = playerStats.get(player.getUniqueId());
if (stats != null) {
    player.sendMessage("Name: " + stats.first());
    player.sendMessage("Points: " + stats.second());
    player.sendMessage("Achievements: " + String.join(", ", stats.third()));
}
```

## Best Practices

- Use Triplet when you need to group three related values that don't warrant a dedicated class
- Use descriptive variable names to clarify what the components of a Triplet represent
- Consider using Triplet for method returns that need to provide multiple pieces of information
- Remember that Triplet is immutable - create new instances if you need to change values
- For more than three related values, consider creating a dedicated class instead