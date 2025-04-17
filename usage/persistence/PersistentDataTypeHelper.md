# PersistentDataTypeHelper

## Overview

A helper class for managing PersistentDataType conversions using the Singleton pattern in NDCore.

## Key Features

- Automatic conversion between primitive and complex types
- Singleton pattern for efficient reuse
- Support for all standard Bukkit PersistentDataTypes

## Usage Examples

```java
// Getting the appropriate PersistentDataType for a class
public <T> PersistentDataType<?, T> getDataType(Class<T> clazz) {
    return PersistentDataTypeHelper.instance().type(clazz);
}

// Using the helper directly with a PersistentDataContainer
public void storeCustomData(PersistentDataContainer container, String key, Object value) {
    NamespacedKey namespacedKey = new NamespacedKey(this, key);
    
    if (value instanceof Integer intValue) {
        PersistentDataType<?, Integer> type = PersistentDataTypeHelper.instance().type(Integer.class);
        container.set(namespacedKey, type, intValue);
    } else if (value instanceof String stringValue) {
        container.set(namespacedKey, PersistentDataType.STRING, stringValue);
    } else if (value instanceof Boolean boolValue) {
        PersistentDataType<?, Boolean> type = PersistentDataTypeHelper.instance().type(Boolean.class);
        container.set(namespacedKey, type, boolValue);
    }
    // Add more type handling as needed
}
```

## Best Practices

- Use the helper to simplify working with different data types in PersistentDataContainers
- Take advantage of the singleton pattern to avoid creating multiple instances
- Use with PersistentProperty for a more complete persistence solution
- Consider the performance impact of frequent property access