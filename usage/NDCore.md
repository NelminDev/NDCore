# NDCore

## Overview

The NDCore class is the main plugin class that provides the core functionality of the NDCore library. It extends NDPlugin and serves as the entry point for the NDCore plugin.

## Key Features

- Initializes the core plugin functionality
- Registers event listeners
- Provides enhanced logging
- Manages player data persistence
- Handles cleanup of offline players

## Implementation Details

NDCore is a concrete implementation of NDPlugin that provides the following functionality:

```java
@Getter
public final class NDCore extends NDPlugin {

    @Override
    public void enable(Runnable commandRegistrarRunnable, Runnable eventRegistrarRunnable) {
        super.logger(new NDLogger("NDCore", this));

        Objects.requireNonNull(logger(), "logger cannot be null");
        Objects.requireNonNull(getPluginMeta(), "plugin meta cannot be null");
        Objects.requireNonNull(getServer(), "server cannot be null");

        logger().info("Loading NDCore", getPluginMeta().getVersion(), "by NelminDev");
        logger().info("Registering event listeners and commands...");
        commandRegistrarRunnable.run();
        eventRegistrarRunnable.run();
        logger().info("Successfully registered event listeners and commands!");

        logger().info("Successfully loaded NDCore!");
    }

    @Override
    public void disable(Runnable runnable) {
        Objects.requireNonNull(logger(), "logger cannot be null");
        logger().info("Disabling NDCore...");
        logger().info("Goodbye, have a nice day! - NelminDev");
    }

    @Override
    public void registerCommands() {
        // NDCore doesn't register any commands by default
    }

    @Override
    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
    }
}
```

The NDCore plugin:

1. Initializes the logger with the plugin name
2. Registers the InventoryClickListener to handle menu interactions
3. Provides cleanup on disable

## Usage

NDCore is designed to be used as a dependency for other plugins. You don't need to interact with the NDCore class directly in most cases. Instead, you'll extend the NDPlugin class to create your own plugins that leverage NDCore's functionality.

```java
// NDCore is loaded automatically as a plugin dependency
// You don't need to instantiate or interact with it directly
```

## Integration with plugin.yml

To use NDCore as a dependency, add it to your plugin.yml file:

```yaml
name: MyPlugin
version: 1.0.0
main: com.example.myplugin.MyPlugin
api-version: 1.21
depend: [NDCore]  # This makes NDCore a required dependency
```

## Automatic Player Data Management

NDCore automatically manages player data cleanup to prevent memory leaks:

1. It maintains a map of player property managers
2. It schedules a task to run every 5 minutes to clean up data for offline players
3. It clears all player data when the plugin is disabled

This ensures that your server remains efficient even with many players joining and leaving.

## Event Listeners

NDCore registers the following event listeners:

- **InventoryClickListener**: Handles clicks in custom menu inventories, allowing for interactive GUIs

## Best Practices

- Add NDCore as a dependency in your plugin.yml
- Extend NDPlugin rather than directly interacting with NDCore
- Use the provided utilities like NDLogger, PersistentPropertyManager, and Menu system
- Let NDCore handle the cleanup of offline player data
- Refer to the NDPlugin usage guide for details on extending the base class
