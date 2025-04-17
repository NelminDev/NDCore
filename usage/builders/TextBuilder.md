# TextBuilder

## Overview

A powerful text formatting utility that leverages Adventure API to create rich text content with support for colors, gradients, and replacements.

## Key Features

- Legacy color code support (`&` and `§`)
- Gradient text effects
- MiniMessage format parsing
- Text replacement functionality
- Player messaging utilities

## Usage Examples

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

## Best Practices

- Use TextBuilder to create complex text with many properties
- Chain method calls for cleaner, more readable code
- Take advantage of the fluent interface to configure text in a single statement
- Use the replacement functionality to create dynamic messages
- Leverage the Adventure API capabilities for rich text formatting