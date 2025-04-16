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
11. [Compliance & Best Practices](#compliance--best-practices)
12. [Example Implementation](#example-implementation)

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

private void shuffleTeams() { ...}
```

But do not add comments like:

```java
// 1. Static variables

// 2. Instance variables

// 3. Constructors (primary first)

// 4. Methods (public → protected → private)
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
    // ...
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
public boolean hasPermission() { ...}  // NOT hasPermission()

public boolean valid() { ...}          // NOT isValid()

// PaperAPI extensions
public boolean canBuild(@NotNull Player p) { ...}
```

---

## Conditional Checks

### Yoda Conditions

```java
// Yoda REQUIRED when comparing to null/literals
if(null==player){...}
        if(5==score){...}
        if(Material.STONE ==block.

getType()){...}

// Yoda FORBIDDEN for method-based booleans
        if(name.

isBlank()){...}          // ✅
        if(true==name.

isBlank()){...}  // ❌
        if(0!=list.

size()){...}        // ❌ (use !list.isEmpty())
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

### Implementation Table

| Principle                 | Java 21 Implementation                             | PaperAPI Example                                         |
|---------------------------|----------------------------------------------------|----------------------------------------------------------|
| **Single Responsibility** | `PlayerMoveHandler` ≠ `PlayerDamageHandler`        | Separate `BlockBreakListener`                            |
| **Open/Closed**           | `sealed interface EventResponse` with new records  | Extend `BukkitEvent` via new subclasses                  |
| **Liskov**                | `@Override` methods never throw broader exceptions | Custom `CloudNetAdapter` implements all parent contracts |
| **Interface Segregation** | `ItemReader` + `ItemWriter` ≠ `ItemRepository`     | `ChunkLoader` ≠ `ChunkSaver`                             |
| **Dependency Inversion**  | `class Shop { private final Economy economy; }`    | Inject `VaultEconomy` via interface                      |

---

## Java 21 Features

### Mandatory Usage

```java
// Records for DTOs
public record Trade(
                @NotNull ItemStack item,
                double price,
                @NotNull UUID seller
        ) {
}

// Sealed hierarchies
public sealed interface Entity permits PlayerEntity, MobEntity, ItemEntity {
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
 * @implNote Uses WeakReference to prevent memory leaks
 * @param maxSize Maximum cached players (0 < maxSize ≤ 1000)
 * @return New cache instance
 * @throws IllegalArgumentException If maxSize invalid
 */
public @NotNull PlayerCache createCache(int maxSize) { ...}
```

### Performance Annotations

```java

@HotSpotIntrinsicCandidate
public static native int fastHashCode(byte[] data);

@BenchmarkMode(Mode.AverageTime)
public void measurePathfinding() { ...}
```

---

## Compliance & Best Practices

1. **Immutability**: All fields `final` unless mutation required
2. **Thread Safety**:
   ```java
   private final ReentrantLock inventoryLock = new ReentrantLock();
   
   public void updateInventory() {
       inventoryLock.lock();
       try {
           // Critical section
       } finally {
           inventoryLock.unlock();
       }
   }
   ```
3. **Dependency Management**:
   ```java
   module com.example.plugin {
       requires org.bukkit;
       requires static org.jetbrains.annotations;
       exports com.example.plugin.api;
   }
   ```

---

## Example Implementation

```java
public final class TradeSystem implements Listener {
    // 1. Static variables
    private static final int MAX_TRADES = 100;

    // 2. Instance variables
    private final @NotNull Map<UUID, TradeOffer> activeTrades;

    // 3. Constructor
    public TradeSystem() {
        this.activeTrades = Collections.synchronizedMap(new LinkedHashMap<>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<UUID, TradeOffer> eldest) {
                return size() > MAX_TRADES;
            }
        });
    }

    // 4. Public methods
    @EventHandler
    public void onTradeInit(@NotNull PlayerTradeEvent event) {
        if (null == event.getInitiator()) return; // Yoda check

        if (event.getOffer().isBlank()) { // Natural boolean check
            event.setCancelled(true);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                processTradeAsync(event)
        );
    }

    private void processTradeAsync(@NotNull PlayerTradeEvent event) {
        // Async processing
    }

    // Fluent accessors
    public @NotNull Collection<TradeOffer> activeTrades() {
        return Collections.unmodifiableCollection(activeTrades.values());
    }
}
```

---

**Compliance Checklist**

- [ ] Yoda conditions for null/literal checks only
- [ ] `@NotNull`/`@Nullable` on all declarations
- [ ] Original code structure preserved
- [ ] No `get`/`set` prefixes unless required
- [ ] SOLID principles validated via interface analysis
- [ ] Java 21 features used where applicable
- [ ] PaperAPI async for I/O operations
- [ ] Javadoc explains performance trade-offs