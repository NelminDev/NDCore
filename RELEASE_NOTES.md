## 🎯 Release Overview

Version 2.2.0 brings significant enhancements to NDCore with improved security features, expanded data management capabilities, and continued optimization of core functionalities.

## 🚀 Key Features

- **Enhanced Data Persistence**:
  - New persistent list property system
  - Thread-safe property management
  - Improved data type handling

- **Security Enhancements**:
  - New secure password management using Argon2
  - Enhanced player security features
  - Player data conversion utilities

- **Platform Support**:
  - Support for PaperMC 1.21.4 or higher
  - Java 21 compatibility
  - Zero configuration requirement

- **Player Experience**:
  - Advanced text handling with Adventure API
  - Comprehensive localization system
  - Extended player data management
  - Player statistics tracking
  - New economy functionality through NDPlayer

- **Monitoring & Logging**:
  - Detailed player activity logging
  - Enhanced event tracking system
  - Improved debugging capabilities

## 📦 Installation

1. Download NDCore-Paper.jar
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. The plugin will load automatically as a dependency

## 💻 For Developers

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
implementation 'dev.nelmin.minecraft:core:2.2.0'
```

</details>

<details>
<summary>Gradle (Kotlin)</summary>

```kts
implementation("dev.nelmin.minecraft:core:2.2.0")
```

</details>

<details>
<summary>Maven</summary>

```xml
<dependency>
  <groupId>dev.nelmin.minecraft</groupId>
    <artifactId>core</artifactId>
    <version>2.2.0</version>
</dependency>
```

</details>

## 📌 System Requirements

- Java 21 or higher
- Compatible with PaperMC 1.21.4 or higher