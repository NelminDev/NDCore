# SkullBuilder

## Overview

A specialized builder for creating player heads and custom skulls with a fluent interface.

## Key Features

- Player head creation by UUID or name
- Custom texture support
- Display name and lore customization
- Integration with ItemBuilder functionality

## Usage Examples

```java
import dev.nelmin.ndcore.builders.SkullBuilder;
import org.bukkit.inventory.ItemStack;

// Create a player head
ItemStack playerHead = new SkullBuilder()
        .owner(player.getUniqueId())
        .name("&e" + player.getName())
        .lore("&7Click to view profile")
        .build();

// Create a custom textured head
ItemStack customHead = new SkullBuilder()
        .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTM1YTZkZDg2ZDg3YWFiNDI5ZDFmZmQ5ZWNiOWY0NzJiNjNkOTZlOTliODdkMTRjNDNlODgyM2Y5MWI5NyJ9fX0=")
        .name("&6Coin")
        .build();
```

## Best Practices

- Use SkullBuilder to create player heads and custom textured skulls
- Chain method calls for cleaner, more readable code
- Take advantage of the fluent interface to configure skulls in a single statement
- Use texture strings from sources like minecraft-heads.com for custom designs
