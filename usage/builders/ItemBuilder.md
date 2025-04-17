# ItemBuilder

## Overview

A utility for creating and customizing ItemStacks with a fluent interface.

## Key Features

- Material selection
- Custom display name and lore
- Enchantment application
- Item flags management
- NBT data manipulation

## Usage Examples

```java
import dev.nelmin.ndcore.builders.ItemBuilder;
import org.bukkit.inventory.ItemStack;

// Create a basic item
ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD)
        .name("&bFrost Blade")
        .lore("&7A sword forged from", "&7ancient ice crystals")
        .enchant(Enchantment.DAMAGE_ALL, 5)
        .flag(ItemFlag.HIDE_ATTRIBUTES)
        .build();
```

## Best Practices

- Use ItemBuilder to create complex items with many properties
- Chain method calls for cleaner, more readable code
- Take advantage of the fluent interface to configure items in a single statement