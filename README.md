# NDCore

NDCore is the core dependency plugin that provides essential functionality for plugins developed by nelmin. This plugin serves as a foundational library, offering crucial utility functions while maintaining zero configuration requirements.

## Overview

NDCore is intentionally designed to be a lightweight core plugin that operates silently in the background. It provides essential backbone functionality for nelmin's plugin ecosystem, including persistent data management, localization support, comprehensive event logging, and secure player management. While primarily serving as a dependency, it now includes enhanced player data management, security features, and monitoring capabilities.

## Key Features

- Persistent data management with support for list properties
- Advanced text handling using Adventure API
- Comprehensive localization system
- Enhanced player data tracking and statistics
- Secure password management using Argon2
- Player data conversion utilities
- Functions as a dependency-only plugin
- Requires no configuration
- Lightweight and efficient design

## For Developers

Developers interested in utilizing NDCore's functionality should look into:

- The [Lumina](https://github.com/NelminDev/Lumina) project for logging capabilities

Key components:
- `PersistentProperty` for managing persistent data
- `PersistentListProperty` and `PersistentMutableListProperty` for managing persistent list data
- `TextBuilder` for Adventure API-based text formatting
- `LocalizedMessage` for translation management
- `NDPlayer` for extensive player data management with conversion methods:
    - `economy()` for economic functionality
    - `security()` for secure player management

To use NDCore as a dependency, add the following to your `plugin.yml`:

```yaml
depend: [NDCore]
```

and import it via maven central:

<details>
<summary>Gradle</summary>

```gradle
implementation 'dev.nelmin.spigot:core:2.2.0'
```

</details>

<details>
<summary>Gradle (Kotlin)</summary>

```kts
implementation("dev.nelmin.spigot:core:2.2.0")
```

</details>

<details>
<summary>Maven</summary>

```xml
<dependency>
    <groupId>dev.nelmin.spigot</groupId>
    <artifactId>core</artifactId>
    <version>2.2.0</version>
</dependency>
```

</details>

## System Requirements

- Java 21 or higher
- Compatible with PaperMC 1.21.4 or higher

## Installation

1. Download NDCore from the releases page
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. The plugin will load automatically as a dependency

## License

This project is licensed under the GNU General Public License v3.0 (GPL-3.0). See the [LICENSE](LICENSE) file for details.

## Support

For issues, questions, or contributions, please use the GitHub repository's issue tracker.