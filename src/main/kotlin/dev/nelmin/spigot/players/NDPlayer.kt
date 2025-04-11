package dev.nelmin.spigot.players

import dev.nelmin.spigot.NDCore
import dev.nelmin.spigot.events.PlayerFreezeEvent
import dev.nelmin.spigot.events.PlayerUnfreezeEvent
import dev.nelmin.spigot.objects.LocalizedMessage
import dev.nelmin.spigot.persistence.PersistentListDataType
import dev.nelmin.spigot.persistence.PersistentMutableListProperty
import dev.nelmin.spigot.persistence.PersistentProperty
import kotlinx.datetime.Clock
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

/**
 * Represents a player in the NDCore system and provides additional properties and behaviors
 * beyond those of a standard Bukkit `Player`. This class uses Kotlin property delegation
 * to manage persistent player-specific data through `PersistentDataContainer`.
 *
 * @constructor Initializes an `NDPlayer` instance.
 * @property bukkitPlayer The underlying Bukkit `Player` instance.
 */
open class NDPlayer(val bukkitPlayer: Player) : Player by bukkitPlayer {
    /**
     * Represents a persistent property for storing and retrieving the preferred language of the player.
     *
     * The `language` property is backed by a `PersistentDataContainer` and is used to persist the player's
     * language preference across sessions. If no value is explicitly set, a default value of `"en"` (English)
     * is used. This property is implemented as a delegate using the `PersistentProperty` class, allowing
     * thread-safe read and write operations.
     */
    var language: String by PersistentProperty(
        "language",
        PersistentDataType.STRING,
        "en",
        persistentDataContainer
    )

    /**
     * Represents a persistent property that tracks whether the "message toggled" state is enabled or disabled.
     *
     * This property is stored in a `PersistentDataContainer` using the `PersistentProperty` delegate mechanism
     * and maintains its state across server restarts or player sessions.
     *
     * The value is of type `Boolean` and defaults to `false` if not explicitly set in the data container.
     *
     * Usage is tied to the owning class, providing automatic management of the message toggling state.
     */
    var msgToggled: Boolean by PersistentProperty(
        "msgToggled",
        PersistentDataType.BOOLEAN,
        false,
        persistentDataContainer
    )

    /**
     * Represents the rank of the player.
     *
     * This property is persisted within a `PersistentDataContainer` using a `PersistentProperty` delegate.
     * The rank is stored as a `String` and defaults to "default" if no value is explicitly set.
     *
     * The value of this property is asynchronously retrieved or updated in a thread-safe manner.
     * It allows for simplified management and persistence of the player's rank across sessions.
     */
    var rank: String by PersistentProperty(
        "rank",
        PersistentDataType.STRING,
        "default",
        persistentDataContainer
    )

    /**
     * Stores the persistent "vanish" state of the player.
     *
     * This property determines whether the player is in a vanished state
     * (hidden from others) or visible. The value is persisted using the
     * Bukkit `PersistentDataContainer` and defaults to `false` if not set.
     *
     * Delegated via the `PersistentProperty` to handle storing and retrieving
     * the value with thread safety and persistence capabilities.
     */
    private var _vanish: Boolean by PersistentProperty(
        "vanish",
        PersistentDataType.BOOLEAN,
        false,
        persistentDataContainer
    )

    /**
     * Represents the vanished state of the player.
     *
     * When set to `true`, the player is hidden from all other online players.
     * When set to `false`, the player becomes visible to other online players again.
     * Changes to this property automatically invoke methods to hide or show the player
     * for all connected players on the server.
     */
    var vanish: Boolean
        get() = _vanish
        set(value) {
            _vanish = value

            if (value)
                Bukkit.getOnlinePlayers().forEach { player -> player.hidePlayer(NDCore.instance(), bukkitPlayer) }
            else
                Bukkit.getOnlinePlayers().forEach { player -> player.showPlayer(NDCore.instance(), bukkitPlayer) }
        }

