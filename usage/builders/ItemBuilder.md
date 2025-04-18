# ItemBuilder

## Overview

A utility for creating and customizing ItemStacks with a fluent interface. This class uses Lombok's `@Data` annotation
to generate getters, setters, equals, hashCode, and toString methods.

## Key Features

- Material selection
- Custom display name and lore
- Enchantment application
- Item flags management
- Immutable builder pattern
- Lombok-powered data handling

## Methods

| Method                                                                        | Description                                      |
|-------------------------------------------------------------------------------|--------------------------------------------------|
| `material(Material material)`                                                 | Sets the material type of the item               |
| `amount(int amount)`                                                          | Sets the amount of items in the stack            |
| `displayName(Component displayName)`                                          | Sets the display name of the item                |
| `enchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction)` | Adds an enchantment to the item                  |
| `itemFlags(ItemFlag... flags)`                                                | Sets the item flags, removing any existing flags |
| `addItemFlags(ItemFlag... flags)`                                             | Adds item flags to existing flags                |
| `lore(List<Component> lore)`                                                  | Sets the lore of the item                        |
| `addLoreLine(Component line)`                                                 | Adds a single line to the item's lore            |
| `toItem()`                                                                    | Builds and returns the final ItemStack           |

## Usage Examples

```java
import dev.nelmin.ndcore.builders.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

// Create a basic item
ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD)
        .displayName(Component.text("Frost Blade"))
        .lore(List.of(
                Component.text("A sword forged from"),
                Component.text("ancient ice crystals")
        ))
        .enchant(Enchantment.DAMAGE_ALL, 5, true)
        .itemFlags(ItemFlag.HIDE_ATTRIBUTES)
        .toItem();

        // Create an item with multiple enchantments and flags
        ItemStack magicBow = new ItemBuilder(Material.BOW)
                .displayName(Component.text("Magic Bow"))
                .amount(1)
                .enchant(Enchantment.ARROW_DAMAGE, 3, false)
                .enchant(Enchantment.ARROW_INFINITE, 1, false)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES)
                .toItem();
```

## Best Practices

- Use ItemBuilder to create complex items with many properties
- Chain method calls for cleaner, more readable code
- Take advantage of the fluent interface to configure items in a single statement
- Use the Lombok-generated getters and setters when needed
- Remember to call `toItem()` at the end to get the final ItemStack
- Use `displayName()` with Component objects for modern text handling
- Prefer `itemFlags()` for setting all flags at once, or `addItemFlags()` to add to existing flags
