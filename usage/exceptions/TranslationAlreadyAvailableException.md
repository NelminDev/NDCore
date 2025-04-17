# TranslationAlreadyAvailableException

## Overview

An exception that is thrown when attempting to add a translation that already exists.

## Description

This exception extends the standard Java `Exception` class and is thrown when trying to add a translation for a key and language that already has a translation. This helps prevent accidental overwriting of existing translations.

## Usage Examples

```java
// Throwing the exception when a translation already exists
public void addTranslation(String key, LanguageCode language, String translation) throws TranslationAlreadyAvailableException {
    if (translations.containsKey(key) && translations.get(key).containsKey(language)) {
        throw new TranslationAlreadyAvailableException(
            "Translation for key '" + key + "' in language '" + language + "' already exists"
        );
    }
    
    // Add the translation
    if (!translations.containsKey(key)) {
        translations.put(key, new HashMap<>());
    }
    translations.get(key).put(language, translation);
}

// Catching and handling the exception
try {
    translationManager.addTranslation("welcome.message", LanguageCode.EN, "Welcome to the server!");
} catch (TranslationAlreadyAvailableException e) {
    // Log the warning
    logger.warning("Duplicate translation: " + e.getMessage());
    
    // Force update the translation if needed
    translationManager.updateTranslation("welcome.message", LanguageCode.EN, "Welcome to the server!");
}
```

## Best Practices

- Use this exception to provide clear error messages about what went wrong
- Always catch this exception at an appropriate level in your code
- Include relevant information in the exception message to aid debugging
- Consider providing fallback behavior when the exception occurs
- Document which methods can throw this exception in your Javadoc comments