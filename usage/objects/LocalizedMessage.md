# LocalizedMessage

## Overview

A class that manages translations for messages in different languages, with fallback support for missing translations in NDCore.

## Key Features

- Fluent API for adding and removing translations
- Fallback language and message support
- YAML file loading for translations
- Type-safe language code handling

## Usage Examples

```java
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

// Checking if a translation exists
if (!welcomeMessage.hasTranslation(LanguageCode.ITALIAN)) {
    // Add the translation
    welcomeMessage.add(LanguageCode.ITALIAN, "Benvenuto nel server!");
}

// Removing a translation
welcomeMessage.remove(LanguageCode.FRENCH);

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

## Best Practices

- Always provide a fallback language and message for LocalizedMessage
- Organize translation files by language (e.g., en.yml, de.yml)
- Use a consistent key structure for translations (e.g., "category.subcategory.message")
- Cache LocalizedMessage objects rather than creating them for each use
- Allow players to select their preferred language
- Consider using placeholders in translations for dynamic content
- Handle missing translations gracefully with meaningful fallbacks