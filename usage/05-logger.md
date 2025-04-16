# Logger Usage Guide

## Overview

The Logger package provides a flexible and powerful logging system for NDCore plugins. It offers different logging levels, customizable formatting, and the ability to write logs to both the console and files.

## Components

### NDLogger

The main logging utility that provides different logging levels (INFO, WARN, ERROR, FATAL) with customizable format and behavior.

#### Key Features

- Multiple logging levels with color-coded output
- Silent logging options for file-only logs
- Timestamp and context information in log messages
- Asynchronous file writing for performance

#### Usage Examples

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

### LoggingStrategy

An interface that defines the contract for formatting, generating, and writing log messages.

#### Key Features

- Customizable message formatting
- Separate file and console output handling
- Extensible for custom logging behaviors

#### Usage Examples

```java
// Creating a custom logging strategy
public class DatabaseLoggingStrategy implements LoggingStrategy {
    private final DatabaseConnection db;
    private String format;
    
    public DatabaseLoggingStrategy(DatabaseConnection db, String format) {
        this.db = db;
        this.format = format;
    }
    
    @Override
    public void format(@NotNull String format) {
        this.format = format;
    }
    
    @Override
    public void log(boolean silent, @NotNull Object... content) {
        Pair<String, TextBuilder> messagePair = generateMessage(content);
        
        // Store in database
        db.insertLog(messagePair.first());
        
        // Also output to console if not silent
        if (!silent && messagePair.second() != null) {
            messagePair.second().sendToConsole();
        }
    }
    
    @Override
    @NotNull
    public Pair<String, TextBuilder> generateMessage(@NotNull Object... content) {
        // Format the message
        String message = Arrays.stream(content)
                .map(Object::toString)
                .collect(Collectors.joining(" "));
                
        String formattedMessage = format
                .replace("%timestamp", new DateTime().formatTime())
                .replace("%content", message);
                
        return new Pair<>(formattedMessage, new TextBuilder(formattedMessage));
    }
    
    @Override
    public void writeToFile(@NotNull String message) {
        // Not used in this strategy as we write to database instead
    }
}

// Using a custom logging strategy
public void setupCustomLogger() {
    LoggingStrategy dbStrategy = new DatabaseLoggingStrategy(
        databaseConnection, 
        "[%timestamp] DB-LOG: %content"
    );
    
    // Use the strategy with NDLogger
    logger.log(dbStrategy, false, "This message will be stored in the database");
}
```

### DefaultLoggingStrategy

The default implementation of LoggingStrategy that handles formatting and writing log messages to files.

#### Key Features

- Color-coded console output
- Daily log file rotation
- Separate log files for different log levels
- Asynchronous file writing

## Best Practices

- Use the appropriate log level for different types of messages:
  - `info()` for general information
  - `warn()` for potential issues
  - `error()` for recoverable errors
  - `fatal()` for critical errors that prevent functionality
- Include relevant context in log messages
- Use silent logging for verbose information that should be recorded but not displayed
- Create a dedicated logger for each major component of your plugin
- Consider implementing custom logging strategies for special requirements