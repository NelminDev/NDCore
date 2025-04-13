# ⚙️ NDCore - Your Minecraft Plugin Foundation

NDCore is a core dependency plugin crafted by nelmin to enhance Minecraft plugin development. It provides essential
utility functions, requiring no configuration, and is designed to improve efficiency and functionality.

## 🚀 Overview

NDCore operates as a lightweight plugin, supporting persistent data management, multilingual localization, secure player
management, advanced menu handling, and efficient performance. It serves as a foundational library for nelmin's plugin
ecosystem, streamlining player data management, security, and interactions.

## ✨ Key Features

- **Persistent Data Management**: Robust support for persistent data, including list properties.
- **Adventure API Text Handling**: Advanced text formatting using the Adventure API.
- **Comprehensive Localization**: Streamlined localization system for multilingual support.
- **Enhanced Player Data**: In-depth player data tracking and statistics.
- **Secure Password Management**: Argon2 encryption for secure password handling.
- **Player Data Conversion**: Utilities for seamless player data conversion.
- **Flexible Menu System**: Advanced menu handling with custom interactions.
- **Dependency-Only Plugin**: Functions as a core dependency without extra overhead.
- **Zero Configuration**: Ready to use out-of-the-box, no configuration needed.
- **Lightweight & Efficient**: Designed for optimal performance and minimal footprint.

## 👨‍💻 For Developers

Explore NDCore's capabilities and integrate it into your projects:

- **Lumina**: Leverage the [Lumina](https://github.com/NelminDev/Lumina) project for enhanced logging.

Key Components:

- `PersistentProperty`: Manages persistent data storage.
- `PersistentListProperty` & `PersistentMutableListProperty`: Handle persistent list data efficiently.
- `TextBuilder`: Simplifies Adventure API-based text formatting.
- `LocalizedMessage`: Manages translation and localization.
- `NDPlayer`: Offers extensive player data management with:
  - `.economy()`: Economic functionality integration.
  - `.security()`: Secure player management tools.
- `MenuInterface`: Create custom menu implementations.
- `MenuClickEvent` & `MenuClickListener`: Handle menu interactions with ease.

### 📦 Dependency Integration

To use NDCore as a dependency, add the following to your `plugin.yml`:

```yaml
depend: [NDCore]
```

Import via Maven Central:

<details>
<summary>Gradle</summary>

```gradle
implementation 'dev.nelmin.minecraft:core:VERSION'
```

</details>

<details>
<summary>Gradle (Kotlin)</summary>

```kts
implementation("dev.nelmin.minecraft:core:VERSION")
```

</details>

<details>
<summary>Maven</summary>

```xml
<dependency>
  <groupId>dev.nelmin.minecraft</groupId>
  <artifactId>core</artifactId>
  <version>VERSION</version>
</dependency>
```

</details>

## ⚙️ System Requirements

- Java 21 or higher
- Compatible with the latest PaperMC version

## 🛠️ Installation

1. Download NDCore from the releases page.
2. Place the JAR file in your server's `plugins` folder.
3. Restart your server.
4. The plugin will load automatically.

## 📜 License

This project is licensed under the GNU General Public License v3.0 (GPL-3.0). See the [LICENSE](LICENSE) file for details.