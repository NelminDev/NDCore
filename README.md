# ⚙️ NDCore - Your Minecraft Plugin Foundation

NDCore is a core dependency plugin crafted by nelmin to enhance Minecraft plugin development. It provides essential
utility functions, requiring no configuration, and is designed to improve efficiency and functionality.

## 🚀 Overview

NDCore operates as a lightweight plugin, supporting persistent data management, multilingual localization, player
management, advanced menu handling, and efficient performance. It serves as a foundational library for nelmin's plugin
ecosystem, streamlining player data management and interactions.

The plugin architecture is built around two main classes:

- **NDPlugin**: An abstract base class extending JavaPlugin that provides lifecycle management, enhanced logging, and player data handling capabilities.
- **NDCore**: The concrete implementation that manages persistent player properties, registers event listeners, and performs regular cleanup of offline player data.

## ✨ Key Features

- **Plugin Architecture**: Extendable NDPlugin base class with lifecycle management and enhanced logging.
- **Automatic Resource Management**: Scheduled cleanup of offline player data to optimize memory usage.
- **Persistent Data Management**: Robust support for persistent data with type safety and default values.
- **Adventure API Text Handling**: Advanced text formatting using the Adventure API with support for gradients and MiniMessage format.
- **Comprehensive Localization**: Streamlined localization system with support for over 180 languages.
- **Enhanced Player Data**: Basic player data tracking with language preferences.
- **Flexible Menu System**: Simple yet powerful menu system with custom click handlers.
- **Integrated Logging**: Enhanced NDLogger system with INFO, WARN, ERROR, and FATAL levels.
- **Dependency-Only Plugin**: Functions as a core dependency without extra overhead.
- **Zero Configuration**: Ready to use out-of-the-box, no configuration needed.
- **Lightweight & Efficient**: Designed for optimal performance and minimal footprint.

## 👨‍💻 For Developers

Explore NDCore's capabilities and integrate it into your projects:

### Key Components:

- `NDPlugin`: Abstract base class providing lifecycle management and enhanced logging.
- `NDCore`: Core implementation managing player data and event listeners.
- `PersistentProperty`: Manages persistent data storage with type safety.
- `PersistentPropertyManager`: Creates and manages persistent properties for players.
- `TextBuilder`: Simplifies Adventure API-based text formatting with support for gradients and replacements.
- `LocalizedMessage`: Manages translation and localization with fallback support.
- `LanguageCode`: Comprehensive enum of ISO 639-1 language codes.
- `BasicNDPlayer`: Basic player wrapper with persistent property support.
- `Menu` & `SimpleMenu`: Create custom menu implementations with click handling.
- `NDLogger`: Integrated logging system with INFO, WARN, ERROR, and FATAL levels.

### Detailed Usage Guides

For detailed usage examples and best practices, check out our comprehensive guides:

0. [Integration Guide](usage/00-integration.md) - Learn how to integrate NDCore into your projects
1. [Builders Usage Guide](usage/01-builders.md) - Create and manipulate items, text, and more with fluent builders
2. [Events Usage Guide](usage/02-events.md) - Work with custom events for player state changes
3. [Exceptions Usage Guide](usage/03-exceptions.md) - Handle specific error conditions with custom exceptions
4. [Listener Usage Guide](usage/04-listener.md) - Implement event listeners for custom menus
5. [Logger Usage Guide](usage/05-logger.md) - Use the enhanced logging system
6. [Logger Strategy Usage Guide](usage/06-logger-strategy.md) - Create custom logging strategies
7. [Menu Usage Guide](usage/07-menu.md) - Build interactive inventories and GUIs
8. [Objects Usage Guide](usage/08-objects.md) - Work with language codes and localized messages
9. [Other Utilities Usage Guide](usage/09-other.md) - Use date/time utilities and data structures
10. [Persistence Usage Guide](usage/10-persistence.md) - Manage persistent data with type safety
11. [Players Usage Guide](usage/11-players.md) - Work with player data and interactions
12. [NDCore Usage Guide](usage/12-ndcore.md) - Learn about the core plugin functionality
13. [NDPlugin Usage Guide](usage/13-ndplugin.md) - Extend the base class to create your plugins

## ⚙️ System Requirements

- Java 21 or higher
- Compatible with the latest PaperMC version (1.21.4)

## 🛠️ Installation

1. Download NDCore from the releases page.
2. Place the JAR file in your server's `plugins` folder.
3. Restart your server.
4. The plugin will load automatically.

## 📜 License

This project is licensed under the GNU General Public License v3.0 (GPL-3.0). See the [LICENSE](LICENSE) file for details.
