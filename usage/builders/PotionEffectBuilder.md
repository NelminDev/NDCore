# PotionEffectBuilder

## Overview

A builder for creating customized potion effects with a fluent interface.

## Key Features

- Potion effect type selection
- Duration and amplifier configuration
- Ambient, particles, and icon settings
- Chainable API for clean code

## Usage Examples

```java
import dev.nelmin.ndcore.builders.PotionEffectBuilder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

// Create a potion effect
PotionEffect invisibility = new PotionEffectBuilder(PotionEffectType.INVISIBILITY)
        .duration(20 * 60) // 1 minute in ticks
        .amplifier(1)      // Level II
        .ambient(true)     // Ambient particles
        .particles(false)  // Hide particles
        .icon(true)        // Show icon
        .build();

// Apply to player
player.addPotionEffect(invisibility);
```

## Best Practices

- Use PotionEffectBuilder to create complex potion effects with many properties
- Chain method calls for cleaner, more readable code
- Take advantage of the fluent interface to configure effects in a single statement