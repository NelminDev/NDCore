package dev.nelmin.spigot.builders

import dev.nelmin.spigot.NDCConfig
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * Utility class for building and managing text messages with customizable prefix and formatting options.
 *
 * This class provides fluent APIs for modifying a text message by applying prefixes, suffixes, replacements,
 * and other transformations. It supports sending messages to players or the console with optional formatting
 * for color and prefix handling.
 */
class TextBuilder(private var message: String, private var prefixEnabled: Boolean = true) {
    /**
     * Disables or enables the use of a prefix for the text content.
     *
     * @param prefixEnabled A boolean flag indicating whether the prefix should be enabled (true) or disabled (false).
     *        The default value is false, which disables the prefix.
     * @return The instance of the LegacyTextBuilder for method chaining.
     */
    fun noPrefix(prefixEnabled: Boolean = false): TextBuilder {
        this.prefixEnabled = prefixEnabled
        return this
    }

    /**
     * Adds a prefix to the provided message if applicable.
     *
     * @param message The message to which the prefix should be added. Defaults to the class's current message value.
     * @return A string comprising the prefix followed by the provided or default message.
     */
    private fun addPrefix(message: String = this.message): String {
        return "${NDCConfig.prefix} $message"
    }

    /**
     * Adds a specified prefix to the current message and updates the message content.
     *
     * @param prefix The string to be added as a prefix to the current message.
     * @return The current instance of [TextBuilder] with the updated message content.
     */
    fun prefix(prefix: String): TextBuilder {
        this.message = "$prefix $message"
        return this
    }

    /**
     * Appends the specified suffix to the current message and updates the builder.
     *
     * @param suffix The string to be appended as a suffix to the current message.
     * @return The updated instance of [TextBuilder].
     */
    fun suffix(suffix: String): TextBuilder {
        this.message = "$message $suffix"
        return this
    }

    /**
     * Updates the message content for the LegacyTextBuilder.
     *
     * @param message the new message content to be set
     * @return the instance of LegacyTextBuilder with the updated message
     */
    fun content(message: String): TextBuilder {
        this.message = message
        return this
    }

    /**
     * Updates the message content of the LegacyTextBuilder.
     *
     * @param message The new message content to be set.
     * @return The updated LegacyTextBuilder instance with the specified message.
     */
    fun message(message: String): TextBuilder = content(message)

    /**
     * Replaces all occurrences of the specified search string with the replacement string
     * in the current message of the LegacyTextBuilder.
     *
     * @param search the value to be replaced within the message
     * @param replacement the value to replace the search value with
     * @return the updated LegacyTextBuilder instance
     */
    fun replace(search: Any, replacement: Any): TextBuilder {
        this.message = this.message.replace(search.toString(), replacement.toString())
        return this
    }

    /**
     * Replaces occurrences of specified keys in the current message with their corresponding values from the provided map.
     *
     * @param map A map containing keys to search for and their respective replacement values.
     * @return The updated LegacyTextBuilder instance with replacements applied.
     */
    fun replace(map: Map<Any, Any>): TextBuilder {
        map.forEach { (search, replacement) -> replace(search, replacement) }
        return this
    }

    /**
     * Sends the message to a specific player, optionally applying colorization and prefix adjustments.
     *
     * @param player The player to whom the message will be sent.
     * @param colorized Determines whether the message should be colorized. Defaults to true.
     * @param prefixOnNewLine Specifies whether the prefix should appear on a new line. Defaults to true.
     * @return The current instance of LegacyTextBuilder.
     */
    fun sendTo(player: Player, colorized: Boolean = true, prefixOnNewLine: Boolean = true): TextBuilder {
        player.sendMessage(if (colorized) colorize(prefixOnNewLine = prefixOnNewLine) else message)
        return this
    }

    /**
     * Sends the message constructed by this LegacyTextBuilder to a list of players.
     *
     * @param players the list of players to whom the message will be sent
     * @param colorized whether the message should be colorized using the legacy character; defaults to true
     * @param prefixOnNewLine whether the prefix, if enabled, should appear on a new line; defaults to true
     * @return the current LegacyTextBuilder instance for method chaining
     */
    fun sendTo(players: List<Player>, colorized: Boolean = true, prefixOnNewLine: Boolean = true): TextBuilder {
        players.forEach { player: Player ->
            player.sendMessage(if (colorized) colorize(prefixOnNewLine = prefixOnNewLine) else message)
        }
        return this
    }

    /**
     * Sends the constructed message to the server console.
     *
     * @param prefixOnNewLine Determines whether the prefix, if present, should be added on a new line.
     *                        Defaults to true.
     * @return The current instance of [TextBuilder], allowing for method chaining.
     */
    fun sendToConsole(prefixOnNewLine: Boolean = true): TextBuilder {
        Bukkit.getConsoleSender().sendMessage(colorize(prefixOnNewLine = prefixOnNewLine))
        return this
    }

    /**
     * Translates color codes in the text associated with this builder using a specified legacy color code
     * character. Optionally applies a prefix to the text based on the provided settings.
     *
     * @param legacyCharacter the character used for legacy color codes, by default '&'
     * @param prefixOnNewLine whether the prefix should be applied to each new line, by default true
     * @return the text with translated color codes and optionally adjusted prefixes
     */
    fun colorize(legacyCharacter: Char = '&', prefixOnNewLine: Boolean = true): String {
        return ChatColor.translateAlternateColorCodes(legacyCharacter, get(prefixOnNewLine))
    }

    /**
     * Generates and returns a formatted message, optionally applying a prefix to new lines.
     *
     * @param prefixOnNewLine A boolean indicating whether the prefix should be applied to new lines. Defaults to true.
     * @return The formatted message string.
     */
    fun get(prefixOnNewLine: Boolean = true): String {
        var message = this.message

        if (prefixEnabled) {
            addPrefix()
        }
        if (prefixOnNewLine) {
            message = message.replace("\n", "\n${NDCConfig.prefix} ")
        }

        return message
    }

    /**
     * Provides a string representation of the object.
     *
     * @return A string resulting from the `colorize` method with `prefixOnNewLine` set to false.
     */
    override fun toString(): String {
        return colorize(prefixOnNewLine = false)
    }
}