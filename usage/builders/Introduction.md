# Builders Package - Introduction

## Overview

The `builders` package in NDCore provides a collection of fluent builder classes that simplify the creation and customization of various game elements. These builders follow the Builder design pattern, allowing for clean, readable code when creating complex objects.

## Purpose

The primary purpose of the builders package is to abstract away the verbose and error-prone process of creating and configuring common Minecraft objects such as items, text, potion effects, and player heads. By using these builders, developers can:

- Create complex objects with a clean, chainable API
- Reduce boilerplate code
- Improve code readability
- Ensure proper configuration of objects

## Components

The builders package includes the following key classes:

1. **ItemBuilder** - Creates and customizes ItemStacks with features like custom names, lore, enchantments, and item flags
2. **TextBuilder** - Creates rich text content using Adventure API with support for colors, gradients, and replacements
3. **PotionEffectBuilder** - Creates customized potion effects with duration, amplifier, and visual settings
4. **SkullBuilder** - Creates player heads and custom textured skulls

## Usage Example

Here's a simple example of how the builders can be used together:

```java
// Create a custom item with the ItemBuilder
ItemStack healingPotion = new ItemBuilder(Material.POTION)
    .name(new TextBuilder("&cHealing Potion")
        .gradient(Color.RED, Color.PINK)
        .build())
    .lore(
        "&7Instantly restores health",
        "&7and gives regeneration"
    )
    .build();

// Create a potion effect with the PotionEffectBuilder
PotionEffect regeneration = new PotionEffectBuilder(PotionEffectType.REGENERATION)
    .duration(20 * 10)  // 10 seconds
    .amplifier(1)       // Level II
    .ambient(true)
    .build();

// Apply the effect when the potion is consumed
// (This would be in an event handler)
player.addPotionEffect(regeneration);
```

## Best Practices

- Chain builder methods for cleaner, more readable code
- Use the appropriate builder for each type of object
- Take advantage of the fluent interface to configure objects in a single statement
- Consider creating your own builders for complex custom objects in your plugin