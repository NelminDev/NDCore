# Objects Usage Guide

## Overview

The Objects package provides utility classes for managing language codes and localized messages in NDCore. These components enable multilingual support in your plugins, allowing you to provide translations for different languages.

## Components

### LanguageCode

An enum that represents ISO 639-1 language codes, providing a type-safe way to work with language identifiers.

#### Key Features

- Comprehensive list of language codes based on ISO 639-1 standard
- Type-safe language code representation
- Easy access to the two-letter language code string

#### Usage Examples

```java
// Using language codes
LanguageCode english = LanguageCode.ENGLISH;
LanguageCode german = LanguageCode.GERMAN;
LanguageCode japanese = LanguageCode.JAPANESE;

// Getting the two-letter code
String englishCode = english.get(); // Returns "en"
String germanCode = german.get();   // Returns "de"
String japaneseCode = japanese.get(); // Returns "ja"

// Using language codes for player preferences
public void setPlayerLanguage(Player player, LanguageCode language) {
    playerLanguages.put(player.getUniqueId(), language);
    player.sendMessage("Language set to: " + language.name());
}

// Determining language from locale
public LanguageCode getLanguageFromLocale(String locale) {
    String code = locale.substring(0, 2).toLowerCase();
    for (LanguageCode language : LanguageCode.values()) {
        if (language.get().equals(code)) {
            return language;
        }
    }
    return LanguageCode.ENGLISH; // Default fallback
}
```

### LocalizedMessage

A class that manages translations for messages in different languages, with fallback support for missing translations.

#### Key Features

- Fluent API for adding and removing translations
- Fallback language and message support
- YAML file loading for translations
- Type-safe language code handling

#### Usage Examples

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

## Creating a Translation System

You can build a complete translation system using these components:

```java
public class TranslationManager {
    private final Map<String, LocalizedMessage> messages = new HashMap<>();
    private final Path langFolder;
    private final Map<UUID, LanguageCode> playerLanguages = new HashMap<>();
    
    public TranslationManager(JavaPlugin plugin) {
        this.langFolder = plugin.getDataFolder().toPath().resolve("lang");
        // Create language folder if it doesn't exist
        if (!Files.exists(langFolder)) {
            try {
                Files.createDirectories(langFolder);
                // Create default English file
                saveDefaultLanguageFile(LanguageCode.ENGLISH);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create language folder: " + e.getMessage());
            }
        }
    }
    
    public void loadAllMessages() {
        // Load all message keys from the default language file
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(langFolder.resolve("en.yml").toFile());
            
            for (String key : config.getKeys(true)) {
                if (config.isString(key)) {
                    loadMessage(key);
                }
            }
        } catch (Exception e) {
            // Handle error
        }
    }
    
    private void loadMessage(String key) {
        LocalizedMessage message = new LocalizedMessage();
        
        // Try to load for all available language files
        for (File file : langFolder.toFile().listFiles((dir, name) -> name.endsWith(".yml"))) {
            String langCode = file.getName().replace(".yml", "");
            LanguageCode language = null;
            
            // Find the corresponding LanguageCode
            for (LanguageCode code : LanguageCode.values()) {
                if (code.get().equals(langCode)) {
                    language = code;
                    break;
                }
            }
            
            if (language != null) {
                try {
                    message.getFromYamlFile(language, key, langFolder, "{lang}.yml", false);
                } catch (Exception e) {
                    // Skip this language for this key
                }
            }
        }
        
        messages.put(key, message);
    }
    
    public String getMessage(String key, Player player) {
        LocalizedMessage message = messages.get(key);
        if (message == null) {
            return "Missing translation key: " + key;
        }
        
        LanguageCode language = playerLanguages.getOrDefault(
            player.getUniqueId(), 
            LanguageCode.ENGLISH
        );
        
        return message.getTranslation(language);
    }
    
    public void setPlayerLanguage(Player player, LanguageCode language) {
        playerLanguages.put(player.getUniqueId(), language);
    }
    
    public LanguageCode getPlayerLanguage(Player player) {
        return playerLanguages.getOrDefault(player.getUniqueId(), LanguageCode.ENGLISH);
    }
}
```

## Best Practices

- Use LanguageCode enum instead of string language codes to avoid typos
- Always provide a fallback language and message for LocalizedMessage
- Organize translation files by language (e.g., en.yml, de.yml)
- Use a consistent key structure for translations (e.g., "category.subcategory.message")
- Cache LocalizedMessage objects rather than creating them for each use
- Allow players to select their preferred language
- Consider using placeholders in translations for dynamic content
- Handle missing translations gracefully with meaningful fallbacks