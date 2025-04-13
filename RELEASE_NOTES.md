## 🎯 Release Overview

Version 2.2.2 introduces a new menu handling system, updates the package name, and improves plugin instance retrieval.
Additionally, text handling has been refactored to use Adventure API components.

## 🚀 Key Changes

- **Package Rename**:
  - Renamed package from `spigot` to `minecraft`.
  - Updated all package declarations and imports.
  - Adjusted the project group in `gradle.properties` and the main class path in `plugin.yml`.

- **Improved Plugin Instance Retrieval**:
  - Replaced `NDCore.instance()` with `JavaPlugin.getPlugin(NDCore::class.java)` for improved reliability and
    consistency.
  - Added utility conversion methods for `Player` to `NDPlayer`, `NDEconomyPlayer`, and `NDSecurityPlayer`.

- **Menu Handling System**:
  - Introduced a `Menu` system with the `MenuInterface` for custom menu management.
  - Implemented `MenuClickEvent` and `MenuClickListener` to handle menu interactions, enabling custom actions per slot.
  - Updated `NDCore` to register the new listener and integrate menu functionalities.

- **Adventure API Text Handling**:
  - Refactored text handling to use Adventure API components for better text formatting and serialization.
  - Replaced `String`-based text handling with Adventure's `Component`.
  - Removed deprecated methods and updated APIs to align with the new component-based approach.

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
implementation 'dev.nelmin.minecraft:core:2.2.2'
```

</details>

<details>
<summary>Gradle (Kotlin)</summary>

```kts
implementation("dev.nelmin.minecraft:core:2.2.2")
```

</details>

<details>
<summary>Maven</summary>

```xml
<dependency>
  <groupId>dev.nelmin.minecraft</groupId>
  <artifactId>core</artifactId>
  <version>2.2.2</version>
</dependency>
```

</details>

## 📌 System Requirements

- Java 21 or higher
- Compatible with PaperMC 1.21.4 or higher