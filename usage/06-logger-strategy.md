# Logger Strategy Usage Guide

## Overview

The Logger Strategy package provides interfaces and implementations for defining how logging operations are performed in NDCore. This package works in conjunction with the NDLogger to provide a flexible and extensible logging system.

## Components

For detailed information about the Logger Strategy components, please refer to the [Logger Usage Guide](05-logger.md), which covers:

- **LoggingStrategy Interface**: The contract for formatting, generating, and writing log messages
- **DefaultLoggingStrategy**: The default implementation that handles formatting and writing log messages to files

## Custom Strategies

The strategy pattern used in the logging system allows you to create custom logging behaviors by implementing the LoggingStrategy interface. This is particularly useful for:

- Sending logs to external services (e.g., Sentry, LogStash)
- Filtering logs based on custom criteria
- Formatting logs in special ways for specific components
- Storing logs in databases or other storage systems

### Creating a Custom Strategy

```java
public class DiscordLoggingStrategy implements LoggingStrategy {
    private final WebhookClient webhookClient;
    private String format;
    
    public DiscordLoggingStrategy(String webhookUrl, String format) {
        this.webhookClient = WebhookClient.create(webhookUrl);
        this.format = format;
    }
    
    @Override
    public void format(@NotNull String format) {
        this.format = format;
    }
    
    @Override
    public void log(boolean silent, @NotNull Object... content) {
        Pair<String, TextBuilder> messagePair = generateMessage(content);
        
        // Send to Discord
        webhookClient.send(messagePair.first());
        
        // Also output to console if not silent
        if (!silent && messagePair.second() != null) {
            messagePair.second().sendToConsole();
        }
    }
    
    @Override
    @NotNull
    public Pair<String, TextBuilder> generateMessage(@NotNull Object... content) {
        // Format the message for Discord
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
        // Not used in this strategy as we send to Discord instead
    }
}
```

### Using Multiple Strategies

You can use multiple logging strategies for different purposes:

```java
public class AdvancedLoggingSystem {
    private final NDLogger logger;
    private final LoggingStrategy fileStrategy;
    private final LoggingStrategy discordStrategy;
    private final LoggingStrategy databaseStrategy;
    
    public AdvancedLoggingSystem(JavaPlugin plugin) {
        this.logger = new NDLogger("AdvancedLogger", plugin);
        
        // Standard file logging (already used by NDLogger internally)
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
        
        // Database logging for analytics
        this.databaseStrategy = new DatabaseLoggingStrategy(
            databaseConnection,
            "[%timestamp] %content"
        );
    }
    
    public void logImportantEvent(String event) {
        // Log to all strategies
        logger.log(fileStrategy, false, event);
        logger.log(discordStrategy, false, event);
        logger.log(databaseStrategy, true, event); // Silent for database
    }
    
    public void logAnalyticsOnly(String data) {
        // Log only to database
        logger.log(databaseStrategy, true, data);
    }
}
```

## Best Practices

- Create specialized strategies for different output destinations
- Keep strategy implementations focused on a single responsibility
- Use composition to combine multiple strategies when needed
- Consider performance implications, especially for synchronous operations
- Implement proper error handling in custom strategies
- Use the strategy pattern to extend logging capabilities without modifying NDLogger