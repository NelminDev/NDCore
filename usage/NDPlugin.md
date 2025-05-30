# NDPlugin

## Overview

The NDPlugin class is an abstract base class that extends JavaPlugin and provides additional functionality for plugins built on NDCore. It handles lifecycle management, enhanced logging, and player data handling.

## Key Features

- Enhanced lifecycle management (load, enable, disable)
- Automatic cleanup of offline player data
- Persistent property management
- Enhanced logging with NDLogger
- Player data management
- Command registration with CommandRegistrar

## Components

### Lifecycle Methods

NDPlugin provides a structured lifecycle with clear methods to override:

```java
public class MyPlugin extends NDPlugin {
    @Override
    public void load() {
        // Called during the load phase
        // Use for early initialization that doesn't depend on other plugins
        // Optional, you don't have to overwrite it
        // equivalent to onLoad()
    }

    @Override
    public void enable(Runnable commandRegistrarRunnable, Runnable eventRegistrarRunnable) {
        // Called during the enable phase
        // This is where most of your initialization should happen
        // equivalent to onEnable()
        logger().info("MyPlugin has been enabled!");

        // Execute the runnables which will register commands and events
        // This is handled via runnables, so that you can log messages like "Registering Commands & Events"
        logger().info("Registering commands and event listeners...");
        commandRegistrarRunnable.run();
        eventRegistrarRunnable.run();
        logger().info("Successfully registered commands and event listeners!");
    }

    @Override
    public void registerCommands() {
        // Register your plugin commands here

        // Traditional way using plugin.yml
        getCommand("mycommand").setExecutor(new MyCommandExecutor());

        // Using CommandRegistrar (no plugin.yml needed)
        getCommandRegistrar().register(new MyCustomCommand());
    }

    @Override
    public void registerEvents() {
        // Register your event listeners here
        getServer().getPluginManager().registerEvents(new MyEventListener(), this);
    }

    @Override
    public void disable(Runnable runnable) {
        // Called during the disable phase
        // Clean up resources here
        // equivalent to onDisable()
        logger().info("MyPlugin has been disabled!");

        // Execute the runnable for any final cleanup
        runnable.run();
    }
}
```

### Command Registration

NDPlugin provides a built-in CommandRegistrar for registering commands without using plugin.yml:

```java
public class MyPlugin extends NDPlugin {
    @Override
    public void enable(Runnable commandRegistrarRunnable, Runnable eventRegistrarRunnable) {
        logger().info("Initializing commands...");

        // Execute the runnables which will register commands and events
        commandRegistrarRunnable.run();
        eventRegistrarRunnable.run();
    }

    @Override
    public void registerCommands() {
        // Register a custom command using CommandRegistrar
        getCommandRegistrar().register(new MyCommand())
                .register(new AnotherCommand());
    }

    @Override
    public void registerEvents() {
        // Register event listeners
    }

    // Example custom command
    private static class MyCommand extends NDCommand {
        public MyCommand() {
            super("mycommand", "A custom command", "/mycommand", List.of("mycmd"));
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
            sender.sendMessage("Custom command executed!");
            return true;
        }
    }
}
```

### Enhanced Logging

NDPlugin provides an enhanced logging system through NDLogger:

```java
public class MyPlugin extends NDPlugin {
    @Override
    public void enable(Runnable commandRegistrarRunnable, Runnable eventRegistrarRunnable) {
        // Basic logging
        logger().info("Plugin enabled!");

        // Warning messages
        logger().warn("This is a warning message");

        // Error messages
        logger().error("This is an error message");

        // Fatal messages
        logger().fatal("This is a fatal error message");

        // Silent logging (logs to file but not console)
        logger().infoSilent("This message only goes to the log file");

        // Execute the runnables to register commands and events
        commandRegistrarRunnable.run();
        eventRegistrarRunnable.run();
    }

    @Override
    public void registerCommands() {
        // Register commands
    }

    @Override
    public void registerEvents() {
        // Register event listeners
    }
}
```

### Player Data Management

NDPlugin provides built-in player data management:

