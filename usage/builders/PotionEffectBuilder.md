# PotionEffectBuilder

## Overview

A builder for creating customized potion effects with a fluent interface.

## Key Features

- Potion effect type selection
- Duration and amplifier configuration
- Ambient, particles, and icon settings
- Chainable API for clean code
- Static factory method for cleaner instantiation
- Duration in ticks or seconds

## Methods

| Method                                  | Description                                      |
|-----------------------------------------|--------------------------------------------------|
| `PotionEffectBuilder(PotionEffectType)` | Constructor with specified effect type           |
| `PotionEffectBuilder()`                 | Default constructor with LUCK effect type        |
| `of(PotionEffectType type)`             | Static factory method to create a builder        |
| `effect(PotionEffectType type)`         | Sets the effect type                             |
| `duration(int ticks)`                   | Sets the duration in ticks (20 ticks = 1 second) |
| `durationSeconds(int seconds)`          | Sets the duration in seconds                     |
| `amplifier(int level)`                  | Sets the amplifier (power level)                 |
| `ambient(boolean ambient)`              | Sets whether the effect is ambient               |
| `particles(boolean particles)`          | Sets whether the effect shows particles          |
| `icon(boolean icon)`                    | Sets whether the effect shows an icon            |
| `build()`                               | Builds and returns the PotionEffect              |

## Usage Examples

```java
import dev.nelmin.ndcore.builders.PotionEffectBuilder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

// Create a potion effect using constructor
PotionEffect invisibility = new PotionEffectBuilder(PotionEffectType.INVISIBILITY)
        .duration(20 * 60) // 1 minute in ticks
        .amplifier(1)      // Level II
        .ambient(true)     // Ambient particles
        .particles(false)  // Hide particles
        .icon(true)        // Show icon
        .build();

// Create a potion effect using static factory method
PotionEffect strength = PotionEffectBuilder.of(PotionEffectType.INCREASE_DAMAGE)
        .durationSeconds(30) // 30 seconds
        .amplifier(2)        // Level III
        .build();

// Create a potion effect and change its type
PotionEffect speed = new PotionEffectBuilder()  // Default GLOWING
        .effect(PotionEffectType.SPEED)         // Change to SPEED
        .durationSeconds(45)                    // 45 seconds
        .build();

// These effects can be applied to players with player.addPotionEffect(effect)
```

## Best Practices

- Use PotionEffectBuilder to create complex potion effects with many properties
- Chain method calls for cleaner, more readable code
- Take advantage of the fluent interface to configure effects in a single statement
- Use the static factory method `of()` for cleaner instantiation
- Use `durationSeconds()` for more readable duration setting
- Remember to call `build()` at the end to get the final PotionEffect