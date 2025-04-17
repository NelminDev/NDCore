<!--- FOR ANYONE CURIOUS: This file is for [Junie](https://www.jetbrains.com/junie/) to write documentation, because I am too lazy -->

### Structure Rules
- **Mirror the source directory**: Every Java package under [`src/main/java/dev/nelmin/ndcore`](../src/main/java/dev/nelmin/ndcore) must have a corresponding directory in [`/usage`](../usage).  
  Example:  
  `src/main/java/dev/nelmin/ndcore/events` → `usage/events`  
  `src/main/java/dev/nelmin/ndcore/logger/strategy` → `usage/logger/strategy`

- **1:1 File Mapping**: Every Java file (e.g., `XEvent.java`) **must** have a Markdown file with the **exact same name** (e.g., `XEvent.md`) in its corresponding `/usage` directory.  
  Example:  
  `src/.../events/PlayerJoinEvent.java` → `usage/events/PlayerJoinEvent.md`  
  `src/.../builders/SkullBuilder.java` → `usage/builders/SkullBuilder.md`

- **Package Introductions**: For every package (e.g., `builders/`, `events/`), create an `Introduction.md` file explaining the **purpose** of the package in the real codebase.  
  Example:  
  `src/.../builders/` → `usage/builders/Introduction.md` (explains what the builders package does)  
  `src/.../logger/strategy/` → `usage/logger/strategy/Introduction.md` (describes logging strategies)

---

### Workflow
1. **Documentation Creation**:
  - For every new `.java` file, create a corresponding `.md` file in the mirrored `/usage` directory.
  - Include **code examples**, **method explanations**, and **usage context** based on the actual code.
  - For packages, always add an `Introduction.md` to explain the package’s role and key components.

2. **Updates**:
  - When a `.java` file is modified (e.g., new methods, bug fixes), update its `.md` file **immediately**.
  - Ensure `Introduction.md` files reflect any structural changes to the package (e.g., new sub-packages, deprecated classes).

3. **README Maintenance**:
  - Add/update links in the [`README.md`](../README.md) under "For Developers" for new or significantly changed documentation.

4. **Validation**:
  - Cross-check documentation against the codebase to ensure accuracy (e.g., parameter names, return types).
  - Verify that all packages have an `Introduction.md` file.

---

### Documentation Requirements
- **Every `.java` file must be documented**: Each Java class/interface (e.g., `TextBuilder.java`, `NDCore.java`) **must** have a corresponding Markdown file in the `/usage` directory.
- **Update existing docs**: If a `.java` file is modified (e.g., new methods, behavioral changes), **immediately update its Markdown file** to reflect the changes.
- **Package Introductions**:
  - Every package directory in `/usage` **must** include an `Introduction.md` explaining:
    - The package’s **role** in the codebase.
    - Key classes and their relationships.
    - Common use cases.
  - Example: `usage/events/Introduction.md` should describe why the events package exists and how it interacts with listeners.
- **Exceptions**:
  - The `exceptions/` folder requires **descriptions of error scenarios and mitigation steps**, not code examples.
  - Top-level classes like `NDCore.java` and `NDPlugin.java` require both **setup guides** and **API references**.

---

### Example Workflow
**Scenario**: A new package `inventory/` is added to `src/.../`:
1. Create `/usage/inventory/` and `/usage/inventory/Introduction.md`.
2. In `Introduction.md`, explain the package’s role (e.g., "Handles GUI inventory management").
3. Document all classes under `inventory/` (e.g., `InventoryManager.md`).
4. Add links to `README.md`.

**Scenario**: `TextBuilder.java` is updated with a new method:
1. Locate `usage/builders/TextBuilder.md`.
2. Add a section explaining the new method’s purpose and usage.
3. Update the `README.md` if the change impacts overall functionality.

---

### Integration Guide
- **Standalone File**: [`Integration.md`](../usage/Integration.md) covers:
  - Importing via Maven/Gradle.
  - Configuring `plugin.yml` for NDCore dependencies.
  - Creating a basic plugin template.  