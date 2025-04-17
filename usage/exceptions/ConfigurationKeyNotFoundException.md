# ConfigurationKeyNotFoundException

## Overview

An exception that is thrown when a requested configuration key is not found.

## Description

This exception extends the standard Java `Exception` class and is thrown when a configuration key that was expected to exist is not found. This helps distinguish between configuration errors and other types of errors in your plugin.

## Usage Examples

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

## Best Practices

- Use this exception to provide clear error messages about what went wrong
- Always catch this exception at an appropriate level in your code
- Include relevant information in the exception message to aid debugging
- Consider providing fallback behavior when the exception occurs
- Document which methods can throw this exception in your Javadoc comments