# DateTime

## Overview

A utility class for handling date and time operations with formatting capabilities in NDCore.

## Key Features

- Multiple constructors for different time sources
- Formatting methods for dates and times
- Custom pattern formatting
- Conversion between different date/time representations

## Usage Examples

```java
// Creating DateTime instances
DateTime now = new DateTime(); // Current time
DateTime fromInstant = new DateTime(Instant.now());
DateTime fromLocalDateTime = new DateTime(LocalDateTime.now());
DateTime fromTimestamp = new DateTime(System.currentTimeMillis());

// Formatting dates and times
String timeString = now.formatTime(); // "14:30:45.123"
String dateString = now.formatDate(); // "25-04-2025"

// Getting both date and time
Pair<String, String> dateAndTime = now.dateTime();
String date = dateAndTime.first();
String time = dateAndTime.second();

// Custom format patterns
String customFormat = now.format("yyyy-MM-dd HH:mm:ss"); // "2025-04-25 14:30:45"

// Using with custom formatters
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
String longDate = now.format(formatter); // "Friday, April 25, 2025"

// Getting the LocalDateTime
LocalDateTime localDateTime = now.localDateTime();

// Using in logging
logger.info("Operation completed at " + new DateTime().formatTime());

// Calculating time differences
DateTime start = new DateTime();
// ... some operation
DateTime end = new DateTime();
long durationMs = end.getInstant().toEpochMilli() - start.getInstant().toEpochMilli();
logger.info("Operation took " + durationMs + "ms");
```

## Best Practices

- Use DateTime for consistent date and time formatting across your plugin
- Use the appropriate constructor based on your time source
- Take advantage of the built-in formatting methods for common formats
- Use custom patterns when you need specific date/time formats
- Consider using DateTime for performance measurements and logging timestamps