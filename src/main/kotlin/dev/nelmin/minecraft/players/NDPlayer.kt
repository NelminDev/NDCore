package dev.nelmin.minecraft.players

import dev.nelmin.minecraft.NDCore
import dev.nelmin.minecraft.events.PlayerFreezeEvent
import dev.nelmin.minecraft.events.PlayerUnfreezeEvent
import dev.nelmin.minecraft.objects.LocalizedMessage
import dev.nelmin.minecraft.persistence.PersistentProperty
import kotlinx.datetime.Clock
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

/**
 * Represents an extended player entity for managing custom player data and behaviors.
 * This class delegates standard player functionality to the wrapped Bukkit `Player` instance
 * and provides additional properties and methods for handling custom persistent data and actions.
 *
 * @property bukkitPlayer The underlying Bukkit `Player` instance.
 */
open class NDPlayer(val bukkitPlayer: Player) : Player by bukkitPlayer {
    /**
     * Stores the player's preferred language setting.
     *
     * This property persists the language configuration using a `PersistentDataContainer`, allowing
     * it to be retained across server restarts or player sessions. By default, it is set to "en"
     * (English) unless another value is explicitly set.
     *
     * Delegated by the `PersistentProperty` class to handle storage and retrieval efficiently.
     */
    var language: String by PersistentProperty(
        "language",
        PersistentDataType.STRING,
        "en",
        persistentDataContainer
    )

    /**
     * Indicates whether messages are toggled for the player.
     *
     * This is a persistent property stored in the `PersistentDataContainer` associated with the player.
     * The property default value is `false`, meaning messages are not toggled unless explicitly set.
     *
     * It is utilized to manage message-related preferences for the player.
     */
    var msgToggled: Boolean by PersistentProperty(
        "msgToggled",
        PersistentDataType.BOOLEAN,
        false,
        persistentDataContainer
    )

    /**
     * Represents the rank of the player as a persistent property.
     *
     * The rank is stored in a `PersistentDataContainer` and is automatically persisted across sessions.
     * It is initialized with a default value of "default" if no value is already present in the container.
     * The property is backed by the `PersistentProperty` delegate, which ensures thread-safe
     * retrieval and storage of data.
     */
    var rank: String by PersistentProperty(
        "rank",
        PersistentDataType.STRING,
        "default",
        persistentDataContainer
    )

    /**
     * A private backing property used to persist the "vanish" state of the player.
     *
     * This property determines whether the player is in a vanished state, meaning they
     * are hidden from other players. The value is backed by a `PersistentDataContainer`
     * using the `PersistentProperty` delegate. It provides automatic persistence and
     * retrieval of the vanish state across game sessions.
     *
     * Default value: `false`
     *
     * Uses:
     * - `namespacedKeyName`: "vanish"
     * - `type`: `PersistentDataType.BOOLEAN`
     */
    private var _vanish: Boolean by PersistentProperty(
        "vanish",
        PersistentDataType.BOOLEAN,
        false,
        persistentDataContainer
    )

    /**
     * Determines whether the player is in vanish mode.
     *
     * When set to `true`, the player is hidden from all online players,
     * effectively making them invisible. When set to `false`, the player
     * becomes visible to all online players.
     *
     * Changing this value automatically updates the visibility state
     * of the player for all currently online players on the server.
     */
    var vanish: Boolean
        get() = _vanish
        set(value) {
            _vanish = value

            if (value)
                Bukkit.getOnlinePlayers()
                    .forEach { player -> player.hidePlayer(JavaPlugin.getPlugin(NDCore::class.java), bukkitPlayer) }
            else
                Bukkit.getOnlinePlayers()
                    .forEach { player -> player.showPlayer(JavaPlugin.getPlugin(NDCore::class.java), bukkitPlayer) }
        }

    /**
     * Represents the timestamp of the first time a player joined.
     *
     * This property is stored persistently in a `PersistentDataContainer` and uses epoch milliseconds
     * to denote the time. If no value exists, it defaults to the current system time when initialized.
     *
     * The `PersistentProperty` delegate ensures that the value is safely retrieved and stored in
     * the underlying container, supporting thread-safe read and write operations.
     */
    var firstJoinTimestamp: Long by PersistentProperty(
        "firstJoinTimestamp",
        PersistentDataType.LONG,
        Clock.System.now().toEpochMilliseconds(),
        persistentDataContainer
    )

    /**
     * Represents the timestamp of the player's most recent join in milliseconds since the Unix epoch.
     *
     * This property is persisted using the `PersistentDataContainer`, ensuring the value is retained
     * across server restarts and player sessions. If no previous value exists, it defaults to the
     * current system time in milliseconds.
     */
    var lastJoinTimestamp: Long by PersistentProperty(
        "lastJoinTimestamp",
        PersistentDataType.LONG,
        Clock.System.now().toEpochMilliseconds(),
        persistentDataContainer
    )

