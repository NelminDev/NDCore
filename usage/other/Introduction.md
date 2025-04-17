# Other Package - Introduction

## Overview

The `other` package in NDCore provides general utility classes that don't fit neatly into the other specialized packages. These utilities handle common tasks like date/time operations and storing related values together.

## Purpose

The primary purpose of the other package is to:

- Provide utility classes for common programming tasks
- Offer type-safe containers for related data
- Simplify date and time operations
- Reduce boilerplate code in plugins
- Provide reusable components that don't warrant their own package

## Components

The other package includes the following key classes:

1. **DateTime** - A utility class for handling date and time operations with formatting capabilities
2. **Pair** - A record that holds two related values of different types
3. **Triplet** - A record that holds three related values of different types

## Usage Examples

### DateTime

The DateTime class simplifies working with dates and times:

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

The Pair record holds two related values:

```java
// Creating pairs with different types
Pair<String, Integer> nameAndAge = new Pair<>("Steve", 30);
Pair<UUID, Location> playerLocation = new Pair<>(
    player.getUniqueId(),
    player.getLocation()
);

// Accessing values
String name = nameAndAge.first();
int age = nameAndAge.second();

// Using in method returns
public Pair<Boolean, String> processCommand(String command) {
    if (!hasPermission(sender, command)) {
        return new Pair<>(false, "No permission");
    }
    
    try {
        // Process command
        return new Pair<>(true, "Command executed successfully");
    } catch (Exception e) {
        return new Pair<>(false, "Error: " + e.getMessage());
    }
}

// Using the command processor
Pair<Boolean, String> result = processCommand("teleport");
if (result.first()) {
    player.sendMessage(result.second()); // Success message
} else {
    player.sendMessage(result.second()); // Error message
}
```

### Triplet

The Triplet record holds three related values:

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
```

## Best Practices

- Use DateTime for consistent date and time formatting across your plugin
- Use Pair and Triplet when you need to group related values that don't warrant a dedicated class
- Use descriptive variable names to clarify what the components of a Pair or Triplet represent
- Consider using Pair and Triplet for method returns that need to provide multiple pieces of information
- Remember that Pair and Triplet are immutable - create new instances if you need to change values
- For more than three related values, consider creating a dedicated class instead