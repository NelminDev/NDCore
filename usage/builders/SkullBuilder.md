# SkullBuilder

## Overview

A specialized builder for creating player heads and custom skulls with a fluent interface. This class extends
ItemBuilder, inheriting all its methods and functionality.

## Key Features

- Player head creation by UUID, name, or OfflinePlayer
- Custom texture support via URL
- Display name and lore customization
- Inherits all ItemBuilder functionality
- Fluent API for method chaining

## Methods

| Method                       | Description                                                           |
|------------------------------|-----------------------------------------------------------------------|
| `SkullBuilder()`             | Constructor that creates a player head item                           |
| `owner(String owner)`        | Sets the skull's owner using a player name                            |
| `owner(OfflinePlayer owner)` | Sets the skull's owner using an OfflinePlayer                         |
| `owner(UUID uuid)`           | Sets the skull's owner using a UUID                                   |
| `texture(String texture)`    | Sets the skull's texture using a Base64 encoded value or a direct URL |
| `toItem()`                   | Builds and returns the final skull ItemStack                          |

*Note: SkullBuilder inherits all methods from ItemBuilder, including displayName(), lore(), etc.*

## Usage Examples

```java
import dev.nelmin.ndcore.builders.SkullBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.List;

// Create a player head
ItemStack playerHead = new SkullBuilder()
        .owner(player.getUniqueId())
        .displayName(Component.text("Player Head: " + player.getName()))
        .lore(List.of(Component.text("Click to view profile")))
        .toItem();

// Create a custom textured head using a direct URL
ItemStack customHead = new SkullBuilder()
        .texture("http://textures.minecraft.net/texture/e35a6dd86d87aab429d1ffd9ecb9f472b63d96e99b87d14c43e8823f91b97")
        .displayName(Component.text("Gold Coin"))
        .toItem();

// Create a custom textured head using a Base64 encoded value
ItemStack customHead2 = new SkullBuilder()
        .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjczMGJjMzY2NmYzMzM5ZjUzMzA2Yjg0NmYzMTc0NjY2OTQxYzg1MzA4MGVkMGM1N2M2YTc5MTNkZmRhOWRjYyJ9fX0=")
        .displayName(Component.text("Diamond Block"))
        .toItem();

// Create a head with both owner and ItemBuilder methods
ItemStack staffHead = new SkullBuilder()
        .owner("Notch")
        .displayName(Component.text("Staff Member"))
        .amount(1)
        .addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES)
        .toItem();
```

## Best Practices

- Use SkullBuilder to create player heads and custom textured skulls
- Chain method calls for cleaner, more readable code
- Take advantage of the fluent interface to configure skulls in a single statement
- Use texture strings from sources like minecraft-heads.com for custom designs (both Base64 encoded values and direct
  URLs are supported)
- Remember that SkullBuilder extends ItemBuilder, so you can use all ItemBuilder methods
- Always call `toItem()` at the end to get the final ItemStack
- Use `displayName()` with Component objects for modern text handling
- Use `lore()` with a List of Component objects for multi-line descriptions