    /**
     * Stores the total playtime of a player as a timestamp in milliseconds.
     *
     * This property is backed by a `PersistentProperty` to persist the data within a
     * `PersistentDataContainer`. The timestamp is automatically initialized with the current
     * epoch milliseconds if no value exists in the container.
     *
     * The corresponding data is stored using a `PersistentDataType.LONG` type with a unique
     * namespaced key "playTimeTimestamp". Changes to this property are automatically
     * synchronized and persisted across sessions.
     *
     * Intended to track the cumulative playtime duration for the player as part of the
     * `NDPlayer` class.
     */
    private var _totalPlayTimeTimestamp: Long by PersistentProperty(
        "playTimeTimestamp",
        PersistentDataType.LONG,
        Clock.System.now().toEpochMilliseconds(),
        persistentDataContainer
    )

    /**
     * Represents the total playtime of the player in milliseconds. This value is calculated as the sum of
     * the stored playtime and the elapsed time since the player's last login.
     */
    val totalPlayTimeTimestamp: Long
        get() = _totalPlayTimeTimestamp + (Clock.System.now().toEpochMilliseconds() - lastJoinTimestamp)

    /**
     * Updates the total playtime of the player when they quit the session.
     *
     * This method calculates the duration of the current session by subtracting
     * the timestamp of the last join event from the current time. The calculated
     * session duration is then added to the player's total playtime.
     */
    fun updatePlayTimeOnQuit() {
        val sessionDuration = Clock.System.now().toEpochMilliseconds() - lastJoinTimestamp
        _totalPlayTimeTimestamp += sessionDuration
    }

    /**
     * Represents the number of kills associated with a player.
     *
     * This variable is backed by a `PersistentProperty` that stores the value in a `PersistentDataContainer`,
     * ensuring that the data is persistently saved and retrieved as needed. The type of this property is defined
     * as an integer, and it defaults to `0` if no value is present in the container.
     *
     * This property allows for tracking the kill count of a player in a persistent and synchronized manner.
     */
    var kills: Int by PersistentProperty(
        "kills",
        PersistentDataType.INTEGER,
        0,
        persistentDataContainer
    )

    /**
     * Tracks the number of mobs killed by a player.
     *
     * This property is stored persistently using the `PersistentDataContainer`
     * and automatically uses a default value of `0` if no data is present.
     * The value is updated and retrieved asynchronously, ensuring data consistency
     * and thread safety.
     */
    var mobKills: Int by PersistentProperty(
        "mobKills",
        PersistentDataType.INTEGER,
        0,
        persistentDataContainer
    )

    /**
     * Tracks the number of deaths for a player.
     *
     * This property uses a `PersistentDataContainer` to persist the death count of a player
     * across different sessions. It is initialized with a default value of `0` if no prior
     * value exists in the container.
     *
     * The property is backed by a custom `PersistentProperty` delegate, providing automatic
     * storage and retrieval functionality in synchronization with the player's data container.
     */
    var deaths: Int by PersistentProperty(
        "deaths",
        PersistentDataType.INTEGER,
        0,
        persistentDataContainer
    )

    /**
     * Represents the location where the player last died, stored as a persistent property.
     *
     * This property is managed through a `PersistentDataContainer`, allowing the location to
     * persist across player sessions. If no value is explicitly set, the default is an empty string.
     *
     * The location is stored as a `String`, typically formatted to represent the coordinates
     * or world name where the death occurred.
     */
    var lastDeathLocation: String by PersistentProperty(
        "lastDeathLocation",
        PersistentDataType.STRING,
        "",
        persistentDataContainer
    )

    /**
     * Indicates whether the player is currently frozen in place.
     *
     * This property is used to determine if a player has been immobilized,
     * preventing them from moving or performing certain actions. It is backed
     * by a persistent data container, allowing its value to be saved and
     * restored across server restarts.
     *
     * The default value is `false`, meaning the player is not frozen by default.
     *
     * When true, the player is considered frozen, and server logic should
     * enforce the necessary restrictions on their movement and actions.
     */
    private var _isFrozenInPlace: Boolean by PersistentProperty(
        "isFrozen",
        PersistentDataType.BOOLEAN,
        false,
        persistentDataContainer
    )

    /**
     * Indicates whether the player is currently frozen in place, preventing any movement.
     *
     * This property is typically managed by invoking the appropriate methods to freeze or unfreeze
     * the player (e.g., `freeze()` and `unfreeze()`). When set to true, the player is restricted
     * from moving within the game.
     */
    val isFrozenInPlace: Boolean
        get() = _isFrozenInPlace

