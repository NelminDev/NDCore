# ⚙️ NDCore - Your Minecraft Plugin Foundation

NDCore is a robust core dependency plugin designed to provide essential functionality for Minecraft plugins. Developed
by nelmin, it's crafted to be a foundational library, offering crucial utility functions without requiring any
configuration. It's available for everyone to use and enhance their plugin development experience.

## 🚀 Overview

NDCore is engineered as a lightweight, background-operating plugin, providing the backbone for nelmin's plugin
ecosystem. It encompasses persistent data management, localization support, comprehensive event logging, secure player
management, and advanced menu handling capabilities. It serves primarily as a dependency, enhancing player data
management, security, monitoring, and menu interactions, all while remaining efficient and unobtrusive.

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