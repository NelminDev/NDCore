# LoggingStrategy

## Overview

An interface that defines the contract for formatting, generating, and writing log messages in the NDCore logging system.

## Key Features

- Customizable message formatting
- Separate file and console output handling
- Extensible for custom logging behaviors

## Usage Examples

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

## Best Practices

- Create specialized strategies for different output destinations
- Keep strategy implementations focused on a single responsibility
- Use composition to combine multiple strategies when needed
- Consider performance implications, especially for synchronous operations
- Implement proper error handling in custom strategies