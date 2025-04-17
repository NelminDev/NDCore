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

### 📚 Documentation & Usage Guides

Dive into our magical world of code with our comprehensive documentation:

🔮 [**Explore the Documentation**](usage/Introduction.md) - Your portal to all NDCore knowledge!

Our documentation is organized into themed realms, each containing treasures of information about NDCore's components. The Introduction will guide you through these realms and help you find exactly what you need.

Whether you're a coding wizard or just starting your journey, our documentation provides:

- 🧙‍♂️ **Integration guide for beginners**
- 🏗️ **Advanced implementation examples**
- 🧩 **Code snippets for common tasks**
- 🛠️ **Best practices for optimal performance**

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
