# TextBuilder

## Overview

A powerful text formatting utility that leverages Adventure API to create rich text content with support for colors, gradients, and replacements.

## Key Features

- Legacy color code support (`&` and `§`)
- Gradient text effects
- MiniMessage format parsing
- Text replacement functionality
- Player messaging utilities
- Prefix and suffix support
- Component to plain text conversion

## Constructors

| Constructor                                          | Description                                            |
|------------------------------------------------------|--------------------------------------------------------|
| `TextBuilder(String message)`                        | Create a TextBuilder with a message                    |
| `TextBuilder(String message, boolean prefixEnabled)` | Create a TextBuilder with a message and prefix setting |

## Methods

### Text Formatting

| Method                                       | Description                                |
|----------------------------------------------|--------------------------------------------|
| `componentToPlainText(Component component)`  | Convert a Component to plain text          |
| `colorize(char legacyCharacter)`             | Colorize text using legacy color codes     |
| `gradient(Color startColor, Color endColor)` | Apply a gradient from one color to another |
| `alternate(TextColor... colors)`             | Alternate between multiple colors          |
| `parseMiniMessage(String miniMessageFormat)` | Parse text using MiniMessage format        |

### Content Management

| Method                           | Description              |
|----------------------------------|--------------------------|
| `message()`                      | Get the current message  |
| `message(String message)`        | Set a new message        |
| `prefix(String prefix)`          | Set a prefix             |
| `prefixEnabled(boolean enabled)` | Enable or disable prefix |
| `suffix(String suffix)`          | Add a suffix             |

### Text Replacement

| Method                                       | Description                              |
|----------------------------------------------|------------------------------------------|
| `replace(Object search, Object replacement)` | Replace a single occurrence              |
| `replace(Map<Object, Object> replacements)`  | Replace multiple occurrences using a map |

### Message Sending

| Method                                            | Description              |
|---------------------------------------------------|--------------------------|
| `sendTo(Player player, boolean colorized)`        | Send to a single player  |
| `sendTo(List<Player> players, boolean colorized)` | Send to multiple players |
| `sendToConsole()`                                 | Send to console          |

### Other Methods

| Method       | Description                   |
|--------------|-------------------------------|
| `toString()` | Get the string representation |

## Usage Examples

### Basic Usage

```
// Import the TextBuilder class
import dev.nelmin.ndcore.builders.TextBuilder;

// Create a colored message using legacy color codes
Component coloredText = new TextBuilder("&aWelcome to the server!").colorize('&');

// Create text with a color gradient from blue to red
TextBuilder gradientText = new TextBuilder("This is a fancy title").gradient(Color.BLUE, Color.RED);

// Create a message with placeholders and replace them
TextBuilder personalizedMessage = new TextBuilder("Hello, {player}! Your balance is {balance}.");
personalizedMessage.replace("{player}", "Steve");
personalizedMessage.replace("{balance}", "1000");

// Create and send a message to a player
TextBuilder message = new TextBuilder("&6You received &e{amount} &6coins!");
message.replace("{amount}", "50");
message.sendTo(player, true);

// Parse text using MiniMessage format
TextBuilder miniMessage = new TextBuilder("");
miniMessage.parseMiniMessage("<rainbow>This text has rainbow colors</rainbow>");

// Add prefix and suffix to a message
TextBuilder prefixedMessage = new TextBuilder("Welcome to the server!");
prefixedMessage.prefix("[Server]");
prefixedMessage.suffix(" Enjoy your stay!");

// Send a message to multiple players
TextBuilder teamMessage = new TextBuilder("Team objective completed!");
teamMessage.prefix("[Team]");
teamMessage.sendTo(playerList, true);

// Convert a component to plain text
TextBuilder converter = new TextBuilder("");
String plainText = converter.componentToPlainText(someComponent);

// Create text with alternating colors
TextBuilder alternatingText = new TextBuilder("This text alternates between colors");
alternatingText.alternate(TextColor.color(255, 0, 0), TextColor.color(0, 0, 255));
```

## Best Practices

- Use TextBuilder to create complex text with many properties
- Chain method calls for cleaner, more readable code
- Take advantage of the fluent interface to configure text in a single statement
- Use the replacement functionality to create dynamic messages
- Leverage the Adventure API capabilities for rich text formatting
- Use prefix and suffix for consistent message formatting
- Consider using alternate() for eye-catching text effects
