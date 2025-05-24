# Java 21/Paper-API Development Guidelines

## Table of Contents

1. [Core Principles](#core-principles)
2. [Performance Optimization](#performance-optimization)
3. [Code Structure & Readability](#code-structure--readability)
4. [Annotations & Null Safety](#annotations--null-safety)
5. [Naming Conventions](#naming-conventions)
6. [Conditional Checks](#conditional-checks)
7. [SOLID Principles](#solid-principles)
8. [Java 21 Features](#java-21-features)
9. [Paper/Spigot-API Integration](#paperspigot-api-integration)
10. [Documentation](#documentation)
11. [Documentation Structure & Workflow](#documentation-structure--workflow)
12. [Compliance & Best Practices](#compliance--best-practices)
13. [Example Implementation](#example-implementation)

---

## Core Principles

- **Performance First**: Execution speed > syntactic sugar
- **Structure Sacred**: Original code order preserved (variables → constructors → methods)
- **Java 21 Modernity**: Mandatory use of new features where applicable
- **Defensive Programming**: Fail fast with null/argument checks
- **SOLID Foundation**: Architecture follows SOLID principles

---

## Performance Optimization

### Zero-Cost Abstractions

| Technique             | Example                             | Anti-Pattern            |  
|-----------------------|-------------------------------------|-------------------------|  
| Pre-sized collections | `new ArrayList<>(512)`              | `new ArrayList<>()`     |  
| Primitive streams     | `IntStream.range(0, 100)`           | `Stream<Integer>`       |  
| EnumMap               | `new EnumMap<>(Material.class)`     | `HashMap<Material, ...` |  
| Array hot paths       | `Item[] hotItems = new Item[1024];` | `List<Item>` in loops   |  

### Memory Management

- `-XX:+UseCompressedOops` JVM flag required
- Object pooling for entities with >1000 instances
- `SoftReference` for caches, `WeakReference` for listeners

### Concurrency

```java
// PaperAPI async scheduling
Bukkit.getScheduler().

runTaskAsynchronously(plugin, () ->{
        // File I/O or network calls
        });

// Virtual threads for non-Paper async
        try(
var executor = Executors.newVirtualThreadPerTaskExecutor()){
        executor.

submit(() ->database.

query(...));
        }
```

---

## Code Structure & Readability

### Structural Mandate

DO NOT add any of those comments in the following code block to the real code:

```java
// 1. Static variables
private static final int MAX_PLAYERS = 100;

// 2. Instance variables
private final @NotNull PlayerRegistry registry;

// 3. Constructors (primary first)
public GameEngine(@NotNull PlayerRegistry registry) {
    this.registry = Objects.requireNonNull(registry);
}

// 4. Methods (public → protected → private)
public void startGame() { ...}

private void validatePlayers() { ...}
```

### Grouping Rules

- Related fields/methods must stay clustered as originally authored
- No splitting of cohesive logic (e.g., validator + validation rules)

---

## Annotations & Null Safety

### JetBrains Annotations

| Situation           | Example                                   |  
|---------------------|-------------------------------------------|  
| Non-null parameter  | `public void teleport(@NotNull Player p)` |  
| Nullable return     | `public @Nullable Config getConfig()`     |  
| Generic null safety | `List<@NotNull Item> inventory`           |  

### Validation Protocol

```java
// Constructor validation
public ItemStack(@NotNull Material material, int amount) {
    this.material = Objects.requireNonNull(material, "Material cannot be null");
    this.amount = Math.max(0, amount);
}

// Method preconditions
public void buyItem(@NotNull Player buyer) {
    if (null == buyer) throw new IllegalArgumentException("buyer cannot be null"); // Yoda
}
```

---

## Naming Conventions

### Accessors/Mutators

```java
// Preferred style
public Location spawnLocation() { ...}

public void spawnLocation(@NotNull Location loc) { ...}

// Allowed only for JavaBean specs
public String getDisplayName() { ...}

public void setDisplayName(@NotNull String name) { ...}
```

### Boolean Methods

```java
// State checks
public boolean hasPermission() { ...}   // NOT isPermission()

public boolean valid() { ...}           // NOT isValid()

public boolean canBuild(@NotNull Player p) { ...}
```

---

## Conditional Checks

### Yoda Conditions

```java
// Yoda REQUIRED for null/literals
if(null==player){...}
        if(5==score){...}

// Yoda FORBIDDEN for method-based booleans
        if(name.

isBlank()){...}       // ✅
        if(true==name.

isBlank()){...} // ❌
```

### Pattern Matching

```java
switch(event.getCause()){
        case
DAMAGE dmg
when 10 <dmg.

amount() ->

handleCriticalHit();
    case
HEAL heal ->

applyHealBonus(heal);

default ->

logUnknownCause();
}
```

---

## SOLID Principles

| Principle                 | Implementation                              |  
|---------------------------|---------------------------------------------|  
| **Single Responsibility** | `PlayerMoveHandler` ≠ `PlayerDamageHandler` |  
| **Open/Closed**           | `sealed interface EventResponse`            |  
| **Liskov**                | `@Override` methods honor parent contracts  |  
| **Interface Segregation** | `ItemReader` ≠ `ItemWriter`                 |  
| **Dependency Inversion**  | `class Shop { private Economy economy; }`   |  

---

## Java 21 Features

### Mandatory Usage

```java
// Records for DTOs
public record Trade(@NotNull ItemStack item, double price, @NotNull UUID seller) {
}

// Sealed hierarchies
public sealed interface Entity permits PlayerEntity, MobEntity {
}

// Pattern matching switches
return switch(block.

getType()){
        case STONE,GRANITE ->"hard_material";
        case AIR ->throw new

InvalidBlockException();

default ->"other";
        };

// Scoped values
final ScopedValue<User> CURRENT_USER = ScopedValue.newInstance();
ScopedValue.

where(CURRENT_USER, user).

run(() ->

processPurchase());
```

---

## Paper/Spigot-API Integration

### Async Best Practices

```java
// Correct async chunk loading
Bukkit.getScheduler().

runTaskAsynchronously(plugin, () ->{
Chunk chunk = world.getChunkAtAsync(x, z).join();
    Bukkit.

getScheduler().

runTask(plugin, () ->{
        chunk.

getBlock(0,64,0).

setType(Material.GOLD_BLOCK);
    });
            });

// Event priority guidelines
@EventHandler(priority = EventPriority.HIGHEST)
public void onPlayerJoin(PlayerJoinEvent event) {
    // Critical logic first
}
```

---

## Documentation

### Javadoc Standards

```java
/**
 * Caches player data with LRU eviction.
 *
 * @implNote Uses WeakReference to prevent leaks
 * @param maxSize Maximum cached players (0 < maxSize ≤ 1000)
 * @throws IllegalArgumentException If maxSize invalid
 */
public @NotNull PlayerCache createCache(int maxSize) { ...}
```

### Comprehensive Documentation

- **Every Class**:
    - Purpose and responsibility
    - Thread safety guarantees
    - Example usage snippet

- **Every Method**:
    - Pre/post conditions
    - Parameter constraints (`@param`)
    - Return semantics (`@return`)
    - Exceptions (`@throws`)

```java
/**
 * Applies damage modifiers from enchantments.
 *
 * @param weapon Must have durability > 0 (logs warning if broken)
 * @return Final damage value between 0-20
 * @throws NullPointerException if weapon is null
 */
public float calculateDamage(@NotNull ItemStack weapon) { ...}
```

---

## Documentation Structure & Workflow

### Structure Rules

- **Mirror Source Directories**:  
  `src/main/java/dev/nelmin/ndcore/events` → `usage/events`

- **1:1 File Mapping**:  
  `PlayerJoinEvent.java` → `PlayerJoinEvent.md`

- **Package Introductions**:  
  Each package directory must have `Introduction.md` explaining:
    - Purpose and scope
    - Key classes and interactions

### Workflow

1. **Creation**:
    - New Java file → mirrored Markdown with code examples
    - Packages get `Introduction.md`

2. **Updates**:
    - Sync Markdown within 24 hours of code changes
    - Update `README.md` links for major changes

3. **Validation**:
    - Weekly checks for:
        - Missing documentation
        - Stale examples

---

## Compliance & Best Practices

1. **Immutability**: All fields `final` unless mutation required
2. **Thread Safety**:
   ```java
   private final ReentrantLock lock = new ReentrantLock();
   public void update() {
       lock.lock();
       try { /* critical section */ } 
       finally { lock.unlock(); }
   }
   ```  
3. **Documentation Compliance**:
    - [ ] All classes/methods fully documented
    - [ ] Package introductions exist
    - [ ] No `get`/`set` prefixes unless required
    - [ ] Yoda conditions enforced for null/literals

---

## Example Implementation

```java
/**
 * Handles player trading with LRU caching.
 *
 * @see PlayerTradeEvent for event integration
 */
public final class TradeSystem implements Listener {
    // Static variables
    private static final int MAX_TRADES = 100;

    // Instance variables
    private final @NotNull Map<UUID, TradeOffer> activeTrades;

    /**
     * Constructs a trade system with LRU caching.
     */
    public TradeSystem() {
        this.activeTrades = Collections.synchronizedMap(new LinkedHashMap<>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<UUID, TradeOffer> eldest) {
                return size() > MAX_TRADES;
            }
        });
    }

    /**
     * Handles trade initialization events.
     *
     * @param event Requires non-null initiator
     */
    @EventHandler
    public void onTradeInit(@NotNull PlayerTradeEvent event) {
        if (null == event.getInitiator()) return;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                processTradeAsync(event)
        );
    }
}
```

---

**Compliance Checklist**

- [ ] Yoda conditions for null/literal checks
- [ ] `@NotNull`/`@Nullable` on all declarations
- [ ] SOLID principles validated
- [ ] Using @Accessors(fluent = true) everywhere where lombok is used
- [ ] Java 21 features used where applicable
- [ ] Every class/method fully documented
- [ ] Package introductions updated