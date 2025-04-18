# NDCommand

The `NDCommand` class is an abstract base class that extends Bukkit's `Command` class. It provides a foundation for
creating custom commands in your plugin with a simplified registration process when used with the `CommandRegistrar`.

## Constructors

### Default Constructor

```java
public NDCommand()
```

Creates a new NDCommand with an empty name. This constructor is primarily used when subclasses will set the name and
other properties in their own constructors.

### Name Constructor

```java
private NDCommand(@NotNull String name)
```

Creates a new NDCommand with the specified name.

**Parameters:**

- `name` - The name of the command (cannot be null)

### Full Constructor

```java
private NDCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases)
```

Creates a new NDCommand with the specified name, description, usage message, and aliases.

**Parameters:**

- `name` - The name of the command (cannot be null)
- `description` - The description of the command (cannot be null)
- `usageMessage` - The usage message of the command (cannot be null)
- `aliases` - A list of command aliases (cannot be null)

## Usage Example

To create a custom command, extend the NDCommand class and implement the required methods:

```java
public class TeleportCommand extends NDCommand {

    public TeleportCommand() {
        super("teleport", "Teleport to a location", "/teleport <x> <y> <z>", List.of("tp", "tele"));
    }
    // OR
    public TeleportCommand() {
        super("teleport");
        setDescription("Teleport to a location");
        ...
    }
    
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players");
            return true;
        }
        
        if (args.length < 3) {
            sender.sendMessage(getUsage());
            return false;
        }
        
        try {
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);
            
            player.teleport(new Location(player.getWorld(), x, y, z));
            player.sendMessage("Teleported to " + x + ", " + y + ", " + z);
            return true;
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid coordinates");
            return false;
        }
    }
    
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return List.of();
        }
        
        if (args.length <= 3) {
            Location loc = player.getLocation();
            return switch (args.length) {
                case 1 -> List.of(String.valueOf(loc.getBlockX()));
                case 2 -> List.of(String.valueOf(loc.getBlockY()));
                case 3 -> List.of(String.valueOf(loc.getBlockZ()));
                default -> List.of();
            };
        }
        
        return List.of();
    }
}
```

## Registration

To register your custom command, use the `CommandRegistrar`:

```java
// In your plugin's enable method
CommandRegistrar registrar = new CommandRegistrar(this);
registrar.

register(new TeleportCommand());
```

## Best Practices

- Override both `execute()` and `tabComplete()` methods for a complete command implementation
- Use the full constructor to provide comprehensive command information
- Implement proper permission checks in your execute method
- Provide helpful error messages when command usage is incorrect
- Use tab completion to guide users with valid input options