    /**
     * Freezes the player in place, preventing them from moving. Optionally, a message can be provided
     * that will be associated with this action and passed to the related event.
     *
     * @param message An optional message that explains or describes the reason for freezing the player.
     */
    fun freeze(message: String? = null) {
        _isFrozenInPlace = true
        JavaPlugin.getPlugin(NDCore::class.java).pluginManager.callEvent(PlayerFreezeEvent(this, message))
    }

    /**
     * Unfreezes the player, allowing them to move again, and triggers the PlayerUnfreezeEvent.
     *
     * @param message an optional message providing additional context or information about the unfreeze action;
     *                defaults to null if no message is provided.
     */
    fun unfreeze(message: String? = null) {
        _isFrozenInPlace = false
        JavaPlugin.getPlugin(NDCore::class.java).pluginManager.callEvent(PlayerUnfreezeEvent(this, message))
    }

    /**
     * Sends a localized message to the player based on their language preference.
     * If no translation is found for the player's language, a fallback message is sent.
     *
     * @param localizedMessage The localized message object containing translations and fallback settings.
     */
    fun sendLocalizedMessage(localizedMessage: LocalizedMessage) {
        bukkitPlayer.sendMessage(localizedMessage[language] ?: localizedMessage.fallbackMessage())
    }

    /**
     * Tracks the number of times a player has been muted.
     *
     * This property is stored persistently using a `PersistentDataContainer`.
     * It defaults to 0 if no other value is available in the container.
     */
    var muteCount: Int by PersistentProperty(
        "muteCount",
        PersistentDataType.INTEGER,
        0,
        persistentDataContainer
    )

    /**
     * Tracks the total number of warnings issued to the player.
     *
     * This property is persistently stored within a `PersistentDataContainer` and is automatically
     * initialized with a default value of `0` if no value exists. Changes to this property are
     * saved asynchronously to ensure data consistency and thread safety.
     *
     * The `warnCount` can be used to monitor player behavior and enforce server rules by tracking
     * the warnings accumulated by a player over time.
     */
    var warnCount: Int by PersistentProperty(
        "warnCount",
        PersistentDataType.INTEGER,
        0,
        persistentDataContainer
    )

    /**
     * Represents the number of times a player has been banned.
     *
     * This property is persisted in a `PersistentDataContainer` and keeps track of the ban count
     * for a player. It utilizes the `PersistentProperty` delegate to ensure the value is stored
     * and retrieved safely, with a default value of 0 if no value exists in the container.
     */
    var banCount: Int by PersistentProperty(
        "banCount",
        PersistentDataType.INTEGER,
        0,
        persistentDataContainer
    )

    /**
     * Returns an instance of `NDEconomyPlayer` associated with this player.
     * This provides access to economy-related functionalities such as managing
     * cash and bank balances, processing payments, and handling economic transactions.
     *
     * @return The `NDEconomyPlayer` instance wrapping this player, allowing for
     * economic operations and property management.
     */
    fun economy(): NDEconomyPlayer = NDEconomyPlayer(this)

    /**
     * Provides access to the security-related functionality for the current NDPlayer instance.
     * This includes features such as password management, verification, and secure password generation.
     *
     * @return An instance of NDSecurityPlayer that represents the secured version of the current NDPlayer.
     */
    fun security(): NDSecurityPlayer = NDSecurityPlayer(this)
}

/**
 * Converts the current Player instance to an NDPlayer instance.
 *
 * @return An NDPlayer instance wrapping the current Player.
 */
fun Player.toNDPlayer(): NDPlayer = NDPlayer(this)

/**
 * Converts a `Player` object into an `NDEconomyPlayer` instance.
 *
 * This method wraps a Bukkit `Player` object in an `NDEconomyPlayer`,
 * which extends the functionality to include economy-related properties
 * and methods, such as cash, bank balance, and interest rate management.
 *
 * @return An `NDEconomyPlayer` instance associated with the provided `Player`.
 */
fun Player.toNDEconomyPlayer(): NDEconomyPlayer = NDEconomyPlayer(this)

/**
 * Converts a Player instance into an NDSecurityPlayer.
 *
 * This method facilitates the transformation of a Player object into its secured counterpart,
 * NDSecurityPlayer, which provides added functionality for password management such as password
 * hashing, verification, and secure password generation.
 *
 * @return An NDSecurityPlayer instance associated with the given Player.
 */
fun Player.toNDSecurityPlayer(): NDSecurityPlayer = NDSecurityPlayer(this)