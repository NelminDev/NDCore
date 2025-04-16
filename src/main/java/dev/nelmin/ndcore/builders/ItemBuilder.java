package dev.nelmin.ndcore.builders;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder class for creating and customizing {@link ItemStack} instances in Bukkit. <p>
 * This class provides a fluent API for modifying items with various attributes such as
 * display name, enchantments, lore, item flags, and unbreakable status. <p>
 * It serves as a utility for simplifying the creation of complex item setups in Minecraft plugins.
 */
@Getter
@Setter
public class ItemBuilder {
    private ItemStack rawItemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(@NotNull Material material, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.rawItemStack = new ItemStack(material, amount);
        this.itemMeta = this.rawItemStack.getItemMeta();
    }

    public ItemBuilder displayName(@NotNull Component displayName) {
        if (displayName.equals(Component.empty())) {
            throw new IllegalArgumentException("Display name cannot be empty");
        }
        itemMeta.displayName(displayName);
        return this;
    }

    public ItemBuilder enchant(@NotNull Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        if (level < 1) {
            throw new IllegalArgumentException("Enchantment level must be greater than 0");
        }
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }

    public ItemBuilder itemFlags(@NotNull ItemFlag... flags) {
        if (flags.length == 0) {
            throw new IllegalArgumentException("At least one ItemFlag must be provided");
        }
        itemMeta.removeItemFlags(itemMeta.getItemFlags().toArray(new ItemFlag[0]));
        itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder addItemFlags(@NotNull ItemFlag... flags) {
        if (flags.length == 0) {
            throw new IllegalArgumentException("At least one ItemFlag must be provided");
        }
        itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder lore(@NotNull List<Component> lore) {
        if (lore.isEmpty()) {
            throw new IllegalArgumentException("Lore list cannot be empty");
        }
        if (lore.stream().anyMatch(component -> component.equals(Component.empty()))) {
            throw new IllegalArgumentException("Lore cannot contain empty components");
        }
        itemMeta.lore(lore);
        return this;
    }

    public ItemBuilder addLoreLine(@NotNull Component line) {
        if (line.equals(Component.empty())) {
            throw new IllegalArgumentException("Lore line cannot be empty");
        }
        List<Component> lore = itemMeta.lore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(line);
        itemMeta.lore(lore);
        return this;
    }

    public ItemStack toItem() {
        this.rawItemStack.setItemMeta(itemMeta);
        return this.rawItemStack;
    }
}