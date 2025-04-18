# SimpleMenu

## Overview

An abstract implementation of the Menu interface that handles most of the boilerplate code for creating interactive
menus in NDCore. This class must be extended, and the `onSetItems()` method must be implemented by subclasses.

## Key Features

- Pre-implemented click handling
- Built-in action mapping
- Enum-based inventory size selection
- Simplified item setting
- Support for both String and Component titles
- Automatic colorization of String titles using TextBuilder

## Usage Examples

```java
// Creating a basic menu with SimpleMenu
public class WarpMenu extends SimpleMenu {

    public WarpMenu() {
        super("Teleport Menu", Rows.THREE); // 3 rows (27 slots)
    }

    @Override
    public void onSetItems() {
        // Spawn warp
        setItem(10, new ItemBuilder(Material.BEACON)
            .name("&aSpawn")
            .lore("&7Click to teleport to spawn")
            .build(),
            player -> {
                player.teleport(player.getWorld().getSpawnLocation());
                player.sendMessage("Teleported to spawn!");
                player.closeInventory();
            }
        );

        // Mine warp
        setItem(12, new ItemBuilder(Material.IRON_PICKAXE)
            .name("&eMining World")
            .lore("&7Click to teleport to the mining world")
            .build(),
            player -> {
                World miningWorld = Bukkit.getWorld("mining");
                if (miningWorld != null) {
                    player.teleport(miningWorld.getSpawnLocation());
                    player.sendMessage("Teleported to mining world!");
                    player.closeInventory();
                }
            }
        );

        // PvP arena warp
        setItem(14, new ItemBuilder(Material.IRON_SWORD)
            .name("&cPvP Arena")
            .lore("&7Click to teleport to the PvP arena")
            .build(),
            player -> {
                Location pvpArena = new Location(Bukkit.getWorld("world"), 100, 64, 100);
                player.teleport(pvpArena);
                player.sendMessage("Teleported to PvP arena!");
                player.closeInventory();
            }
        );

        // Close button
        setItem(26, new ItemBuilder(Material.BARRIER)
            .name("&cClose Menu")
            .build(),
            Player::closeInventory
        );
    }
}

// Using the menu in your plugin
public void openWarpMenu(Player player) {
    WarpMenu warpMenu = new WarpMenu();
    warpMenu.open(player);
}

// Example using Component title constructor
public class ComponentTitleMenu extends SimpleMenu {

    public ComponentTitleMenu() {
        super(Component.text("Advanced Menu").color(TextColor.color(0x00AAFF)), Rows.THREE);
    }

    @Override
    public void onSetItems() {
        // Set menu items here
    }
}
```

## Creating Paginated Menus

For larger collections of items, you can create paginated menus:

```java
public class PaginatedMenu extends SimpleMenu {
    private final List<ItemStack> items;
    private int page = 0;
    private final int itemsPerPage = 45; // 5 rows of items

    public PaginatedMenu(List<ItemStack> items) {
        super("Items - Page 1", Rows.SIX); // 6 rows
        this.items = items;
    }

    @Override
    public void onSetItems() {
        // Clear previous items
        for (int i = 0; i < 54; i++) {
            inventory.clear(i);
        }

        // Calculate start and end indices
        int start = page * itemsPerPage;
        int end = Math.min(start + itemsPerPage, items.size());

        // Set items for current page
        int slot = 0;
        for (int i = start; i < end; i++) {
            setItem(slot++, items.get(i));
        }

        // Navigation buttons
        if (page > 0) {
            setItem(45, new ItemBuilder(Material.ARROW)
                .name("&aPrevious Page")
                .build(),
                player -> {
                    page--;
                    // Update title
                    // Note: In Paper, you'd need to recreate the inventory to change the title
                    onSetItems();
                }
            );
        }

        if (end < items.size()) {
            setItem(53, new ItemBuilder(Material.ARROW)
                .name("&aNext Page")
                .build(),
                player -> {
                    page++;
                    // Update title
                    onSetItems();
                }
            );
        }

        // Page indicator
        setItem(49, new ItemBuilder(Material.PAPER)
            .name("&ePage " + (page + 1) + "/" + ((items.size() - 1) / itemsPerPage + 1))
            .build()
        );
    }
}
```

## Best Practices

- Remember that SimpleMenu is abstract and must be extended
- Always implement the abstract onSetItems() method in your subclass
- Choose the appropriate constructor based on your title needs (String or Component)
- Use ItemBuilder to create visually consistent menu items
- Keep click handlers simple and focused
- Consider using constants for slot positions to improve readability
- Close inventories when appropriate to prevent player confusion
- For complex menus, create a dedicated class that extends SimpleMenu
- Use the Rows enum for clear inventory size specification
