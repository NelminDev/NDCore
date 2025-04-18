# Integration Guide

## Overview

This guide explains how to integrate NDCore into your Minecraft plugin project. NDCore serves as a foundation for plugin development, providing essential utilities and functionality that streamline the development process.

## Key Features

- Zero configuration required
- Automatic resource management
- Enhanced player data handling
- Persistent data management
- Advanced text formatting
- Comprehensive localization support
- Flexible menu system
- Enhanced logging capabilities

## Integration Steps

### 1. Add NDCore as a Dependency

#### Maven

Add the following to your `pom.xml`:

```xml
<dependencies>
    <!-- NDCore dependency -->
    <dependency>
        <groupId>dev.nelmin.minecraft</groupId>
        <artifactId>core-paper</artifactId>
        <version>{VERSION}</version>
        <scope>provided</scope>
    </dependency>

    <!-- Other dependencies -->
</dependencies>
```

#### Gradle

Add the following to your `build.gradle`:

```gradle
dependencies {
    implementation 'dev.nelmin.minecraft:core-paper:{VERSION}'
    // Other dependencies
}
```

#### Gradle (Kotlin DSL)

Add the following to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("dev.nelmin.minecraft:core-paper:{VERSION}")
    // Other dependencies
}
```

### 2. Configure plugin.yml

Add NDCore as a dependency in your `plugin.yml`:

```yaml
name: YourPluginName
version: 1.0
main: com.example.yourplugin.YourPlugin
api-version: 1.21
authors: [YourName]
description: Your plugin description

# Add NDCore as a dependency
depend: [NDCore]
# Or as a soft dependency if your plugin can function without it
# softdepend: [NDCore]
```

### 3. Create Your Plugin Class

Extend the [`NDPlugin`](../src/main/java/dev/nelmin/ndcore/NDPlugin.java) class instead of JavaPlugin:

```java
package com.example.yourplugin;

import dev.nelmin.ndcore.NDPlugin;
import dev.nelmin.ndcore.persistence.PersistentPropertyManager;
import dev.nelmin.ndcore.players.BasicNDPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class YourPlugin extends NDPlugin {
    private final Map<UUID, PersistentPropertyManager> propertyManagers = new HashMap<>();
    private final List<BasicNDPlayer> players = new ArrayList<>();

    @Override
    public void enable() {
        // Plugin initialization code
        logger().info("YourPlugin has been enabled!");

        // Register commands, listeners, etc.
        getServer().getPluginManager().registerEvents(new YourEventListener(this), this);
    }

    @Override
    public void disable() {
        // Cleanup code
        logger().info("YourPlugin has been disabled!");

        // Clear collections
        propertyManagers.clear();
        players.clear();
    }

    @Override
    public @NotNull List<BasicNDPlayer> getBasicNDPlayers() {
        return players;
    }

    @Override
    public @NotNull Map<UUID, PersistentPropertyManager> getPlayerPropertyManagers() {
        return propertyManagers;
    }
}
```

### 4. Initialize Player Data

Create a listener to initialize player data when they join:

```java
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

import dev.nelmin.ndcore.persistence.PersistentPropertyManager;
import dev.nelmin.ndcore.players.BasicNDPlayer;

import java.util.UUID;

public class YourEventListener implements Listener {
    private final YourPlugin plugin;

    public YourEventListener(YourPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Create property manager for the player
        PersistentPropertyManager propertyManager = new PersistentPropertyManager(
            plugin, 
            player.getPersistentDataContainer()
        );

        // Store the property manager
        plugin.getPlayerPropertyManagers().put(playerUUID, propertyManager);

        // Create and store BasicNDPlayer
        BasicNDPlayer ndPlayer = new BasicNDPlayer(player, propertyManager);
        plugin.getBasicNDPlayers().add(ndPlayer);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        // Remove player data
        plugin.getBasicNDPlayers().removeIf(p -> p.getUUID().equals(playerUUID));
        // Property managers will be cleaned up automatically by NDPlugin
    }
}
```

## Complete Example

Here's a complete example of a simple plugin that uses NDCore:

```java
package com.example.simpleplugin;

import dev.nelmin.ndcore.NDPlugin;
import dev.nelmin.ndcore.builders.TextBuilder;
import dev.nelmin.ndcore.persistence.PersistentProperty;
import dev.nelmin.ndcore.persistence.PersistentPropertyManager;
import dev.nelmin.ndcore.players.BasicNDPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SimplePlugin extends NDPlugin implements Listener {
    private final Map<UUID, PersistentPropertyManager> propertyManagers = new HashMap<>();
    private final List<BasicNDPlayer> players = new ArrayList<>();

    @Override
    public void enable() {
        logger().info("SimplePlugin enabled!");

        // Register this class as a listener
        getServer().getPluginManager().registerEvents(this, this);

        // Register command
        getCommand("welcome").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player player) {
                new TextBuilder("&aWelcome to the server!")
                    .colorize('&')
                    .sendTo(player);
            }
            return true;
        });
    }

    @Override
    public void disable() {
        logger().info("SimplePlugin disabled!");

        // Clear collections
        propertyManagers.clear();
        players.clear();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Create property manager
        PersistentPropertyManager propertyManager = new PersistentPropertyManager(
            this, 
            player.getPersistentDataContainer()
        );

        // Create a persistent property for visit count
        PersistentProperty<Integer> visitCount = propertyManager.create(
            "visit_count", 
            0, 
            (prev, cur) -> cur + 1
        );

        // Store the property manager
        propertyManagers.put(playerUUID, propertyManager);

        // Create and store BasicNDPlayer
        BasicNDPlayer ndPlayer = new BasicNDPlayer(player, propertyManager);
        players.add(ndPlayer);

        // Send welcome message with visit count
        new TextBuilder("&6Welcome back! This is visit #&e{count}")
            .replace("{count}", visitCount.get())
            .colorize('&')
            .sendTo(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        players.removeIf(p -> p.getUUID().equals(playerUUID));
    }

    @Override
    public @NotNull List<BasicNDPlayer> getBasicNDPlayers() {
        return players;
    }

    @Override
    public @NotNull Map<UUID, PersistentPropertyManager> getPlayerPropertyManagers() {
        return propertyManagers;
    }
}
```

## Best Practices

- Always specify NDCore as a dependency in your plugin.yml
- Use `provided` scope in Maven to avoid including NDCore in your plugin JAR
- Extend NDPlugin instead of JavaPlugin to access enhanced functionality
- Implement the required methods: `getBasicNDPlayers()` and `getPlayerPropertyManagers()`
- Initialize player data on join and clean up on quit
- Use the enhanced logging system provided by NDPlugin
- Take advantage of the persistent property system for player data
- Use the TextBuilder for creating rich text messages
