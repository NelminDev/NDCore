# ConfigurationValueNullOrEmptyException

## Overview

`ConfigurationValueNullOrEmptyException` is an exception that is thrown when a configuration value is expected to be
non-empty but is found to be empty or blank.

## Class Declaration

```java
public class ConfigurationValueNullOrEmptyException extends Exception
```

## Usage Context

This exception is primarily used in the `JSONConfiguration` class when retrieving values that must not be empty:

- `getStringOrThrow()` - Throws this exception if the retrieved string is empty or contains only whitespace
- `getListOrThrow()` - Throws this exception if the retrieved list is empty

## Constructors

| Constructor                                                               | Description                                                         |
|---------------------------------------------------------------------------|---------------------------------------------------------------------|
| `ConfigurationValueNullOrEmptyException(String message)`                  | Creates a new exception with the specified detail message           |
| `ConfigurationValueNullOrEmptyException(String message, Throwable cause)` | Creates a new exception with the specified detail message and cause |
| `ConfigurationValueNullOrEmptyException(Throwable cause)`                 | Creates a new exception with the specified cause                    |

## Example Usage

```java
try {
    // This will throw ConfigurationValueNullOrEmptyException if "server.name" exists but is empty
    String serverName = config.getStringOrThrow("server.name");
    
    // This will throw ConfigurationValueNullOrEmptyException if "players.allowed" exists but is an empty list
    List<?> allowedPlayers = config.getListOrThrow("players.allowed");
} catch (ConfigurationValueNullOrEmptyException e) {
    // Handle the case where a required value is empty
    logger.error("Configuration error: " + e.getMessage());
    // Provide a default value or take other appropriate action
}
```

## Best Practices

- Use this exception to validate that critical configuration values are not only present but also contain meaningful
  data
- Always catch this exception separately from NullPointerException to provide specific error handling for empty values
- Consider providing user-friendly error messages that guide the user to fix the configuration issue