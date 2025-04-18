## 🎯 Release Overview

Version 3.0.3 refactors getters using Lombok annotations, makes the setupProperties method public for flexibility,
improves builder classes with better documentation, and updates the project version.

## 🚀 Key Changes

- **Code Refactoring and Improvements**:
  - Applied `@Getter` annotations directly to fields in `BasicNDPlayer`, simplifying code.
  - Made `setupProperties` method public for increased flexibility.
  - Improved code maintainability through Lombok annotations.
  - Enhanced builder classes with better documentation and consistent method naming.
  - Updated builder documentation to reflect current implementation and best practices.
  - Enhanced `SkullBuilder` to support both Base64 encoded texture values and direct URLs for custom skull textures.

- **Build Configuration Updates**:
  - Updated project version to 3.0.3.

- **Menu System Improvements** (from 3.0.2):
  - Refactored `SimpleMenu` to be an abstract class, ensuring `onSetItems()` must be implemented by subclasses.
  - Added overloaded constructors to handle both `String` and `Component` titles.
  - Integrated `TextBuilder` for string colorization in title handling.
  - Improved type safety and null checking in menu implementations.

- **Build Configuration Updates** (from 3.0.2):
  - Updated project version to 3.0.3.
  - Moved `finalName` from plugin configuration to build settings for better Maven compatibility.

- **Enhanced Logging System** (Merged from Lumina):
  - Integrated the Lumina logging project directly into NDCore for streamlined dependency management.
  - Introduced `NDLogger` with multiple logging levels (INFO, WARN, ERROR, FATAL).
  - Implemented `LoggingStrategy` interface for customizable logging behavior.
  - Added `DefaultLoggingStrategy` with color-coded console output and organized log files.
  - Deprecated standard `getLogger()` method in favor of the new `logger()` method.

- **Command Registration System**:
    - Added new `commands` package with `CommandRegistrar` and `NDCommand` classes.
    - Implemented reflection-based command registration without requiring plugin.yml entries.
    - Integrated command registration into the NDPlugin lifecycle.
    - Added fluent API for registering multiple commands.

- **JSON Configuration Support**:
    - Added `JSONConfiguration` class for JSON-based configuration files.
    - Implemented full compatibility with Bukkit's configuration API.
    - Added pretty printing options for human-readable JSON output.

- **Improved Plugin Instance Retrieval**:
  - Added `getNDPlugin(Class<T> clazz)` method for more convenient NDPlugin instance retrieval.
  - Enhanced type safety with proper casting to NDPlugin.

- **Null Safety Enhancements**:
  - Added JetBrains annotations (@NotNull) throughout the codebase.
  - Improved null checking with Objects.requireNonNull() in constructors.
  - Enhanced parameter validation for better error handling.

## 📦 Installation

1. Download NDCore-3.0.3.jar
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. The plugin will load automatically as a dependency

## 💻 For Developers

For comprehensive documentation on how to use NDCore, please refer to the [Introduction](usage/Introduction.md) in the
usage directory.

To use NDCore as a dependency, add the following to your `plugin.yml`:

```yaml
depend: [NDCore]
```

and import it via maven central:

<details>
<summary>Gradle</summary>

```gradle
implementation 'dev.nelmin.minecraft:core-paper:3.0.3'
```

</details>

<details>
<summary>Gradle (Kotlin)</summary>

```kts
implementation("dev.nelmin.minecraft:core-paper:3.0.3")
```

</details>

<details>
<summary>Maven</summary>

```xml
<dependency>
    <groupId>dev.nelmin.minecraft</groupId>
    <artifactId>core-paper</artifactId>
  <version>3.0.3</version>
</dependency>
```

</details>

## 📌 System Requirements

- Java 21 or higher
- Compatible with PaperMC 1.21.4 or higher
