# Builders Package - Introduction

## Overview

The `builders` package in NDCore provides a collection of fluent builder classes that simplify the creation and
customization of various game elements. These builders follow the Builder design pattern, allowing for clean, readable
code when creating complex objects.

## Purpose

The primary purpose of the builders package is to abstract away the verbose and error-prone process of creating and
configuring common Minecraft objects such as items, text, potion effects, and player heads. By using these builders,
developers can:

- Create complex objects with a clean, chainable API
- Reduce boilerplate code
- Improve code readability
- Ensure proper configuration of objects

## Components

The builders package includes the following key classes:

1. **ItemBuilder** - Creates and customizes ItemStacks with features like custom names, lore, enchantments, and item
   flags. Uses Lombok's @Data annotation for cleaner code.
2. **TextBuilder** - Creates rich text content using Adventure API with support for colors, gradients, and replacements.
   Provides methods for content manipulation and sending messages.
3. **PotionEffectBuilder** - Creates customized potion effects with duration, amplifier, and visual settings. Includes
   convenience methods for duration in seconds.
4. **SkullBuilder** - Creates player heads and custom textured skulls. Extends ItemBuilder to inherit all its
   functionality.

## Usage Example

Here's a simple example of how the builders can be used together:

```java
import dev.nelmin.ndcore.builders.ItemBuilder;
import dev.nelmin.ndcore.builders.PotionEffectBuilder;
import dev.nelmin.ndcore.builders.TextBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.Color;
import java.util.List;

// Create a custom item with the ItemBuilder
ItemStack healingPotion = new ItemBuilder(Material.POTION)
        .displayName(new TextBuilder("Healing Potion")
        .gradient(Color.RED, Color.PINK)
                .colorize('&'))
        .lore(List.of(
                Component.text("Instantly restores health"),
                Component.text("and gives regeneration")
        ))
        .toItem();

// Create a potion effect with the PotionEffectBuilder
PotionEffect regeneration = new PotionEffectBuilder(PotionEffectType.REGENERATION)
        .durationSeconds(10)  // 10 seconds
        .amplifier(1)         // Level II
    .ambient(true)
    .build();

// These objects can now be used in your plugin
// For example, in a click event handler
```

## Best Practices

- Chain builder methods for cleaner, more readable code
- Use the appropriate builder for each type of object
- Take advantage of the fluent interface to configure objects in a single statement
- Remember to call the final method (`toItem()`, `build()`, etc.) to get the actual object
- Use Component objects with displayName() and lore() methods in ItemBuilder
- Consider creating your own builders for complex custom objects in your plugin