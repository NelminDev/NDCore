# DefaultLoggingStrategy

## Overview

The default implementation of LoggingStrategy that handles formatting and writing log messages to files in the NDCore logging system.

## Key Features

- Color-coded console output
- Daily log file rotation
- Separate log files for different log levels
- Asynchronous file writing

## Usage Examples

```java
// The DefaultLoggingStrategy is used by NDLogger by default
// You don't need to create it manually unless you want to customize it

// Creating a custom instance with specific formatting
LoggingStrategy customStrategy = new DefaultLoggingStrategy(
    plugin,
    "[%timestamp] - %strategyName - %loggerName - %content",
    "CustomLogger",
    "FILE",
    "&7"
);

// Using the custom strategy with NDLogger
logger.log(customStrategy, false, "This message uses custom formatting");

// Using multiple strategies
public class AdvancedLoggingSystem {
    private final NDLogger logger;
    private final LoggingStrategy fileStrategy;
    private final LoggingStrategy discordStrategy;
    
    public AdvancedLoggingSystem(JavaPlugin plugin) {
        this.logger = new NDLogger("AdvancedLogger", plugin);
        
        // Standard file logging with custom format
        this.fileStrategy = new DefaultLoggingStrategy(
            plugin,
            "[%timestamp] - %strategyName - %loggerName - %content",
            "AdvancedLogger",
            "FILE",
            "&7"
        );
        
        // Discord logging for important events
        this.discordStrategy = new DiscordLoggingStrategy(
            "https://discord.com/api/webhooks/your-webhook-url",
            "**[%timestamp]** %content"
        );
    }
    
    public void logImportantEvent(String event) {
        // Log to both strategies
        logger.log(fileStrategy, false, event);
        logger.log(discordStrategy, false, event);
    }
}
```

## Best Practices

- Use the DefaultLoggingStrategy for standard logging needs
- Customize the format string to include relevant context information
- Consider creating specialized strategies for different output destinations
- Use the strategy pattern to extend logging capabilities without modifying NDLogger
- Take advantage of the asynchronous file writing for performance