# CommandRegistrar

The `CommandRegistrar` class provides a mechanism for registering custom commands with the Bukkit server without using
plugin.yml entries. It uses reflection to access the server's command map and register commands at runtime.

## Constructor

```java
public CommandRegistrar(NDPlugin plugin)
```

Creates a new CommandRegistrar instance.

**Parameters:**

- `plugin` - The NDPlugin instance that will be used for logging errors

**Example:**

```java
CommandRegistrar registrar = new CommandRegistrar(myPlugin);
```

## Methods

### register

```java
public CommandRegistrar register(NDCommand command)
```

Registers a command with the Bukkit server's command map.

**Parameters:**

- `command` - The NDCommand instance to register

**Returns:**

- The CommandRegistrar instance for method chaining

**Example:**

```java
// getCommandRegistrar() is available in NDPlugin

// Register a single command
getCommandRegistrar().register(new MyCommand());

// Register multiple commands using method chaining
getCommandRegistrar().register(new FirstCommand())
         .register(new SecondCommand())
         .register(new ThirdCommand());
```

### cleanup

```java
public void cleanup()
```

Cleans up resources used by the CommandRegistrar to prevent memory leaks. This method should be called when your plugin
is being disabled.

**Example:**

```java
@Override
public void disable(Runnable runnable) {
    // Clean up the command registrar
    if (commandRegistrar != null) {
        commandRegistrar.cleanup();
    }

    runnable.run();
}
```

## Implementation Details

The CommandRegistrar uses reflection to access the Bukkit server's internal command map:

1. It retrieves the `commandMap` field from the Bukkit server class
2. It makes the field accessible using reflection
3. It gets the CommandMap instance from the server
4. It uses this CommandMap to register commands

If reflection fails, an error is logged using the plugin's logger.

## Best Practices

- Always store a reference to your CommandRegistrar in your plugin class
- Call the cleanup() method when your plugin is disabled to prevent memory leaks
- Use the fluent API (method chaining) when registering multiple commands
- Handle potential errors by checking if the CommandRegistrar was successfully initialized
