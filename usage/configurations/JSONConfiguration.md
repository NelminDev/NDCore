# JSONConfiguration

## Overview

`JSONConfiguration` is a specialized implementation of Bukkit's `FileConfiguration` that provides support for
JSON-formatted configuration files. It allows plugin developers to use JSON as an alternative to the default YAML format
for configuration storage.

## Class Declaration

```java
public class JSONConfiguration extends dev.nelmin.ndcore.configurations.FileConfiguration
```

## Key Features

- Load and save configuration data in JSON format
- Convert between Bukkit's ConfigurationSection objects and JSON structures
- Control JSON formatting with pretty printing options
- Robust error handling with getOrThrow methods
- Validation for empty values with ConfigurationValueNullOrEmptyException
- Seamless integration with Bukkit's configuration API

## Properties

| Property | Type   | Description                                                              |
|----------|--------|--------------------------------------------------------------------------|
| `gson`   | `Gson` | The Google Gson instance used for JSON serialization and deserialization |

## Methods

### Constructor

```java
public JSONConfiguration()
```

Creates a new JSONConfiguration with default settings.

### Core Methods

#### saveToString

```java
@NotNull
@Override
public String saveToString()
```

Converts the configuration to a JSON string.

#### loadFromString

```java
@Override
public void loadFromString(@NotNull String contents) throws InvalidConfigurationException
```

Loads the configuration from a JSON string.

#### options

```java
@Override
@NotNull
public JSONConfiguration.Options options()
```

Gets the options for this configuration.

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

### Static Factory Methods

#### loadConfiguration (File)

```java
public static @NotNull JSONConfiguration loadConfiguration(@NotNull File file)
```

Loads a JSON configuration from a file.

**Parameters:**

- `file` - The file to load from

**Returns:** A new JSONConfiguration loaded from the specified file

#### loadConfiguration (Reader)

```java
public static @NotNull JSONConfiguration loadConfiguration(@NotNull Reader reader)
```

Loads a JSON configuration from a reader.

**Parameters:**

- `reader` - The reader to load from

**Returns:** A new JSONConfiguration loaded from the specified reader

## Inner Classes

### Options

Extends `FileConfigurationOptions` to provide JSON-specific configuration options.

#### Methods

##### prettyPrinting(boolean)

```java
public Options prettyPrinting(boolean value)
```

Sets whether the JSON output should be formatted with pretty printing.

##### prettyPrinting()

```java
public boolean prettyPrinting()
```

Gets whether pretty printing is enabled.

##### configuration()

```java

@Override
@NotNull
public JSONConfiguration configuration()
```

Gets the configuration instance.

##### copyDefaults(boolean)

```java

@Override
@NotNull
public JSONConfiguration.Options copyDefaults(boolean value)
```

Sets whether to copy defaults.

##### pathSeparator(char)

```java

@Override
@NotNull
public JSONConfiguration.Options pathSeparator(char value)
```

Sets the path separator character.

## Example Usage

### Basic Configuration Loading and Saving

```java
// Load configuration from a file
File configFile = new File(plugin.getDataFolder(), "config.json");
JSONConfiguration config = JSONConfiguration.loadConfiguration(configFile);

// Access values
String serverName = config.getString("server.name", "Default Server");
int maxPlayers = config.getInt("server.max-players", 20);

// Modify values
config.

set("server.name","My JSON Server");
config.

set("server.max-players",50);

// Save with pretty printing
config.

options().

prettyPrinting(true);
try{
        config.

save(configFile);
}catch(
IOException e){
        e.

printStackTrace();
}
```

### Creating a New Configuration

```java
// Create a new configuration
JSONConfiguration config = new JSONConfiguration();

// Add some nested values
config.

set("database.host","localhost");
config.

set("database.port",3306);
config.

set("database.credentials.username","admin");

// Save to string
String jsonString = config.saveToString();
System.out.

println(jsonString);
```

## Notes

- JSON configurations support all the same operations as YAML configurations in Bukkit
- The implementation uses Google's Gson library for JSON processing
- When loading invalid JSON, an InvalidConfigurationException will be thrown
