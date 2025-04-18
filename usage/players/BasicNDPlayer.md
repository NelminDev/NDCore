# BasicNDPlayer

## Overview

A wrapper class for Bukkit's Player that adds persistent property management and localized messaging capabilities in NDCore.

## Key Features

- Persistent property management for player data
- Language preference tracking
- Localized message sending
- Player freeze/unfreeze functionality with events
- Integration with NDPlugin system
- Lombok @Getter annotations for cleaner code
- Public setupProperties method for customization

## Usage Examples

```java
// Creating a BasicNDPlayer instance
public BasicNDPlayer getOrCreatePlayer(Player player) {
    return BasicNDPlayer.of(player, this); // 'this' refers to your NDPlugin instance
}

// Using a BasicNDPlayer to send localized messages
public void sendWelcomeMessage(Player player) {
    BasicNDPlayer ndPlayer = getOrCreatePlayer(player);

    // Create a localized message
    LocalizedMessage welcomeMessage = new LocalizedMessage()
        .add(LanguageCode.ENGLISH, "Welcome to the server!")
        .add(LanguageCode.GERMAN, "Willkommen auf dem Server!")
        .add(LanguageCode.FRENCH, "Bienvenue sur le serveur!")
        .add(LanguageCode.SPANISH, "¡Bienvenido al servidor!");

    // Send the message in the player's preferred language
    ndPlayer.sendLocalizedMessage(welcomeMessage, e -> {
        getLogger().severe("Error sending localized message: " + e.getMessage());
    });
}

// Changing a player's language preference
public void setPlayerLanguage(Player player, String languageCode) {
    BasicNDPlayer ndPlayer = getOrCreatePlayer(player);

    try {
        // Convert string to LanguageCode enum
        LanguageCode language = LanguageCode.valueOf(languageCode.toUpperCase());

        // Update the player's language preference
        ndPlayer.getLanguageCode().set(language.get(), () -> {
            player.sendMessage("Language set to: " + language.name());
        }, e -> {
            getLogger().severe("Error setting player language: " + e.getMessage());
        });
    } catch (IllegalArgumentException e) {
        player.sendMessage("Invalid language code: " + languageCode);
    }
}

// Freezing and unfreezing a player
public void freezePlayer(Player player, boolean freeze) {
    BasicNDPlayer ndPlayer = getOrCreatePlayer(player);

    ndPlayer.getIsFrozen().set(freeze, () -> {
        if (freeze) {
            player.sendMessage("You have been frozen!");
        } else {
            player.sendMessage("You have been unfrozen!");
        }
    }, e -> {
        getLogger().severe("Error " + (freeze ? "freezing" : "unfreezing") + " player: " + e.getMessage());
    });
}

// Checking if a player is frozen
public boolean isPlayerFrozen(Player player) {
    BasicNDPlayer ndPlayer = getOrCreatePlayer(player);

    return ndPlayer.getIsFrozen().get(e -> {
        getLogger().severe("Error checking if player is frozen: " + e.getMessage());
    });
}
```

## Extending BasicNDPlayer

You can extend BasicNDPlayer to add custom properties by overriding the setupProperties method:

```java
public class CustomPlayer extends BasicNDPlayer {
    @Getter private PersistentProperty<Integer> coins;
    @Getter private PersistentProperty<Boolean> isVIP;

    public CustomPlayer(Player bukkitPlayer, PersistentPropertyManager propertyManager) {
        super(bukkitPlayer, propertyManager);
    }

    @Override
    public void setupProperties() {
        // Call the parent method to set up the default properties
        super.setupProperties();

        // Add custom properties
        this.coins = propertyManager.create(
            "coins",
            0,
            (prev, cur) -> {
                if (cur < 0) return 0; // Ensure coins can't be negative
                return cur;
            }
        );

        this.isVIP = propertyManager.create(
            "isVIP",
            false
        );
    }

    // Custom methods for your extended player class
    public void addCoins(int amount) {
        coins.set(coins.get() + amount);
    }

    public void removeCoins(int amount) {
        coins.set(Math.max(0, coins.get() - amount));
    }

    public boolean canAfford(int cost) {
        return coins.get() >= cost;
    }
}
```

## Managing Player Collections

When working with multiple players, you'll typically want to maintain a collection of BasicNDPlayer instances:

```java
public class PlayerManager {
    private final Map<UUID, BasicNDPlayer> players = new HashMap<>();
    private final NDPlugin plugin;

    public PlayerManager(NDPlugin plugin) {
        this.plugin = plugin;

        // Register event listeners for player join/quit
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), plugin);
    }

    public BasicNDPlayer getPlayer(Player player) {
        return players.computeIfAbsent(
            player.getUniqueId(), 
            uuid -> BasicNDPlayer.of(player, plugin)
        );
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    public Collection<BasicNDPlayer> getAllPlayers() {
        return Collections.unmodifiableCollection(players.values());
    }

    // Example inner class for handling player events
    private class PlayerListener implements Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            BasicNDPlayer ndPlayer = getPlayer(player);

            // Send welcome message
            LocalizedMessage welcomeMessage = new LocalizedMessage()
                .add(LanguageCode.ENGLISH, "Welcome to the server!")
                .add(LanguageCode.GERMAN, "Willkommen auf dem Server!");

            ndPlayer.sendLocalizedMessage(welcomeMessage, e -> {
                plugin.getLogger().severe("Error: " + e.getMessage());
            });
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event) {
            removePlayer(event.getPlayer().getUniqueId());
        }
    }
}
```

## Best Practices

- Create BasicNDPlayer instances on player join and store them in a map
- Remove BasicNDPlayer instances on player quit to prevent memory leaks
- Use the player's language preference for all messages
- Handle errors in the error handler consumer
- Consider caching frequently accessed player data
- Use the freeze/unfreeze functionality with appropriate events
- Implement custom player properties by extending BasicNDPlayer
- Ensure thread safety when accessing player collections from async tasks
- Override the public setupProperties method when extending BasicNDPlayer to add custom properties
- Take advantage of Lombok's @Getter annotations for cleaner code
