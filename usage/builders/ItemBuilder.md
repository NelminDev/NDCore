# ItemBuilder

## Overview

A utility for creating and customizing ItemStacks with a fluent interface. This class uses Lombok's `@Data` annotation
to generate getters, setters, equals, hashCode, and toString methods.

## Key Features

- Material selection
- Custom display name and lore
- Enchantment application
- Item flags management
- Damage and durability control
- Custom model data
- Repair cost customization
- Persistent data tags
- Cooldown management
- Tooltip style customization
- Visibility and enchantability toggles
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
| `unbreakable(boolean unbreakable)`                                            | Sets whether the item is unbreakable             |
| `hideTooltip(boolean hideTooltip)`                                            | Sets whether to hide item attributes in tooltip  |
| `customModelData(Integer modelData)`                                          | Sets the custom model data for the item          |
| `damage(int damage)`                                                          | Sets the current damage value of the item        |
| `repairCost(int repairCost)`                                                  | Sets the repair cost of the item                 |
| `maxStackSize(int maxStackSize)`                                              | Sets the maximum stack size for this item        |
| `addStringTag(NamespacedKey key, String value)`                               | Adds a string tag to the item                    |
| `addIntTag(NamespacedKey key, int value)`                                     | Adds an integer tag to the item                  |
| `removeTag(NamespacedKey key)`                                                | Removes a tag from the item                      |
| `enchantable(boolean enchantable)`                                            | Sets whether the item can be enchanted           |
| `invisible(boolean invisible)`                                                | Sets whether the item is invisible in inventory  |
| `cooldown(Player player, int duration)`                                       | Sets the cooldown for the item for a player      |
| `durability(double multiplier)`                                               | Makes the item more durable                      |
| `useRemainder(Material remainderItem)`                                        | Sets the remainder item after using this item    |
| `tooltipStyle(boolean hideAttributes, ...)`                                   | Sets the tooltip style of the item               |
| `toItem()`                                                                    | Builds and returns the final ItemStack           |

## Usage Examples

```java
import dev.nelmin.ndcore.builders.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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

// Create a custom tool with advanced properties
ItemStack customPickaxe = new ItemBuilder(Material.NETHERITE_PICKAXE)
        .displayName(Component.text("§6Ancient Excavator"))
        .customModelData(1001)  // Custom model for resource pack
        .damage(10)             // Set initial damage
        .repairCost(30)         // Expensive to repair
        .durability(2.5)        // More durable than normal
        .addStringTag(new NamespacedKey("myserver", "special_tool"), "excavator")
        .tooltipStyle(true, false, false, true, false, false)
        .unbreakable(false)     // Not unbreakable, but more durable
        .toItem();

// Create a consumable item with cooldown
ItemStack cooldownPotion = new ItemBuilder(Material.POTION)
        .displayName(Component.text("Swift Potion"))
        .lore(List.of(Component.text("Grants temporary speed")))
        // .cooldown(somePlayer, 600)  // 30-second cooldown (20 ticks per second)
        .useRemainder(Material.GLASS_BOTTLE)  // Returns a bottle when used
        .invisible(false)       // Show all attributes
        .enchantable(false)     // Cannot be enchanted
        .toItem();

// You would apply cooldown when giving the item to a player:
// somePlayer.setCooldown(cooldownPotion.getType(), 600);
```

## Best Practices

- Use ItemBuilder to create complex items with many properties
- Chain method calls for cleaner, more readable code
- Take advantage of the fluent interface to configure items in a single statement
- Use the Lombok-generated getters and setters when needed
- Remember to call `toItem()` at the end to get the final ItemStack
- Use `displayName()` with Component objects for modern text handling
- Prefer `itemFlags()` for setting all flags at once, or `addItemFlags()` to add to existing flags
