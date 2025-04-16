# Builders Usage Guide

## Overview

The Builders package provides fluent builder classes for creating and manipulating various Minecraft objects with a clean, chainable API. These builders simplify complex object creation and reduce boilerplate code.

## Components

### TextBuilder

A powerful text formatting utility that leverages Adventure API to create rich text content with support for colors, gradients, and replacements.

#### Key Features

- Legacy color code support (`&` and `§`)
- Gradient text effects
- MiniMessage format parsing
- Text replacement functionality
- Player messaging utilities

#### Usage Examples

```java
import dev.nelmin.ndcore.builders.TextBuilder;

// Basic colored message
Component coloredText = new TextBuilder("&aWelcome to the server!")
        .colorize('&');

// Gradient text
TextBuilder gradientText = new TextBuilder("This is a fancy title")
        .gradient(Color.BLUE, Color.RED);

// Text with replacements
TextBuilder personalizedMessage = new TextBuilder("Hello, {player}! Your balance is {balance}.")
        .replace("{player}", player.getName())
        .replace("{balance}", economy.getBalance(player));

// Send message to player
new TextBuilder("&6You received &e{amount} &6coins!")
        .replace("{amount}",reward)
        .sendTo(player, true);

// MiniMessage format
TextBuilder miniMessage = new TextBuilder("")
        .parseMiniMessage("<rainbow>This text has rainbow colors</rainbow>");
```

### ItemBuilder

A utility for creating and customizing ItemStacks with a fluent interface.

#### Key Features

- Material selection
- Custom display name and lore
- Enchantment application
- Item flags management
- NBT data manipulation

#### Usage Examples

```java
import dev.nelmin.ndcore.builders.ItemBuilder;
import dev.nelmin.ndcore.builders.SkullBuilder;
import org.bukkit.inventory.ItemStack;

// Create a basic item
ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD)
        .name("&bFrost Blade")
        .lore("&7A sword forged from", "&7ancient ice crystals")
        .enchant(Enchantment.DAMAGE_ALL, 5)
        .flag(ItemFlag.HIDE_ATTRIBUTES)
        .build();

// Create a custom skull
ItemStack playerHead = new SkullBuilder()
        .owner(player.getUniqueId())
        .name("&e" + player.getName() + "'s Head")
        .build();
```

### PotionEffectBuilder

A builder for creating customized potion effects.

#### Usage Examples

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

### SkullBuilder

A specialized builder for creating player heads and custom skulls.

#### Usage Examples

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

- Use builders to create complex objects with many properties
- Chain method calls for cleaner, more readable code
- Take advantage of the fluent interface to configure objects in a single statement
- Use the replacement functionality in TextBuilder to create dynamic messages
- Leverage the Adventure API capabilities through TextBuilder for rich text formatting