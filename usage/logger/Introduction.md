# Logger Package - Introduction

## Overview

The `logger` package in NDCore provides enhanced logging capabilities beyond what is available in Bukkit's standard logging system. It offers multiple logging levels, customizable formatting, and both console and file output options.

## Purpose

The primary purpose of the logger package is to:

- Provide more granular logging levels (INFO, WARN, ERROR, FATAL)
- Enable color-coded console output for better readability
- Support file logging with automatic rotation
- Allow for silent logging (file-only) for verbose information
- Provide a consistent logging interface across plugins

## Components

The logger package includes the following key classes:

1. **NDLogger** - The main logging utility that provides different logging levels with customizable format and behavior

The logger package also contains a `strategy` subpackage that defines the logging strategy interface and implementations.

## Usage Example

Here's an example of how to use the NDLogger in your plugin:

```java
import dev.nelmin.ndcore.logger.NDLogger;

public class MyPlugin extends NDPlugin {
    private NDLogger logger;
    
    @Override
    public void enable() {
        // Initialize logger with your plugin's name
        this.logger = new NDLogger("MyPlugin", this);
        
        // Basic logging
        logger.info("Plugin initialized successfully");
        
        // Logging with multiple parameters
        logger.info("Player", player.getName(), "joined the game");
        
        // Warning messages
        logger.warn("Low memory detected:", Runtime.getRuntime().freeMemory() / 1024 / 1024, "MB available");
        
        // Error logging
        try {
            // Some risky operation
            loadConfiguration();
        } catch (Exception e) {
            logger.error("Failed to load configuration:", e.getMessage());
            e.printStackTrace();
        }
        
        // Fatal errors (critical issues that might require shutdown)
        if (!databaseConnected) {
            logger.fatal("Database connection failed - plugin cannot function!");
            getServer().getPluginManager().disablePlugin(this);
        }
        
        // Silent logging (writes to file but not console)
        logger.infoSilent("This message only goes to the log file");
    }
}
```

## Integration with NDPlugin

When extending NDPlugin, you get access to a logger instance through the `logger()` method:

```java
public class MyPlugin extends NDPlugin {
    
    @Override
    public void enable() {
        // Use the logger provided by NDPlugin
        logger().info("Plugin enabled!");
        
        // Log with multiple parameters
        logger().info("Server version:", getServer().getVersion());
    }
    
    @Override
    public void disable() {
        logger().info("Plugin disabled!");
    }
}
```

## Log Levels

NDLogger provides the following log levels:

1. **INFO** - General information messages
2. **WARN** - Warning messages for potential issues
3. **ERROR** - Error messages for recoverable errors
4. **FATAL** - Critical error messages that prevent functionality

Each level has both a standard and silent variant (e.g., `info()` and `infoSilent()`).

## Best Practices

- Use the appropriate log level for different types of messages
- Include relevant context in log messages
- Use silent logging for verbose information that should be recorded but not displayed
- Create a dedicated logger for each major component of your plugin
- Take advantage of the multi-parameter logging methods for cleaner code
- Consider using custom logging strategies for specialized logging needs