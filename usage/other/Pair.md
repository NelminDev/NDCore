# Pair

## Overview

A record that holds two related values of different types in NDCore.

## Key Features

- Type-safe container for two related values
- Immutable data structure
- Null-safe value handling
- Compatible with Java's pattern matching

## Usage Examples

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

## Best Practices

- Use Pair for simple data relationships that don't warrant a dedicated class
- Use descriptive variable names to clarify what the components of a Pair represent
- Consider using Pair for method returns that need to provide both a result and status/message
- Remember that Pair is immutable - create new instances if you need to change values
- For more than two related values, consider using Triplet or creating a dedicated class