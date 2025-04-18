# NDCore Documentation - Usage Directory

## Welcome to the NDCore Documentation

This directory contains comprehensive documentation for the NDCore library, a powerful toolkit for Minecraft plugin development on the Paper/Spigot platform. The documentation is organized to help you quickly find the information you need to leverage NDCore in your projects.

## About This Directory

The `/usage` directory serves as the central hub for all NDCore documentation. Here you'll find:

- Detailed guides for each component of the library
- Usage examples for all classes and interfaces
- Best practices for implementing NDCore features
- Code snippets that demonstrate common patterns

## Documentation Structure

The documentation includes standalone files for core components:

- [**Integration.md**](Integration.md) - Guide for integrating NDCore into your projects
- [**NDCore.md**](NDCore.md) - Documentation for the main NDCore class
- [**NDPlugin.md**](NDPlugin.md) - Documentation for the NDPlugin base class

Additionally, the documentation is organized into subdirectories that mirror the package structure of the NDCore
library:

1. [**builders/**](builders/Introduction.md) - Documentation for fluent builders (ItemBuilder, TextBuilder, etc.)
2. [**commands/**](commands/Introduction.md) - Documentation for command registration and implementation
3. [**configurations/**](configurations/Introduction.md) - Documentation for configuration management
4. [**events/**](events/Introduction.md) - Documentation for custom events (PlayerFreezeEvent, etc.)
5. [**exceptions/**](exceptions/Introduction.md) - Documentation for specialized exceptions
6. [**listener/**](listener/Introduction.md) - Documentation for event listeners
7. [**logger/**](logger/Introduction.md) - Documentation for the logging system
   - [**strategy/**](logger/strategy/Introduction.md) - Documentation for logging strategies
8. [**menu/**](menu/Introduction.md) - Documentation for the menu framework
9. [**objects/**](objects/Introduction.md) - Documentation for utility objects
10. [**other/**](other/Introduction.md) - Documentation for general utility classes
11. [**persistence/**](persistence/Introduction.md) - Documentation for data persistence
12. [**players/**](players/Introduction.md) - Documentation for player management

## How to Use This Documentation

1. **Start with Integration**: If you're new to NDCore, begin with the [Integration Guide](Integration.md) to learn how to add NDCore to your project.

2. **Explore by Package**: Each package has an Introduction.md file that explains the purpose and components of that package. Start with these introductions to understand the capabilities of each package.

3. **Dive into Classes**: Once you understand a package, explore the individual class documentation files to learn about specific implementations and usage patterns.

4. **Follow Examples**: Each documentation file includes code examples that demonstrate how to use the components effectively.

## Finding What You Need

- If you need to create rich text with colors and formatting, check the [TextBuilder documentation](builders/TextBuilder.md)
- If you want to create custom menus, explore the [menu package documentation](menu/Introduction.md)
- If you need to store persistent player data, see the [persistence package documentation](persistence/Introduction.md)
- If you want to use JSON for configuration files, check
  the [JSONConfiguration documentation](configurations/JSONConfiguration.md)
- If you need to register commands without plugin.yml, see
  the [commands package documentation](commands/Introduction.md)
- If you're using NDCore in your own plugin, refer to the [NDPlugin documentation](NDPlugin.md)

## Contributing to Documentation

If you find any issues or have suggestions for improving this documentation, please submit an issue or pull request to the NDCore repository.
