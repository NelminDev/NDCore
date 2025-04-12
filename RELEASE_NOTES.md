## 🎯 Release Overview

Version 2.2.1 focuses on improving the logging system for better scalability and maintainability.

## 🚀 Key Changes

- **Refactored Logging System**:
  - Replaced direct logging calls (e.g., `Logger.infoSilent`) with a queued logging system (`Logger.queueInfo`) for
    improved log management.
  - Implemented coroutines for asynchronous log handling.
  - Updated plugin lifecycle behavior to utilize the new logging system.

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
implementation 'dev.nelmin.minecraft:core:2.2.1'
```

</details>

<details>
<summary>Gradle (Kotlin)</summary>

```kts
implementation("dev.nelmin.minecraft:core:2.2.1")
```

</details>

<details>
<summary>Maven</summary>

```xml
<dependency>
  <groupId>dev.nelmin.minecraft</groupId>
  <artifactId>core</artifactId>
  <version>2.2.1</version>
</dependency>
```

</details>

## 📌 System Requirements

- Java 21 or higher
- Compatible with PaperMC 1.21.4 or higher