    /**
     * Represents the timestamp of the first time the player joined the server.
     *
     * This property is stored persistently using a `PersistentDataContainer`, allowing it to
     * retain its value across server restarts. It is initialized to the current system time in
     * milliseconds since epoch if no value is already set. The value can be updated or retrieved
     * programmatically and is represented as a `Long`.
     */
    var firstJoinTimestamp: Long by PersistentProperty(
        "firstJoinTimestamp",
        PersistentDataType.LONG,
        Clock.System.now().toEpochMilliseconds(),
        persistentDataContainer
    )

    /**
     * Represents a timestamp indicating the last time the player joined the server.
     *
     * This property is stored persistently using the player's `PersistentDataContainer`, allowing the data
     * to persist across server restarts or player relogs. The timestamp is expressed in milliseconds since
     * the epoch (1970-01-01T00:00:00Z), retrieved using the system clock at the time of instance initialization.
     *
     * The default value for this property is the current system time in milliseconds if no previous data
     * exists in the container.
     */
    var lastJoinTimestamp: Long by PersistentProperty(
        "lastJoinTimestamp",
        PersistentDataType.LONG,
        Clock.System.now().toEpochMilliseconds(),
        persistentDataContainer
    )

    /**
     * Tracks the total playtime of the player in milliseconds, stored persistently using a `PersistentDataContainer`.
     *
     * This property leverages the `PersistentProperty` delegate to store and retrieve data in a persistent
     * container, ensuring the value is retained across sessions. It represents the timestamp of the player's
     * accumulated playtime and updates automatically when needed. The default value is the current system
     * time in milliseconds at the moment of initialization.
     */
    private var _totalPlayTimeTimestamp: Long by PersistentProperty(
        "playTimeTimestamp",
        PersistentDataType.LONG,
        Clock.System.now().toEpochMilliseconds(),
        persistentDataContainer
    )

    /**
     * Represents the calculated total playtime of the player in milliseconds.
     * Combines the stored playtime (`_totalPlayTimeTimestamp`) and the duration since the last join (`lastJoinTimestamp`),
     * measured using the current system time.
     */
    val totalPlayTimeTimestamp: Long
        get() = _totalPlayTimeTimestamp + (Clock.System.now().toEpochMilliseconds() - lastJoinTimestamp)

    /**
     * Updates the player's total playtime by calculating the duration of the current session.
     * The session duration is determined by subtracting the last join timestamp from the current system time.
     * The calculated session duration is then added to the total playtime timestamp.
     */
    fun updatePlayTimeOnQuit() {
        val sessionDuration = Clock.System.now().toEpochMilliseconds() - lastJoinTimestamp
        _totalPlayTimeTimestamp += sessionDuration
    }

    /**
     * Represents the number of kills associated with a player.
     *
     * This property is backed by a `PersistentProperty` to store and retrieve the value
     * from a `PersistentDataContainer`, allowing it to persist across server restarts.
     * The default value is `0` if no data is found in the container.
     */
    var kills: Int by PersistentProperty(
        "kills",
        PersistentDataType.INTEGER,
        0,
        persistentDataContainer
    )

    /**
     * Tracks the number of mobs killed by the player.
     *
     * This property is persisted in the player's `PersistentDataContainer` and is linked
     * to the key "mobKills". It utilizes the `PersistentProperty` delegate to handle
     * the storage and retrieval of the value. By default, if no value is found, it will
     * initialize with 0.
     */
    var mobKills: Int by PersistentProperty(
        "mobKills",
        PersistentDataType.INTEGER,
        0,
        persistentDataContainer
    )

    /**
     * A property that represents the number of deaths for a player.
     *
     * This value is stored persistently in a `PersistentDataContainer`, allowing it to
     * be retained across server restarts or player sessions. The property is backed
     * by the `PersistentProperty` delegate, which ensures thread-safe access and modification.
     *
     * By default, the number of deaths is initialized to `0`.
     */
    var deaths: Int by PersistentProperty(
        "deaths",
        PersistentDataType.INTEGER,
        0,
        persistentDataContainer
    )

    /**
     * Stores the last known location where the player died.
     *
     * This property is persisted in the player's `PersistentDataContainer` and allows the
     * retrieval or update of the player's last death location across sessions. The location
     * is stored as a string in a predefined format.
     *
     * By default, the value is an empty string if the player has not died or no location
     * has been recorded yet.
     */
    var lastDeathLocation: String by PersistentProperty(
        "lastDeathLocation",
        PersistentDataType.STRING,
        "",
        persistentDataContainer
    )

