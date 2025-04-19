# FileConfiguration

## Overview

`FileConfiguration` is an abstract base class that extends Bukkit's `FileConfiguration` to provide enhanced error
handling and validation for configuration values. It serves as the foundation for specific file format implementations
like `JSONConfiguration`.

## Class Declaration

```java
public abstract class FileConfiguration extends org.bukkit.configuration.file.FileConfiguration
```

## Key Features

- Robust error handling with getOrThrow methods
- Validation for empty values with ConfigurationValueNullOrEmptyException
- Type-safe value retrieval with proper null checking
- Consistent error messaging

## Methods

### Value Retrieval Methods with Exception Handling

These methods provide robust error handling by throwing exceptions when values are null, empty, or invalid.

#### getOrThrow

```java
public @NotNull Object getOrThrow(@NotNull String key) throws NullPointerException
```

Gets any value for a given key or throws an exception if not valid.

**Parameters:**

- `key` - The key to get the value for

**Returns:** The value object, never null

**Throws:** `NullPointerException` if key is null or value is null

#### getStringOrThrow

```java
public @NotNull String getStringOrThrow(@NotNull String key) throws NullPointerException, ConfigurationValueNullOrEmptyException
```

Gets a string value for a given key or throws exceptions if not valid.

**Parameters:**

- `key` - The key to get the value for

**Returns:** The non-empty string value

**Throws:**

- `NullPointerException` if key is null or value is null
- `ConfigurationValueNullOrEmptyException` if value is empty or blank

#### getIntOrThrow

```java
public @NotNull Integer getIntOrThrow(@NotNull String key) throws NullPointerException
```

Gets an integer value for a given key or throws an exception if not valid.

**Parameters:**

- `key` - The key to get the value for

**Returns:** The integer value, never null

**Throws:** `NullPointerException` if key is null or value is null

#### getBooleanOrThrow

```java
public @NotNull Boolean getBooleanOrThrow(@NotNull String key) throws NullPointerException
```

Gets a boolean value for a given key or throws an exception if not valid.

**Parameters:**

- `key` - The key to get the value for

**Returns:** The boolean value, never null

**Throws:** `NullPointerException` if key is null or value is null

#### getDoubleOrThrow

```java
public @NotNull Double getDoubleOrThrow(@NotNull String key) throws NullPointerException
```

Gets a double value for a given key or throws an exception if not valid.

**Parameters:**

- `key` - The key to get the value for

**Returns:** The double value, never null

**Throws:** `NullPointerException` if key is null or value is null

#### getLongOrThrow

```java
public @NotNull Long getLongOrThrow(@NotNull String key) throws NullPointerException
```

Gets a long value for a given key or throws an exception if not valid.

**Parameters:**

- `key` - The key to get the value for

**Returns:** The long value, never null

**Throws:** `NullPointerException` if key is null or value is null

#### getListOrThrow

```java
public @NotNull List<?> getListOrThrow(@NotNull String key) throws ConfigurationValueNullOrEmptyException, NullPointerException
```

Gets a list value for a given key or throws exceptions if not valid.

**Parameters:**

- `key` - The key to get the value for

**Returns:** The non-empty list value

**Throws:**

- `NullPointerException` if key is null or value is null
- `ConfigurationValueNullOrEmptyException` if the list is empty

## Example Usage

```java
// Using FileConfiguration through a concrete implementation like JSONConfiguration
JSONConfiguration config = JSONConfiguration.loadConfiguration(configFile);

try{
// Get values with robust error handling
String serverName = config.getStringOrThrow("server.name");
int maxPlayers = config.getIntOrThrow("server.max-players");
boolean enableFeature = config.getBooleanOrThrow("features.new-feature");
List<?> allowedWorlds = config.getListOrThrow("worlds.allowed");

// Process the configuration values
    System.out.

println("Server name: "+serverName);
    System.out.

println("Max players: "+maxPlayers);
    System.out.

println("Feature enabled: "+enableFeature);
    System.out.

println("Allowed worlds: "+allowedWorlds);
}catch(
NullPointerException e){
        // Handle missing values
        System.err.

println("Missing required configuration value: "+e.getMessage());
        }catch(
ConfigurationValueNullOrEmptyException e){
        // Handle empty values
        System.err.

println("Empty configuration value: "+e.getMessage());
        }
```

## Best Practices

- Use the getOrThrow methods when configuration values are required for your application to function correctly
- Catch exceptions separately to provide specific error messages for different types of configuration issues
- Consider providing default values for non-critical configuration options using the standard get methods
- Use try-catch blocks to handle configuration errors gracefully and provide user-friendly error messages