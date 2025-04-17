# Logger Strategy Package - Introduction

## Overview

The `logger.strategy` package in NDCore provides a flexible system for customizing how log messages are formatted, generated, and written. It implements the Strategy design pattern, allowing different logging behaviors to be swapped in and out without changing the core logging code.

## Purpose

The primary purpose of the logger.strategy package is to:

- Separate logging behavior from the main logger implementation
- Allow for customizable message formatting
- Enable different output destinations (console, file, database, etc.)
- Provide a consistent interface for all logging strategies
- Support extension through custom strategy implementations

## Components

The logger.strategy package includes the following key classes:

1. **LoggingStrategy** - An interface that defines the contract for formatting, generating, and writing log messages
2. **DefaultLoggingStrategy** - The default implementation that handles formatting and writing log messages to files

## How It Works

The logging strategy system works as follows:

1. The NDLogger class uses a LoggingStrategy to handle the actual logging
2. The strategy is responsible for formatting messages, generating output, and writing to files
3. Different strategies can be created for different logging needs
4. Strategies can be swapped at runtime to change logging behavior

## Usage Example

Here's an example of how to use and create custom logging strategies:

```java
import dev.nelmin.ndcore.logger.NDLogger;
import dev.nelmin.ndcore.logger.strategy.DefaultLoggingStrategy;
import dev.nelmin.ndcore.logger.strategy.LoggingStrategy;
import dev.nelmin.ndcore.other.Pair;

// Using the default strategy with custom formatting
LoggingStrategy customStrategy = new DefaultLoggingStrategy(
    plugin,
    "[%timestamp] - %strategyName - %loggerName - %content",
    "CustomLogger",
    "FILE",
    "&7"
);

// Using the custom strategy with NDLogger
NDLogger logger = new NDLogger("MyPlugin", plugin);
logger.log(customStrategy, false, "This message uses custom formatting");

// Creating a custom strategy implementation
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
```

## Integration with NDLogger

The strategy pattern allows NDLogger to be extended without modification:

```java
// Using multiple strategies for different purposes
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
- Create specialized strategies for different output destinations
- Keep strategy implementations focused on a single responsibility
- Use composition to combine multiple strategies when needed
- Consider performance implications, especially for synchronous operations
- Implement proper error handling in custom strategies
- Use the strategy pattern to extend logging capabilities without modifying NDLogger