# Other Utilities Usage Guide

## Overview

The Other package provides utility classes for common operations like date/time handling and data structures for holding related values. These utilities simplify common programming tasks and provide type-safe ways to work with related data.

## Components

### DateTime

A utility class for handling date and time operations with formatting capabilities.

#### Key Features

- Multiple constructors for different time sources
- Formatting methods for dates and times
- Custom pattern formatting
- Conversion between different date/time representations

#### Usage Examples

```java
// Creating DateTime instances
DateTime now = new DateTime(); // Current time
DateTime fromInstant = new DateTime(Instant.now());
DateTime fromLocalDateTime = new DateTime(LocalDateTime.now());
DateTime fromTimestamp = new DateTime(System.currentTimeMillis());

// Formatting dates and times
String timeString = now.formatTime(); // "14:30:45.123"
String dateString = now.formatDate(); // "25-04-2025"

// Getting both date and time
Pair<String, String> dateAndTime = now.dateTime();
String date = dateAndTime.first();
String time = dateAndTime.second();

// Custom format patterns
String customFormat = now.format("yyyy-MM-dd HH:mm:ss"); // "2025-04-25 14:30:45"

// Using with custom formatters
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
String longDate = now.format(formatter); // "Friday, April 25, 2025"

// Getting the LocalDateTime
LocalDateTime localDateTime = now.localDateTime();

// Using in logging
logger.info("Operation completed at " + new DateTime().formatTime());

// Calculating time differences
DateTime start = new DateTime();
// ... some operation
DateTime end = new DateTime();
long durationMs = end.getInstant().toEpochMilli() - start.getInstant().toEpochMilli();
logger.info("Operation took " + durationMs + "ms");
```

### Pair

A record that holds two related values of different types.

#### Key Features

- Type-safe container for two related values
- Immutable data structure
- Null-safe value handling
- Compatible with Java's pattern matching

#### Usage Examples

```java
// Creating pairs with different types
Pair<String, Integer> nameAndAge = new Pair<>("John", 30);
Pair<UUID, Player> idAndPlayer = new Pair<>(player.getUniqueId(), player);
Pair<Location, Double> locationAndDistance = new Pair<>(player.getLocation(), 10.5);

// Accessing values
String name = nameAndAge.first();
int age = nameAndAge.second();

// Using with nullable values
Pair<Player, String> playerAndReason = new Pair<>(null, "Player not found");
if (playerAndReason.first() == null) {
    System.out.println(playerAndReason.second());
}

// Using in method returns
public Pair<Boolean, String> validateUsername(String username) {
    if (username.length() < 3) {
        return new Pair<>(false, "Username too short");
    }
    if (username.length() > 16) {
        return new Pair<>(false, "Username too long");
    }
    if (!username.matches("[a-zA-Z0-9_]+")) {
        return new Pair<>(false, "Username contains invalid characters");
    }
    return new Pair<>(true, "Username is valid");
}

// Using the validation method
Pair<Boolean, String> result = validateUsername(input);
if (result.first()) {
    player.sendMessage("Validation successful: " + result.second());
} else {
    player.sendMessage("Validation failed: " + result.second());
}

// Using with collections
Map<String, Pair<Integer, String>> items = new HashMap<>();
items.put("diamond_sword", new Pair<>(10, "A powerful sword"));
items.put("golden_apple", new Pair<>(5, "Restores health"));

// Retrieving and using the pair
Pair<Integer, String> item = items.get("diamond_sword");
if (item != null) {
    player.sendMessage("Item: Diamond Sword");
    player.sendMessage("Price: " + item.first() + " coins");
    player.sendMessage("Description: " + item.second());
}
```

### Triplet

A record that holds three related values of different types.

#### Key Features

- Type-safe container for three related values
- Immutable data structure
- Null-safe value handling
- Compatible with Java's pattern matching

#### Usage Examples

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

- Use DateTime for consistent date and time formatting across your plugin
- Prefer Pair and Triplet over custom classes for simple data relationships
- Consider using Pair for method returns that need to provide both a result and status/message
- Use Triplet when you need to group three related values that don't warrant a dedicated class
- Remember that Pair and Triplet are immutable - create new instances if you need to change values
- Use descriptive variable names to clarify what the components of a Pair or Triplet represent
- For more than three related values, consider creating a dedicated class instead of nesting Pairs/Triplets