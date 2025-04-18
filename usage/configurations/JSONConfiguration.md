# JSONConfiguration

## Overview

`JSONConfiguration` is a specialized implementation of Bukkit's `FileConfiguration` that provides support for
JSON-formatted configuration files. It allows plugin developers to use JSON as an alternative to the default YAML format
for configuration storage.

## Class Declaration

```java
public class JSONConfiguration extends FileConfiguration
```

## Key Features

- Load and save configuration data in JSON format
- Convert between Bukkit's ConfigurationSection objects and JSON structures
- Control JSON formatting with pretty printing options
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
public JSONConfigurationOptions options()
```

Gets the options for this configuration.

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

### JSONConfigurationOptions

Extends `FileConfigurationOptions` to provide JSON-specific configuration options.

#### Methods

##### prettyPrinting(boolean)

```java
public JSONConfigurationOptions prettyPrinting(boolean value)
```

Sets whether the JSON output should be formatted with pretty printing.

##### prettyPrinting()

```java
public boolean prettyPrinting()
```

Gets whether pretty printing is enabled.

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