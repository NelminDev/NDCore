package dev.nelmin.ndcore.builders;

import lombok.Data;
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
 * A builder class for creating and customizing {@link ItemStack} instances in Bukkit.
 *
 * <p>This class provides a fluent API for modifying items with various attributes such as
 * display name, enchantments, lore, item flags, and unbreakable status.
 *
 * <p>It serves as a utility for simplifying the creation of complex item setups in Minecraft plugins.
 *
 * @implNote This class is immutable once constructed and all builder methods return the same instance
 */
@Data
public class ItemBuilder {
    private ItemStack rawItemStack;
    private ItemMeta itemMeta;

    /**
     * Creates a new ItemBuilder for the specified material and amount.
     *
     * @param material The material type for the item
     * @param amount   The amount of items in the stack
     * @throws IllegalArgumentException if amount is less than or equal to 0
     */
    public ItemBuilder(@NotNull Material material, int amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.rawItemStack = new ItemStack(material, amount);
        this.itemMeta = this.rawItemStack.getItemMeta();
    }

    /**
     * Sets the display name of the item.
     *
     * @param displayName The display name to set
     * @return This builder instance
     * @throws IllegalArgumentException if displayName is empty
     */
    public ItemBuilder displayName(@NotNull Component displayName) throws IllegalArgumentException {
        if (displayName.equals(Component.empty())) {
            throw new IllegalArgumentException("Display name cannot be empty");
        }
        itemMeta.displayName(displayName);
        return this;
    }

    /**
     * Adds an enchantment to the item.
     *
     * @param enchantment            The enchantment to add
     * @param level                  The level of the enchantment
     * @param ignoreLevelRestriction Whether to ignore vanilla level restrictions
     * @return This builder instance
     * @throws IllegalArgumentException if level is less than 1
     */
    public ItemBuilder enchant(@NotNull Enchantment enchantment, int level, boolean ignoreLevelRestriction) throws IllegalArgumentException {
        if (level < 1) {
            throw new IllegalArgumentException("Enchantment level must be greater than 0");
        }
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }

    /**
     * Sets the item flags, removing any existing flags.
     *
     * @param flags The flags to set
     * @return This builder instance
     * @throws IllegalArgumentException if no flags are provided
     */
    public ItemBuilder itemFlags(@NotNull ItemFlag... flags) throws IllegalArgumentException {
        if (flags.length == 0) {
            throw new IllegalArgumentException("At least one ItemFlag must be provided");
        }
        itemMeta.removeItemFlags(itemMeta.getItemFlags().toArray(new ItemFlag[0]));
        itemMeta.addItemFlags(flags);
        return this;
    }

    /**
     * Adds item flags to existing flags.
     *
     * @param flags The flags to add
     * @return This builder instance
     * @throws IllegalArgumentException if no flags are provided
     */
    public ItemBuilder addItemFlags(@NotNull ItemFlag... flags) throws IllegalArgumentException {
        if (flags.length == 0) {
            throw new IllegalArgumentException("At least one ItemFlag must be provided");
        }
        itemMeta.addItemFlags(flags);
        return this;
    }

    /**
     * Sets the lore of the item, replacing any existing lore.
     *
     * @param lore The list of lore lines to set
     * @return This builder instance
     * @throws IllegalArgumentException if lore list is empty or contains empty components
     */
    public ItemBuilder lore(@NotNull List<Component> lore) throws IllegalArgumentException {
        if (lore.isEmpty()) {
            throw new IllegalArgumentException("Lore list cannot be empty");
        }
        if (lore.stream().anyMatch(component -> component.equals(Component.empty()))) {
            throw new IllegalArgumentException("Lore cannot contain empty components");
        }
        itemMeta.lore(lore);
        return this;
    }

    /**
     * Adds a single line to the item's lore.
     *
     * @param line The line to add to the lore
     * @return This builder instance
     * @throws IllegalArgumentException if the line is empty
     */
    public ItemBuilder addLoreLine(@NotNull Component line) throws IllegalArgumentException {
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

    /**
     * Builds and returns the final ItemStack.
     *
     * @return The constructed ItemStack with all applied modifications
     */
    public ItemStack toItem() {
        this.rawItemStack.setItemMeta(itemMeta);
        return this.rawItemStack;
    }
}