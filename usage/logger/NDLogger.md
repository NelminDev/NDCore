# NDLogger

## Overview

The main logging utility that provides different logging levels (INFO, WARN, ERROR, FATAL) with customizable format and behavior.

## Key Features

- Multiple logging levels with color-coded output
- Silent logging options for file-only logs
- Timestamp and context information in log messages
- Asynchronous file writing for performance

## Usage Examples

```java
// Creating a logger in your plugin
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

// Using logger from NDPlugin
@Override
public void disable() {
    // NDPlugin provides a logger() method
    logger().info("Plugin is shutting down");
    
    // Log cleanup operations
    logger().info("Saving", playerData.size(), "player records");
}
```

## Best Practices

- Use the appropriate log level for different types of messages:
  - `info()` for general information
  - `warn()` for potential issues
  - `error()` for recoverable errors
  - `fatal()` for critical errors that prevent functionality
- Include relevant context in log messages
- Use silent logging for verbose information that should be recorded but not displayed
- Create a dedicated logger for each major component of your plugin