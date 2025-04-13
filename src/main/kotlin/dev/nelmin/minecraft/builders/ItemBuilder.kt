package dev.nelmin.minecraft.builders

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/**
 * Builder class for creating and customizing ItemStacks in a fluent and flexible way.
 * Allows setting various attributes such as display name, lore, enchantments,
 * custom model data, and more.
 *
 * @constructor Creates an instance of ItemBuilder with a specified material and amount.
 *
 * @param material The material of the item.
 * @param amount The quantity of the item in the stack. Defaults to 1.
 */
open class ItemBuilder(material: Material, amount: Int = 1) {
    /**
     * Represents the stack of items being constructed or manipulated by the `ItemBuilder` class.
     *
     * This variable holds an instance of `ItemStack`, which specifies the material and amount
     * of the items in the stack. It serves as the base structure for adding metadata, enchantments,
     * custom model data, and other modifications through the methods provided by the `ItemBuilder`.
     */
    val itemStack: ItemStack = ItemStack(material, amount)

    /**
     * Represents the metadata of the item being constructed within the [ItemBuilder].
     * This variable is initialized from the associated `ItemStack`'s metadata and is utilized
     * to set or modify properties such as display name, lore, enchantments, item flags, and other
     * attributes of the item.
     */
    private val itemMeta: ItemMeta = itemStack.itemMeta!!

    /**
     * Sets the display name of the item being built.
     *
     * @param displayName The new display name to set for the item, represented as a `Component`.
     * @return The current instance of [ItemBuilder], allowing for method chaining.
     */
    fun setDisplayName(displayName: Component): ItemBuilder {
        itemMeta.displayName(displayName)
        return this
    }

    /**
     * Adds a line to the item's lore. If the lore is not already present, it initializes a new lore list.
     *
     * @param line The `Component` representing the line to be added to the item's lore.
     * @return The current instance of [ItemBuilder] to allow for method chaining.
     */
    fun addLoreLine(line: Component): ItemBuilder {
        val lore = itemMeta.lore() ?: mutableListOf()
        lore.add(line)
        itemMeta.lore(lore)
        return this
    }

    /**
     * Sets the lore of the item in the `ItemBuilder` with the provided list of `Component` objects.
     *
     * @param lore A list of `Component` objects representing the lines of lore to set for the item.
     * @return The current instance of `ItemBuilder`, allowing for method chaining.
     */
    fun setLore(lore: List<Component>): ItemBuilder {
        itemMeta.lore(lore)
        return this
    }

    /**
     * Adds an enchantment to the item with the specified level, optionally ignoring level restrictions.
     *
     * @param enchantment The enchantment to apply to the item.
     * @param level The level of the enchantment to add.
     * @param ignoreLevelRestriction If true, allows adding the enchantment even if the level exceeds the normal allowed maximum.
     * @return The current instance of [ItemBuilder], allowing for method chaining.
     */
    fun addEnchant(enchantment: Enchantment, level: Int, ignoreLevelRestriction: Boolean): ItemBuilder {
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction)
        return this
    }

    /**
     * Removes all enchantments from the item's metadata.
     *
     * @return The current instance of [ItemBuilder], allowing for method chaining.
     */
    fun clearEnchantments(): ItemBuilder {
        itemMeta.removeEnchantments()
        return this
    }

    /**
     * Adds a glowing effect to the item by applying the Unbreaking enchantment at level 1 and hiding
     * the enchantment indicator in the item's tooltip. If the item already has enchantments, no changes
     * will be made.
     *
     * @return The current instance of [ItemBuilder], allowing for method chaining.
     */
    fun glow(): ItemBuilder {
        if (itemMeta.enchants.isEmpty()) {
            itemMeta.addEnchant(Enchantment.UNBREAKING, 1, true)
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        return this
    }

    /**
     * Sets a custom model data value for the associated item.
     *
     * @param data An integer representing the custom model data to be set.
     * @return The current instance of [ItemBuilder], enabling method chaining.
     */
    fun setCustomModelData(data: Int): ItemBuilder {
        itemMeta.setCustomModelData(data)
        return this
    }

    /**
     * Adds one or more [ItemFlag] values to the item's metadata, which can be used to suppress certain item features
     * from being displayed in the item's tooltip.
     *
     * @param flags A vararg parameter representing the [ItemFlag] values to be added to the item.
     *              Each [ItemFlag] specifies a particular feature to be hidden.
     * @return The current instance of [ItemBuilder], allowing for method chaining.
     */
    fun addItemFlags(vararg flags: ItemFlag): ItemBuilder {
        itemMeta.addItemFlags(*flags)
        return this
    }

    /**
     * Sets the unbreakable property of the item.
     *
     * @param unbreakable A boolean indicating whether the item should be unbreakable.
     * @return The current instance of [ItemBuilder] for method chaining.
     */
    fun setUnbreakable(unbreakable: Boolean): ItemBuilder {
        itemMeta.isUnbreakable = unbreakable
        return this
    }

    /**
     * Combines the `itemMeta` with the `itemStack` and returns the constructed `ItemStack`.
     *
     * @return The `ItemStack` resulting from adding the `itemMeta` to the `itemStack`.
     */
    open fun toItem(): ItemStack {
        itemStack.itemMeta = itemMeta
        return itemStack
    }

    /**
     * A subclass of ItemBuilder that is specialized for creating and modifying player head items.
     * This class provides methods to set the owner of the player head using a player name,
     * an OfflinePlayer instance, or a UUID.
     */
    class Head : ItemBuilder(Material.PLAYER_HEAD) {
        /**
         * Stores the `SkullMeta` of the associated `ItemStack`, allowing for modifications specific
         * to player heads, such as setting the owning player.
         *
         * This property directly casts the `ItemMeta` of the `ItemStack` to `SkullMeta`, assuming
         * the `ItemStack` is of the type `PLAYER_HEAD`. Any changes made to this `SkullMeta`
         * instance will reflect back onto the associated `ItemStack`.
         */
        private val skullMeta: SkullMeta = itemStack.itemMeta as SkullMeta

        /**
         * Sets the owner of the player head using the provided owner's username.
         *
         * @param owner The username of the player whose head this item should represent.
         * @return The updated [Head] instance with the owner set.
         * @deprecated Since version 1.12.1. Use [setOwner] with a UUID instead.
         */
        @Deprecated(
            "since 1.12.1",
            ReplaceWith("setOwner(uuid)", "dev.nelmin.minecraft.builders.ItemBuilder.Head.setOwner"),
            DeprecationLevel.WARNING
        )
        fun setOwner(owner: String): Head {
            skullMeta.owningPlayer = Bukkit.getOfflinePlayer(owner)
            return this
        }

        /**
         * Sets the owning player for the head.
         *
         * @param owner The [OfflinePlayer] to set as the owner of the head.
         * @return The current instance of [Head], allowing for method chaining.
         */
        fun setOwner(owner: OfflinePlayer): Head {
            skullMeta.owningPlayer = owner
            return this
        }

        /**
         * Sets the owner of the player head to the specified UUID.
         *
         * @param uuid The UUID of the player to set as the owner of the player head.
         * @return The current instance of [Head], allowing for method chaining.
         */
        fun setOwner(uuid: UUID): Head {
            skullMeta.owningPlayer = Bukkit.getOfflinePlayer(uuid)
            return this
        }

        /**
         * Converts the current instance of `Head` into an `ItemStack`.
         *
         * @return An `ItemStack` representing the `Head` item, fully configured with its metadata.
         */
        override fun toItem(): ItemStack {
            itemStack.itemMeta = skullMeta
            return itemStack
        }
    }
}