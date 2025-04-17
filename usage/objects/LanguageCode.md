# LanguageCode

## Overview

An enum that represents ISO 639-1 language codes, providing a type-safe way to work with language identifiers in NDCore.

## Key Features

- Comprehensive list of language codes based on ISO 639-1 standard
- Type-safe language code representation
- Easy access to the two-letter language code string

## Usage Examples

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

## Best Practices

- Use LanguageCode enum instead of string language codes to avoid typos
- Provide a fallback language (typically ENGLISH) when a requested language is not available
- Consider allowing players to select their preferred language
- Use LanguageCode in conjunction with LocalizedMessage for a complete translation system