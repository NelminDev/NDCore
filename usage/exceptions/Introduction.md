# Exceptions Package - Introduction

## Overview

The `exceptions` package in NDCore provides specialized exception classes that help identify and handle specific error conditions that may occur during plugin operation. These custom exceptions make error handling more precise and informative.

## Purpose

The primary purpose of the exceptions package is to:

- Provide clear, specific error types for different failure scenarios
- Enable more granular exception handling
- Improve debugging by making error sources more identifiable
- Allow for more informative error messages to developers

## Components

The exceptions package includes the following key classes:

1. **ConfigurationKeyNotFoundException** - Thrown when a requested configuration key is not found
2. **TranslationAlreadyAvailableException** - Thrown when attempting to add a translation that already exists

## Usage Example

Here's an example of how to use and handle these custom exceptions:

```java
import dev.nelmin.ndcore.exceptions.ConfigurationKeyNotFoundException;
import dev.nelmin.ndcore.exceptions.TranslationAlreadyAvailableException;

// Using ConfigurationKeyNotFoundException
public String getConfigValue(String key) throws ConfigurationKeyNotFoundException {
    if (!config.contains(key)) {
        throw new ConfigurationKeyNotFoundException("Configuration key '" + key + "' not found");
    }
    return config.getString(key);
}

// Handling ConfigurationKeyNotFoundException
public void loadServerName() {
    try {
        String serverName = getConfigValue("server.name");
        // Use the server name
    } catch (ConfigurationKeyNotFoundException e) {
        // Log the error
        logger.severe("Missing configuration: " + e.getMessage());
        
        // Set a default value
        String serverName = "Default Server";
        
        // Or create the missing configuration
        config.set("server.name", "Default Server");
        config.save(configFile);
    }
}

// Using TranslationAlreadyAvailableException
public void addTranslation(String key, LanguageCode language, String translation) 
        throws TranslationAlreadyAvailableException {
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

// Handling TranslationAlreadyAvailableException
public void setupTranslations() {
    try {
        addTranslation("welcome.message", LanguageCode.ENGLISH, "Welcome to the server!");
    } catch (TranslationAlreadyAvailableException e) {
        // Log the warning
        logger.warning("Duplicate translation: " + e.getMessage());
        
        // Force update the translation if needed
        updateTranslation("welcome.message", LanguageCode.ENGLISH, "Welcome to the server!");
    }
}
```

## Best Practices

- Use custom exceptions to provide clear error messages about what went wrong
- Always catch exceptions at an appropriate level in your code
- Include relevant information in the exception message to aid debugging
- Consider providing fallback behavior when exceptions occur
- Document which methods can throw which exceptions in your Javadoc comments
- Use exceptions for exceptional conditions, not for normal control flow