```java
public class MyPlugin extends NDPlugin {
    @Override
    public void enable(Runnable commandRegistrarRunnable, Runnable eventRegistrarRunnable) {
        // Main initialization
        logger().info("Initializing player data management...");

        // Execute the runnables to register commands and events
        commandRegistrarRunnable.run();
        eventRegistrarRunnable.run();
    }

    @Override
    public void registerCommands() {
        // No commands for this example
    }

    @Override
    public void registerEvents() {
        // Register player join event to initialize player data
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    private class PlayerListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();

            // Get or create a property manager for the player
            PersistentPropertyManager propertyManager = getPlayerPropertyManagers()
                .computeIfAbsent(player.getUniqueId(), 
                    uuid -> new PersistentPropertyManager(
                        MyPlugin.this, 
                        player.getPersistentDataContainer()
                    )
                );

            // Create a BasicNDPlayer instance
            BasicNDPlayer ndPlayer = new BasicNDPlayer(player, propertyManager);

            // Add to the list of managed players
            getBasicNDPlayers().add(ndPlayer);
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            UUID playerUUID = event.getPlayer().getUniqueId();

            // Remove from the list of managed players
            getBasicNDPlayers().removeIf(p -> p.getUUID().equals(playerUUID));

            // Note: The property manager will be automatically cleaned up by NDPlugin
            // The cleanup happens every 5 minutes via a scheduled task
        }
    }
}
```

### Persistent Property Management

NDPlugin provides built-in persistent property management:

```java
PersistentProperty<Integer> intProperty = propertyManager.create(
        "NAME",
        DEFAULT_VALUE, // The default value for this property
        (prev, cur) -> {
            // Custom logic -> what should happen when someone changes this property via PersistentProperty#set
            // You have the previous (prev) and current (cur) value
            // You can do anything you want.
        }
);
```

## Complete Example

Here's a complete example of a plugin that extends NDPlugin:

```java
public class MyPlugin extends NDPlugin {
    private final Map<UUID, BasicNDPlayer> players = new ConcurrentHashMap<>(512);

    @Override
    public void load() {
        // Early initialization
        logger().info("Loading configuration...");
        saveDefaultConfig();
    }

    @Override
    public void enable(Runnable commandRegistrarRunnable, Runnable eventRegistrarRunnable) {
        // Main initialization
        logger().info("Initializing MyPlugin...");

        // Execute the runnables which will register commands and events
        commandRegistrarRunnable.run();
        eventRegistrarRunnable.run();

        logger().info("MyPlugin has been enabled!");
    }

    @Override
    public void registerCommands() {
        // Register commands
        getCommand("hello").setExecutor((sender, command, label, args) -> {
            sender.sendMessage("Hello from MyPlugin!");
            return true;
        });
    }

    @Override
    public void registerEvents() {
        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void disable(Runnable runnable) {
        // Cleanup
        logger().info("Saving player data...");

        // Clear player data
        players.clear();

        // Execute the runnable for any final cleanup
        runnable.run();

        logger().info("MyPlugin has been disabled!");
    }

    private class PlayerListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();

            // Create a BasicNDPlayer instance
            BasicNDPlayer ndPlayer = BasicNDPlayer.of(player, MyPlugin.this);
            players.put(player.getUniqueId(), ndPlayer);

            // Send welcome message
            LocalizedMessage welcomeMessage = new LocalizedMessage()
                .add(LanguageCode.ENGLISH, "Welcome to the server!")
                .add(LanguageCode.GERMAN, "Willkommen auf dem Server!");

            ndPlayer.sendLocalizedMessage(welcomeMessage, e -> {
                logger().error("Error sending welcome message", e);
            });
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            players.remove(event.getPlayer().getUniqueId());
        }
    }
}
```

## Relationship with NDCore

NDPlugin is the base class that NDCore extends. While NDCore is the concrete implementation that runs as a plugin, NDPlugin provides the framework that both NDCore and your custom plugins can use. This separation allows for:

1. **Consistent Plugin Structure**: All plugins built on NDCore follow the same lifecycle and structure
2. **Shared Functionality**: Common features like logging and player data management are available to all plugins
3. **Simplified Development**: You don't need to reimplement common functionality in each plugin

When you create a plugin that depends on NDCore, you'll typically extend NDPlugin rather than JavaPlugin directly, giving you access to all the enhanced features.

## Best Practices

- Always override the `enable(Runnable, Runnable)` and `disable(Runnable)` methods, not `onEnable()` and `onDisable()`
- Implement the abstract methods `registerCommands()` and `registerEvents()` for proper organization
- Use the enhanced `logger()` method instead of the deprecated `getLogger()`
- Use the CommandRegistrar for registering commands without plugin.yml entries
- Take advantage of the automatic cleanup of offline player data (runs every 5 minutes)
- Use pre-sized collections for better performance (e.g., `new ConcurrentHashMap<>(512)`)
- Use the built-in persistent property management for player data
- Implement proper error handling in your plugins
- Follow the lifecycle methods for proper initialization and cleanup
- Use BasicNDPlayer for enhanced player functionality
- Use `@NotNull` annotations for null safety
- Use `Objects.requireNonNull()` for defensive programming
