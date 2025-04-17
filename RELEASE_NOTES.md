## 🎯 Release Overview

Version 3.0.0 introduces an enhanced logging system (merged from the Lumina project), improves plugin instance
retrieval, and provides better null safety with JetBrains annotations.

## 🚀 Key Changes

- **Enhanced Logging System** (Merged from Lumina):
  - Integrated the Lumina logging project directly into NDCore for streamlined dependency management.
  - Introduced `NDLogger` with multiple logging levels (INFO, WARN, ERROR, FATAL).
  - Implemented `LoggingStrategy` interface for customizable logging behavior.
  - Added `DefaultLoggingStrategy` with color-coded console output and organized log files.
  - Deprecated standard `getLogger()` method in favor of the new `logger()` method.

- **Improved Plugin Instance Retrieval**:
  - Added `getNDPlugin(Class<T> clazz)` method for more convenient NDPlugin instance retrieval.
  - Enhanced type safety with proper casting to NDPlugin.

- **Null Safety Enhancements**:
  - Added JetBrains annotations (@NotNull) throughout the codebase.
  - Improved null checking with Objects.requireNonNull() in constructors.
  - Enhanced parameter validation for better error handling.

## 📦 Installation

1. Download NDCore-v3.0.0.jar
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
implementation 'dev.nelmin:NDCore:3.0.0'
```

</details>

<details>
<summary>Gradle (Kotlin)</summary>

```kts
implementation("dev.nelmin:NDCore:3.0.0")
```

</details>

<details>
<summary>Maven</summary>

```xml
<dependency>
  <groupId>dev.nelmin</groupId>
  <artifactId>NDCore</artifactId>
  <version>3.0.0</version>
</dependency>
```

</details>

## 📌 System Requirements

- Java 21 or higher
- Compatible with PaperMC 1.21.4 or higher