    /**
     * Represents a mutable list of home locations associated with a player.
     *
     * This property is delegated by `PersistentMutableListProperty` to persistently store
     * and retrieve the list of homes in a `PersistentDataContainer`.
     *
     * Key characteristics:
     * - The list elements are of type `String`.
     * - Changes to this property are automatically persisted.
     * - Default value is an empty mutable list.
     * - Used for maintaining player-specific home locations in a persistent manner.
     */
    var homes: MutableList<String> by PersistentMutableListProperty(
        "homes",
        PersistentListDataType.STRING,
        mutableListOf<String>(),
        persistentDataContainer
    )

    /**
     * Indicates whether the player is currently frozen and unable to move.
     *
     * This property is stored persistently using a `PersistentDataContainer` and is
     * associated with the key "isFrozen." It controls whether the player's movement
     * is restricted and can be updated dynamically at runtime. The default value
     * for this property is `false`, meaning the player is not frozen by default.
     *
     * Modifications to this property will be synchronized with the underlying
     * persistent storage, ensuring consistent behavior across sessions.
     */
    var isFrozenInPlace: Boolean by PersistentProperty(
        "isFrozen",
        PersistentDataType.BOOLEAN,
        false,
        persistentDataContainer
    )

    /**
     * Freezes the player, preventing them from moving or interacting with the game world.
     * This method marks the player as frozen in place and triggers a `PlayerFreezeEvent`.
     *
     * @param message An optional message providing a reason or context for the freeze action.
     *                This message can be displayed to the player or used for logging purposes.
     */
    fun freeze(message: String? = null) {
        isFrozenInPlace = true
        NDCore.instance().pluginManager.callEvent(PlayerFreezeEvent(this, message))
    }

    /**
     * Unfreezes the player, allowing them to move freely in the game world.
     * This method updates the player's frozen state and triggers the `PlayerUnfreezeEvent`.
     *
     * @param message An optional string providing context or an explanation for unfreezing the player.
     *                This can be used for logging purposes or to notify the player of the reason.
     */
    fun unfreeze(message: String? = null) {
        isFrozenInPlace = false
        NDCore.instance().pluginManager.callEvent(PlayerUnfreezeEvent(this, message))
    }

    /**
     * Sends a localized message to the player based on their preferred language.
     *
     * If a translation for the specified language is not available, a fallback message
     * from the `LocalizedMessage` instance will be sent instead.
     *
     * @param localizedMessage the `LocalizedMessage` instance containing translations and fallback information
     */
    fun sendLocalizedMessage(localizedMessage: LocalizedMessage) {
        bukkitPlayer.sendMessage(localizedMessage[language] ?: localizedMessage.fallbackMessage())
    }

    /**
     * Tracks the number of mutes applied to the player.
     *
     * This property uses a `PersistentProperty` delegate to persist its value in a `PersistentDataContainer`.
     * It ensures that the mute count is saved and retrievable across player sessions, defaulting to `0` if no value is set.
     */
    var muteCount: Int by PersistentProperty(
        "muteCount",
        PersistentDataType.INTEGER,
        0,
        persistentDataContainer
    )

    /**
     * Tracks the number of warnings a player has received.
     *
     * This property is persisted in the player's `PersistentDataContainer` and allows
     * tracking of warnings across sessions. If no value is present, it defaults to `0`.
     *
     * The value is managed with thread-safe read and write operations, ensuring consistency
     * in concurrent environments.
     */
    var warnCount: Int by PersistentProperty(
        "warnCount",
        PersistentDataType.INTEGER,
        0,
        persistentDataContainer
    )

    /**
     * Represents the count of bans issued to a player.
     *
     * This property is backed by a `PersistentProperty` to ensure that the value is persisted
     * across sessions using the `PersistentDataContainer`. It keeps track of the total number
     * of bans associated with a player and can be updated or retrieved as needed.
     *
     * The property is initialized with a default value of `0` if no value is found
     * in the storage container.
     */
    var banCount: Int by PersistentProperty(
        "banCount",
        PersistentDataType.INTEGER,
        0,
        persistentDataContainer
    )
}