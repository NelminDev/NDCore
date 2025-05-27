# ⚙️ NDCore

NDCore is a core dependency plugin developed by mtctx to enhance Minecraft plugin development. It provides essential
utility functions with no configuration requirements, designed to improve efficiency and functionality.

NDCore stood for NelminDevs Core, but I've changed my name and I don't want to change the branding of this repository,

## 🚀 Overview

NDCore functions as a lightweight plugin that supports persistent data management, multilingual localization, player
management, advanced menu handling, and efficient performance. It serves as a foundational library for mtctx's plugin
ecosystem, streamlining player data management and interactions.

The plugin architecture is built around two main classes:

- **NDPlugin**: An abstract base class extending JavaPlugin that provides lifecycle management, enhanced logging, and player data handling capabilities.
- **NDCore**: The concrete implementation that manages persistent player properties, registers event listeners, and performs regular cleanup of offline player data.

## ✨ Key Features

| Feature                           | Description                                                                                        |
|-----------------------------------|----------------------------------------------------------------------------------------------------|
| **Plugin Architecture**           | Extendable NDPlugin base class with lifecycle management and enhanced logging                      |
| **Command Registration System**   | Register commands without plugin.yml entries using the CommandRegistrar                            |
| **JSON Configuration Support**    | Full-featured JSON configuration with Bukkit API compatibility                                     |
| **Automatic Resource Management** | Scheduled cleanup of offline player data to optimize memory usage                                  |
| **Persistent Data Management**    | Robust support for persistent data with type safety and default values                             |
| **Adventure API Text Handling**   | Advanced text formatting using the Adventure API with support for gradients and MiniMessage format |
| **Comprehensive Localization**    | Streamlined localization system with support for over 180 languages                                |
| **Enhanced Player Data**          | Basic player data tracking with language preferences                                               |
| **Flexible Menu System**          | Simple yet powerful menu system with custom click handlers                                         |
| **Integrated Logging**            | Enhanced NDLogger system with INFO, WARN, ERROR, and FATAL levels                                  |
| **Dependency-Only Plugin**        | Functions as a core dependency without extra overhead                                              |
| **Zero Configuration**            | Ready to use out-of-the-box, no configuration needed                                               |
| **Lightweight & Efficient**       | Designed for optimal performance and minimal footprint                                             |

## 👨‍💻 For Developers

### 📚 Documentation & Usage Guides

Comprehensive documentation is available to help you integrate and use NDCore effectively:

🔮 [**Access Documentation**](usage/Introduction.md)

Our documentation is organized by component categories, providing detailed information about NDCore's features. The
Introduction will guide you through the documentation structure and help you locate specific information.

The documentation includes:

- 🧙‍♂️ **Integration guides for new developers**
- 🏗️ **Advanced implementation examples**
- 🧩 **Code snippets for common tasks**
- 🛠️ **Best practices for optimal performance**

## ⚙️ System Requirements

- ☕ Java 21 or higher
- 🧱 Compatible with the latest PaperMC version (1.21.5)

## 🔧 Installation

1. 📥 Download NDCore from the release page
2. 📁 Place the JAR file in your server's `plugins` folder
3. 🔄 Restart your server
4. ✅ The plugin will load automatically

## 📜 License

This project is licensed under the GNU General Public License v3.0 (GPL-3.0). See the [LICENSE](LICENSE) file for details.
