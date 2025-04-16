# Exceptions Usage Guide

## Overview

The Exceptions package provides custom exception classes for handling specific error conditions in NDCore. These exceptions help with proper error handling and provide clear information about what went wrong.

## Components

### ConfigurationKeyNotFoundException

An exception that is thrown when a requested configuration key is not found.

#### Description

This exception extends the standard Java `Exception` class and is thrown when a configuration key that was expected to exist is not found. This helps distinguish between configuration errors and other types of errors in your plugin.

#### Usage Examples

```java
// Throwing the exception when a configuration key is missing
public String getConfigValue(String key) throws ConfigurationKeyNotFoundException {
    if (!config.contains(key)) {
        throw new ConfigurationKeyNotFoundException("Configuration key '" + key + "' not found");
    }
    return config.getString(key);
}

// Catching and handling the exception
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
```

### TranslationAlreadyAvailableException

An exception that is thrown when attempting to add a translation that already exists.

#### Description

This exception extends the standard Java `Exception` class and is thrown when trying to add a translation for a key and language that already has a translation. This helps prevent accidental overwriting of existing translations.

#### Usage Examples

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

- Use these exceptions to provide clear error messages about what went wrong
- Always catch these exceptions at an appropriate level in your code
- Include relevant information in the exception message to aid debugging
- Consider providing fallback behavior when exceptions occur
- Use try-with-resources when working with resources that need to be closed
- Document which methods can throw these exceptions in your Javadoc comments