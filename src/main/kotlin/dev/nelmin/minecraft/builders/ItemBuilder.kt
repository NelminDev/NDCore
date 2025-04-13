package dev.nelmin.minecraft.builders

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
 *
 */
open class ItemBuilder(material: Material, amount: Int = 1) {
    /**
     * Represents the underlying `ItemStack` object being built or modified by the `ItemBuilder`.
     * This variable is used to store and manage the state of the item, including its material type,
     * amount, meta data, and other properties like enchantments, lore, and flags.
     */
    val itemStack: ItemStack = ItemStack(material, amount)

    /**
     * Holds the metadata of the `ItemStack` being built.
     * Used to configure various properties of the `ItemStack` such as display name, lore,
     * custom model data, enchantments, and more.
     */
    private val itemMeta: ItemMeta = itemStack.itemMeta!!

    /**
     * Sets the display name for the item being built.
     *
     * @param displayName The display name to set on the item.
     * @return The current instance of [ItemBuilder] to allow method chaining.
     */
    fun setDisplayName(displayName: String): ItemBuilder {
        itemMeta.setDisplayName(displayName)
        return this
    }

    /**
     * Adds a new line to the item's lore.
     *
     * @param line The line of text to add to the item's lore.
     * @return The current instance of [ItemBuilder] for method chaining.
     */
    fun addLoreLine(line: String): ItemBuilder {
        val lore = itemMeta.lore ?: mutableListOf()
        lore.add(line)
        itemMeta.lore = lore
        return this
    }

    /**
     * Sets the lore (additional text information) for the item.
     *
     * @param lore A list of strings representing the lore to be displayed on the item.
     * @return The current instance of [ItemBuilder] for method chaining.
     */
    fun setLore(lore: List<String>): ItemBuilder {
        itemMeta.lore = lore
        return this
    }

    /**
     * Adds an enchantment to the item with the specified level and optionally ignores level restrictions.
     *
     * @param enchantment The enchantment to add to the item.
     * @param level The level of the enchantment to apply.
     * @param ignoreLevelRestriction A boolean indicating whether to bypass the enchantment level restrictions.
     * @return The current instance of [ItemBuilder], for method chaining.
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
     * Adds a "glow" effect to the item by applying a hidden unbreaking enchantment if no enchantments are present.
     *
     * @return The current instance of [ItemBuilder] to allow method chaining.
     */
    fun glow(): ItemBuilder {
        if (itemMeta.enchants.isEmpty()) {
            itemMeta.addEnchant(Enchantment.UNBREAKING, 1, true)
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        return this
    }

    /**
     *
     */
    fun setCustomModelData(data: Int): ItemBuilder {
        itemMeta.setCustomModelData(data)
        return this
    }

    /**
     * Adds the specified item flags to the item's metadata.
     *
     * Item flags allow certain properties or visual elements of the item to be hidden
     * when viewed in the player's inventory. This can be used to control what information
     * is displayed to the player, such as enchantments, attributes, or*/
    fun addItemFlags(vararg flags: ItemFlag): ItemBuilder {
        itemMeta.addItemFlags(*flags)
        return this
    }

    /**
     * Sets whether the item should be unbreakable.
     *
     * @param unbreakable A boolean indicating whether the item should be unbreakable.
     * @return The current instance of [ItemBuilder] for method chaining.
     */
    fun setUnbreakable(unbreakable: Boolean): ItemBuilder {
        itemMeta.isUnbreakable = unbreakable
        return this
    }

    /**
     * Converts the current state of the `ItemBuilder` to an `ItemStack` with the configured properties.
     *
     * @return An `ItemStack` instance that reflects the properties set in the `ItemBuilder`.
     */
    open fun toItem(): ItemStack {
        itemStack.itemMeta = itemMeta
        return itemStack
    }

    /**
     * A specialized implementation of the `ItemBuilder` class for constructing `PLAYER_HEAD` items.
     * Provides convenience methods to set the player head's owner using different input types.
     */
    class Head : ItemBuilder(Material.PLAYER_HEAD) {
        /**
         * Represents the metadata of a player head item (`SkullMeta`) for customization.
         * This variable holds the current state of the SkullMeta associated with the item stack.
         *
         * Used within the [Head] class to modify or manage properties such as owner details
         * for player heads in Minecraft.
         */
        private val skullMeta: SkullMeta = itemStack.itemMeta as SkullMeta

        /**
         *
         */
        @Deprecated(
            "since 1.12.1",
            ReplaceWith("setOwner(uuid)", "dev.nelmin.minecraft.builders.ItemBuilder.Head.setOwner"),
            DeprecationLevel.WARNING
        )
        fun setOwner(owner: String): Head {
            skullMeta.owner = owner
            return this
        }

        /**
         * Sets the owner of the head item to the provided OfflinePlayer instance.
         *
         * @param owner The OfflinePlayer to be set as the owner of the head.
         * @return The current instance of [Head] for method chaining.
         */
        fun setOwner(owner: OfflinePlayer): Head {
            skullMeta.owningPlayer = owner
            return this
        }

        /**
         * Sets the owner of the player head using the specified UUID.
         *
         * @param uuid The UUID of the player to be set as the owner of the player head.
         * @return The current instance of [Head] to allow method chaining.
         */
        fun setOwner(uuid: UUID): Head {
            skullMeta.owningPlayer = Bukkit.getOfflinePlayer(uuid)
            return this
        }

        /**
         * Converts the current instance into an ItemStack with updated metadata.
         *
         * @return The ItemStack representation of the current item with its metadata set.
         */
        override fun toItem(): ItemStack {
            itemStack.itemMeta = skullMeta
            return itemStack
        }
    }
}