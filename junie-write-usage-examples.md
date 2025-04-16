<!--- FOR ANYONE CURIOUS: This file is for [Junie](https://www.jetbrains.com/junie/) to write documentation, because I am too lazy -->

Update [README.md](README.md) to have a "For Developers" Section, which only refers to all of those files you will create later on.
Make every new Markdown file have the same structure and design.
For each step (1., 2., 3., ...) create a new file under usage/ and refer to it in the [README.md](README.md) under the "For Developers" Section. DO NOT put multiple points into one file!!!
Do take on each step at the same time, do them step by step.
When writing a usage example, take a look at the codebase to see how it's used in production.

- Write usage examples for or describe (e.g. only describe exceptions, no usage example):
  1. [Builders Folder](/src/main/java/dev/nelmin/ndcore/builders)
  2. [Events Folder](/src/main/java/dev/nelmin/ndcore/events)
  3. [Exceptions Folder](/src/main/java/dev/nelmin/ndcore/exceptions)
  4. [Listener Folder](/src/main/java/dev/nelmin/ndcore/listener)
  5. [Logger Folder](/src/main/java/dev/nelmin/ndcore/logger)
  6. [Logger/Strategy Folder](/src/main/java/dev/nelmin/ndcore/logger/strategy)
  7. [Menu Folder](/src/main/java/dev/nelmin/ndcore/menu)
  8. [Objects Folder](/src/main/java/dev/nelmin/ndcore/objects)
  9. [Other Folder](/src/main/java/dev/nelmin/ndcore/other)
  10. [Persistence Folder](/src/main/java/dev/nelmin/ndcore/persistence)
  11. [Players Folder](/src/main/java/dev/nelmin/ndcore/players)
  12. [NDCore.java](/src/main/java/dev/nelmin/ndcore/NDCore.java)
  13. [NDPlugin.java](/src/main/java/dev/nelmin/ndcore/NDPlugin.java) (Based on [NDCore.java](/src/main/java/dev/nelmin/ndcore/NDCore.java))
  14. usage/00-integration.md: Write Integration Examples (Import via Maven Central, add to plugin.yml, Create a Plugin using NDCore, etc.)