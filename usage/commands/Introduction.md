# Commands Package

The commands package provides a streamlined way to create and register custom commands in your Bukkit/Spigot plugin. It
simplifies the process of command registration by handling the reflection-based access to the server's command map.

## Key Components

### NDCommand

`NDCommand` is an abstract class that extends Bukkit's `Command` class. It serves as a base for all custom commands in
your plugin, providing:

- A consistent interface for command implementation
- Multiple constructors for different initialization scenarios
- Integration with the CommandRegistrar for easy registration

### CommandRegistrar

`CommandRegistrar` handles the registration of custom commands with the Bukkit server. It:

- Uses reflection to access the server's command map
- Provides a fluent API for registering multiple commands
- Includes proper cleanup methods to prevent memory leaks

## Usage Example

```java
// Create a custom command
public class MyCommand extends NDCommand {

    public MyCommand() {
        super("mycommand", "A custom command", "/mycommand", List.of("mycmd", "mc"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        sender.sendMessage("Command executed!");
        return true;
    }
}

// Register the command in your plugin
public class MyPlugin extends NDPlugin {

    @Override
    public void enable(Runnable commandRegistrarRunnable, Runnable eventRegistrarRunnable) {
        // Execute the runnables which will register commands and events
        commandRegistrarRunnable.run();
        eventRegistrarRunnable.run();

        // Register commands
        getCommandRegistrar().register(new MyCommand());
    }

    @Override
    public void disable(Runnable runnable) {
        runnable.run();
    }
}
```

## Best Practices

- Always store a reference to the CommandRegistrar in your plugin class
- Call the cleanup() method when your plugin is disabled
- Create separate command classes for different functionality
- Use the fluent API for registering multiple commands: `registrar.register(cmd1).register(cmd2)`
