# Configurations Package

## Overview

The `configurations` package provides utilities for managing configuration data in various formats for NDCore plugins.
It offers a structured way to load, manipulate, and save configuration data, making it easier to handle plugin settings
and data persistence.

## Key Components

### JSONConfiguration

The primary class in this package is `JSONConfiguration`, which extends Bukkit's `FileConfiguration` to provide
JSON-specific functionality:

- **Loading and Saving**: Methods to load JSON from files or readers and save configuration data as JSON
- **Conversion Utilities**: Tools to convert between Bukkit's ConfigurationSection objects and JSON structures
- **Pretty Printing**: Options to format JSON output for better readability

### JSONConfigurationOptions

An inner class of `JSONConfiguration` that provides configuration options specific to JSON:

- **Pretty Printing Control**: Enable or disable formatted JSON output
- **Path Separator Configuration**: Customize the character used to separate paths in the configuration

## Common Use Cases

- Loading plugin settings from JSON files
- Saving structured data in a human-readable JSON format
- Converting between Bukkit's configuration sections and JSON objects
- Working with web APIs that use JSON
- Maintaining configuration files that need to be both human-readable and machine-parseable

## Integration with Other Packages

The configurations package works closely with:

- Core plugin initialization to load settings
- Other components that need to store or retrieve configuration data

## Example Usage

```java
// Load a JSON configuration file
File configFile = new File(plugin.getDataFolder(), "config.json");
JSONConfiguration config = JSONConfiguration.loadConfiguration(configFile);

// Access configuration values
String serverName = config.getString("server.name");
int maxPlayers = config.getInt("server.max-players");

// Modify configuration
config.

set("server.name","My Awesome Server");
config.

set("server.max-players",100);

// Enable pretty printing
config.

options().

prettyPrinting(true);

// Save changes
try{
        config.

save(configFile);
}catch(
IOException e){
        plugin.

getLogger().

severe("Could not save configuration: "+e.getMessage());
        }
```
