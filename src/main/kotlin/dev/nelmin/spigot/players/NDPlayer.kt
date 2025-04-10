package dev.nelmin.spigot.players

import dev.nelmin.spigot.NDCore
import dev.nelmin.spigot.events.PlayerFreezeEvent
import dev.nelmin.spigot.events.PlayerUnfreezeEvent
import dev.nelmin.spigot.persistence.PersistentProperty
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

/**
 * Represents a custom player implementation utilizing persistent properties for managing
 * specific data tied to the player. This class delegates standard player operations to
 * the underlying `bukkitPlayer` instance.
 *
 * @param bukkitPlayer The underlying Bukkit `Player` object to delegate all standard player behaviors.
 */
open class NDPlayer(private val bukkitPlayer: Player) : Player by bukkitPlayer {
    /**
     * Represents the preferred language of the player.
     *
     * This property is backed by a `PersistentDataContainer` to ensure the value persists
     * across player sessions. It supports dynamic storage and retrieval of the language
     * setting, defaulting to `"en"` if no value is specified or stored.
     *
     * The language can be used to support localization or other language-specific features
     * for the player.
     */
    var language: String by PersistentProperty(
        "language",
        PersistentDataType.STRING,
        "en",
        persistentDataContainer
    )

    /**
     * Indicates whether the player is currently frozen in place.
     *
     * This property uses a persistent data store to track and update the frozen state of the player.
     * Modifying this property will update the corresponding value in the player's persistent data container.
     *
     * When set to `true`, the player is considered frozen and unable to move. Setting it to `false`
     * unfreezes the player, allowing movement.
     *
     * Delegates to `PersistentProperty` to manage data persistence within the provided data container,
     * with `false` as the default value.
     */
    var isFrozenInPlace: Boolean by PersistentProperty(
        "isFrozen",
        PersistentDataType.BOOLEAN,
        false,
        persistentDataContainer
    )

    /**
     * Freezes the player in place, preventing movement and triggering a PlayerFreezeEvent.
     *
     * @param message An optional message to be passed with the freeze event.
     *                This can be used for providing context or additional information about why the player is being frozen.
     */
    fun freeze(message: String? = null) {
        isFrozenInPlace = true
        NDCore.instance().pluginManager.callEvent(PlayerFreezeEvent(this, message))
    }

    /**
     * Unfreezes the player by setting the `isFrozenInPlace` state to `false` and triggering the `PlayerUnfreezeEvent`.
     *
     * @param message An optional message providing context for the unfreezing action. It may be used to inform
     *                the player or log additional details.
     */
    fun unfreeze(message: String? = null) {
        isFrozenInPlace = false
        NDCore.instance().pluginManager.callEvent(PlayerUnfreezeEvent(this, message))
    }
}