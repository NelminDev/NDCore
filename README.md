# NDCore

NDCore is the core dependency plugin that provides essential functionality for plugins developed by nelmin. This plugin serves as a foundational library, offering crucial utility functions while maintaining zero configuration requirements.

## Overview

NDCore is intentionally designed to be a lightweight core plugin that operates silently in the background. While it doesn't provide any standalone features, it serves as an essential backbone for nelmin's plugin ecosystem by providing common utility functions and features that other plugins can leverage.

## Key Characteristics

- Functions as a dependency-only plugin
- Requires no configuration
- Provides essential utility functions for dependent plugins
- Lightweight and efficient design
- Zero standalone features

## For Developers

Developers interested in utilizing NDCore's functionality should look into:

1. [NDCConfig.kt](src/main/kotlin/dev/nelmin/spigot/NDCConfig.kt) for configuration utilities
2. [Lumina](https://github.com/NelminDev/Lumina) project for logging capabilities

To use NDCore as a dependency, add the following to your `plugin.yml`:

```yaml
depend: [NDCore]
```

## System Requirements

- Java 21 or higher
- Compatible with Bukkit/Spigot/Paper servers

## Installation

1. Download NDCore from the releases page
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. The plugin will load automatically as a dependency

## License

This project is licensed under the GNU General Public License v3.0 (GPL-3.0). See the [LICENSE](LICENSE) file for details.

## Support

For issues, questions, or contributions, please use the GitHub repository's issue tracker.