package dev.nelmin.spigot.builders

import dev.nelmin.spigot.NDCore
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/**
 * A utility class for constructing and modifying `ItemStack` objects in a fluent and readable manner.
 * Provides methods for customizing item properties such as name, lore, enchantments, and additional metadata.
 *
 * @param material The material type of the `ItemStack`.
 * @param amount The quantity of items in the stack. Defaults to 1.
 */
open class ItemBuilder(material: Material, amount: Int = 1) {
    /**
     * Represents the internal mutable `ItemStack` instance being constructed and customized by the `ItemBuilder`.
     *
     * This variable holds the foundational item stack object, which can be modified through various builder methods
     * such as setting the item's metadata, enchantments, lore, flags, and more. The final state of `itemStack` is
     * returned by the `toItem()` method, providing the fully customized item.
     */
    val itemStack: ItemStack = ItemStack(material, amount)

    /**
     * A reference to the `ItemMeta` of the `itemStack` being manipulated by the `ItemBuilder`.
     *
     * This property is used to modify various metadata attributes of an item, such as its
     * display name, lore, enchantments, custom model data, item flags, and more, based on
     * the provided fluent API methods within the `ItemBuilder`.
     *
     * The `itemMeta` is always non-null, as it is initialized with the `ItemMeta` obtained
     * from the `itemStack` at the time of the `ItemBuilder`'s creation.
     */
    private val itemMeta: ItemMeta = itemStack.itemMeta!!

    /**
     * Sets the display name for the item.
     *
     * @param displayName The string to be used as the new display name for the item.
     * @return The current instance of [ItemBuilder] to allow for method chaining.
     */
    fun setDisplayName(displayName: String): ItemBuilder {
        itemMeta.setDisplayName(displayName)
        return this
    }

    /**
     * Adds a new line to the lore of the item metadata.
     *
     * The lore is a list of strings used to display additional information about the item.
     * If the item's lore is not already initialized, it creates a new mutable list before adding the line.
     *
     * @param line The string to be added as a new line to the item's lore.
     * @return The current instance of [ItemBuilder] for chaining further modifications.
     */
    fun addLoreLine(line: String): ItemBuilder {
        val lore = itemMeta.lore ?: mutableListOf()
        lore.add(line)
        itemMeta.lore = lore
        return this
    }

    /**
     * Sets the lore (a list of descriptive strings) for the item being built.
     *
     * @param lore A list of strings representing the lore to be applied to the item.
     * @return The current instance of [ItemBuilder] for chaining further modifications.
     */
    fun setLore(lore: List<String>): ItemBuilder {
        itemMeta.lore = lore
        return this
    }

    /**
     * Adds an enchantment to the item with the specified level and level restriction settings.
     *
     * @param enchantment The enchantment to be added to the item.
     * @param level The level of the enchantment to be applied.
     * @param ignoreLevelRestriction Whether to ignore the level restrictions for the enchantment (true to ignore, false otherwise).
     * @return The current instance of [ItemBuilder] for method chaining.
     */
    fun addEnchant(enchantment: Enchantment, level: Int, ignoreLevelRestriction: Boolean): ItemBuilder {
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction)
        return this
    }

    /**
     * Clears all enchantments from the item associated with this `ItemBuilder` instance.
     *
     * @return This `ItemBuilder` instance for method chaining.
     */
    fun clearEnchantments(): ItemBuilder {
        itemMeta.removeEnchantments()
        return this
    }

    /**
     * Applies a glowing effect to the item by adding the `UNBREAKING` enchantment (with level 1)
     * and hiding the enchantment flag if no enchantments are already present.
     *
     * @return The current instance of [ItemBuilder] for method chaining.
     */
    fun glow(): ItemBuilder {
        if (itemMeta.enchants.isEmpty()) {
            itemMeta.addEnchant(Enchantment.UNBREAKING, 1, true)
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        return this
    }

    /**
     * Sets the custom model data for the item being built.
     *
     * @param data The integer value representing the custom model data to be set.
     * @return The current instance of [ItemBuilder] for method chaining.
     */
    fun setCustomModelData(data: Int): ItemBuilder {
        itemMeta.setCustomModelData(data)
        return this
    }

    /**
     * Adds the specified item flags to the item's metadata.
     *
     * This method allows adding one or more `ItemFlag`s to the item's metadata,
     * which can be used to hide certain attributes or properties of the item
     * when viewed in the inventory by a player.
     *
     * @param flags The item flags to be added to the item's metadata.
     *              Multiple flags can be specified as vararg parameters.
     * @return The current instance of `ItemBuilder` for method chaining.
     */
    fun addItemFlags(vararg flags: ItemFlag): ItemBuilder {
        itemMeta.addItemFlags(*flags)
        return this
    }

    /**
     * Sets the unbreakable status for the item being built.
     *
     * @param unbreakable A boolean indicating whether the item should be unbreakable (true) or not (false).
     * @return The current instance of [ItemBuilder] to allow for method chaining.
     */
    fun setUnbreakable(unbreakable: Boolean): ItemBuilder {
        itemMeta.isUnbreakable = unbreakable
        return this
    }

    /**
     * Converts the current state of the `ItemBuilder` into an `ItemStack` with applied metadata.
     *
     * @return The constructed `ItemStack` with properties and metadata set using the `ItemBuilder`.
     */
    open fun toItem(): ItemStack {
        itemStack.itemMeta = itemMeta
        return itemStack
    }

    /**
     * A builder class for creating and customizing `PLAYER_HEAD` items in Minecraft.
     *
     * The `Head` class extends the functionality of the `ItemBuilder` class, specifically designed
     * to construct and manipulate player head items (`Material.PLAYER_HEAD`) with additional options
     * for setting ownership and customizing attributes.
     */
    class Head : ItemBuilder(Material.PLAYER_HEAD) {
        /**
         * Represents the metadata for a player head item in Minecraft.
         *
         * This property is cast from the item stack's metadata and provides access to specific methods and attributes
         * for modifying the player head, such as setting the owner of the skull.
         */
        private val skullMeta: SkullMeta = itemStack.itemMeta as SkullMeta

        /**
         * Sets the owner of the player head using the provided player's name.
         *
         * @param owner The name of the player whose head should be set as the owner.
         * @return The current instance of [Head] with the updated owner.
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
         * Sets the owner of the player head to the specified OfflinePlayer.
         *
         * @param owner The [OfflinePlayer] to be set as the owner of the player head.
         * @return The current instance of [Head] to allow for method chaining.
         */
        fun setOwner(owner: OfflinePlayer): Head {
            skullMeta.owningPlayer = owner
            return this
        }

        /**
         * Sets the owner of the player head based on the provided UUID.
         *
         * @param uuid The UUID of the player whose head will be set as the owner.
         * @return The current instance of [Head] for method chaining.
         */
        fun setOwner(uuid: UUID): Head {
            skullMeta.owningPlayer = NDCore.instance.server.getOfflinePlayer(uuid)
            return this
        }

        /**
         * Converts the current builder object into an instance of ItemStack, applying the configured item metadata.
         *
         * @return The resulting ItemStack instance with the applied metadata.
         */
        override fun toItem(): ItemStack {
            itemStack.itemMeta = skullMeta
            return itemStack
        }
    }
}