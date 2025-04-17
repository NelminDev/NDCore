# Objects Package - Introduction

## Overview

The `objects` package in NDCore provides utility objects that handle localization, language codes, and message translation. These objects form the foundation of NDCore's internationalization system.

## Purpose

The primary purpose of the objects package is to:

- Provide a robust localization system for multi-language support
- Offer type-safe language code handling
- Enable fallback mechanisms for missing translations
- Support loading translations from configuration files
- Simplify the process of sending localized messages to players

## Components

The objects package includes the following key classes:

1. **LanguageCode** - An enum that represents ISO 639-1 language codes, providing a type-safe way to work with language identifiers
2. **LocalizedMessage** - A class that manages translations for messages in different languages, with fallback support for missing translations

## How It Works

The localization system works as follows:

1. You create LocalizedMessage objects with translations for different languages
2. Players have a preferred language stored in their player data
3. When sending a message to a player, the system retrieves the translation in their preferred language
4. If a translation is not available in the player's language, it falls back to a default language
5. This ensures players always receive messages they can understand

## Usage Example

Here's an example of how to use the localization system:

```java
import dev.nelmin.ndcore.objects.LanguageCode;
import dev.nelmin.ndcore.objects.LocalizedMessage;

// Creating a simple localized message
LocalizedMessage welcomeMessage = new LocalizedMessage()
    .add(LanguageCode.ENGLISH, "Welcome to the server!")
    .add(LanguageCode.GERMAN, "Willkommen auf dem Server!")
    .add(LanguageCode.FRENCH, "Bienvenue sur le serveur!")
    .add(LanguageCode.SPANISH, "¡Bienvenido al servidor!")
    .fallbackLanguageCode(LanguageCode.ENGLISH)
    .fallbackMessage("Welcome! (Translation not available in your language)");

// Getting a translation for a specific language
String message = welcomeMessage.getTranslation(LanguageCode.GERMAN);
player.sendMessage(message); // Sends "Willkommen auf dem Server!"

// Handling missing translations with fallback
LanguageCode playerLanguage = getPlayerLanguage(player);
String localizedMessage = welcomeMessage.getTranslation(playerLanguage);
player.sendMessage(localizedMessage); // Falls back if translation not available

// Loading translations from YAML files
try {
    Path langFolder = plugin.getDataFolder().toPath().resolve("lang");
    
    // Load English translation
    LocalizedMessage helpMessage = new LocalizedMessage()
        .getFromYamlFile(
            LanguageCode.ENGLISH,
            "messages.help",
            langFolder,
            "{lang}.yml",
            false
        );
    
    // Add more languages
    for (LanguageCode language : Arrays.asList(
            LanguageCode.GERMAN, 
            LanguageCode.FRENCH, 
            LanguageCode.SPANISH)) {
        try {
            helpMessage.getFromYamlFile(
                language,
                "messages.help",
                langFolder,
                "{lang}.yml",
                false
            );
        } catch (Exception e) {
            logger.warning("Could not load " + language + " translation: " + e.getMessage());
        }
    }
    
    // Use the message
    player.sendMessage(helpMessage.getTranslation(playerLanguage));
    
} catch (Exception e) {
    logger.severe("Failed to load translations: " + e.getMessage());
}
```

## Integration with Player System

The localization system integrates with the player system:

```java
// Using BasicNDPlayer to send localized messages
public void sendWelcomeMessage(Player player) {
    BasicNDPlayer ndPlayer = BasicNDPlayer.of(player, this);
    
    // Create a localized message
    LocalizedMessage welcomeMessage = new LocalizedMessage()
        .add(LanguageCode.ENGLISH, "Welcome to the server!")
        .add(LanguageCode.GERMAN, "Willkommen auf dem Server!")
        .add(LanguageCode.FRENCH, "Bienvenue sur le serveur!")
        .add(LanguageCode.SPANISH, "¡Bienvenido al servidor!");
    
    // Send the message in the player's preferred language
    ndPlayer.sendLocalizedMessage(welcomeMessage, e -> {
        getLogger().severe("Error sending localized message: " + e.getMessage());
    });
}
```

## Best Practices

- Always provide a fallback language and message for LocalizedMessage
- Organize translation files by language (e.g., en.yml, de.yml)
- Use a consistent key structure for translations (e.g., "category.subcategory.message")
- Cache LocalizedMessage objects rather than creating them for each use
- Allow players to select their preferred language
- Consider using placeholders in translations for dynamic content
- Handle missing translations gracefully with meaningful fallbacks