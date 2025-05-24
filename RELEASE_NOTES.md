## 🎯 Release Overview

Version 3.0.8 introduces expanded ItemBuilder capabilities, updated coding guidelines, and Minecraft 1.21.5 compatibility.

## 🚀 Key Changes

- **ItemBuilder Enhancements**:
  - Added advanced customization options for item creation
  - Improved method documentation and examples
  - Enhanced parameter validation for better error handling
  - Added support for component-based display names and lore

- **Coding Guidelines**:
  - Updated guidelines to enforce consistent Lombok usage
  - Added new sections for Java 21 features utilization
  - Enhanced documentation requirements
  - Improved null safety protocols

- **Dependency Updates**:
  - Updated to support Minecraft 1.21.5
  - Updated Paper API to 1.21.5-R0.1-SNAPSHOT
  - Updated Lombok to version 1.18.38
  - Updated GSON to version 2.13.0

## 📦 Installation

1. Download NDCore.jar
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
implementation 'dev.nelmin.minecraft:core-paper:3.0.6'
```

</details>

<details>
<summary>Gradle (Kotlin)</summary>

```kts
implementation("dev.nelmin.minecraft:core-paper:3.0.6")
```

</details>

<details>
<summary>Maven</summary>

```xml
<dependency>
    <groupId>dev.nelmin.minecraft</groupId>
    <artifactId>core-paper</artifactId>
  <version>3.0.6</version>
</dependency>
```

</details>

## 📌 System Requirements

- Java 21 or higher
- Compatible with PaperMC 1.21.4 or higher
