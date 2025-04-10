package dev.nelmin.spigot.builders

import dev.nelmin.spigot.NDCore
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * A utility class designed to build, modify, and send customizable text messages.
 * This class offers various methods to set prefixes, suffixes, and replace parts
 * of text while also providing support for color formatting and sending messages
 * to players or the console.
 */
class TextBuilder(private var message: String, private var prefixEnabled: Boolean = true) {
    /**
     * Disables or enables the prefix functionality for the text being built.
     *
     * @param prefixEnabled A boolean indicating whether the prefix should be enabled. Defaults to false.
     * @return The current instance of [TextBuilder] for method chaining.
     */
    fun noPrefix(prefixEnabled: Boolean = false): TextBuilder {
        this.prefixEnabled = prefixEnabled
        return this
    }

    /**
     * Adds a predefined prefix to the given message or to the default message.
     *
     * @param message The message to which the prefix will be added. Defaults to the class's default message.
     * @return A new string with the prefix prepended to the message.
     */
    private fun addPrefix(message: String = this.message): String {
        return "${NDCore.prefix} $message"
    }

    /**
     * Appends the specified prefix to the current message and updates the message content.
     *
     * @param prefix The string to prepend to the message.
     * @return The current instance of TextBuilder for method chaining.
     */
    fun prefix(prefix: String): TextBuilder {
        this.message = "$prefix $message"
        return this
    }

    /**
     * Appends the specified suffix to the current message and updates the TextBuilder instance.
     *
     * @param suffix the string to append to the current message
     * @return the updated TextBuilder instance with the suffix appended
     */
    fun suffix(suffix: String): TextBuilder {
        this.message = "$message $suffix"
        return this
    }

    /**
     * Sets the content of the message and returns the current instance of TextBuilder.
     *
     * @param message The message content to set.
     * @return The current instance of TextBuilder.
     */
    fun content(message: String): TextBuilder {
        this.message = message
        return this
    }

    /**
     * Sets the message content for the TextBuilder.
     *
     * @param message The message to be set as the content.
     * @return The updated instance of TextBuilder with the specified message.
     */
    fun message(message: String): TextBuilder = content(message)

    /**
     * Replaces all occurrences of the specified search value in the current text with the given replacement value.
     *
     * @param search The value to search for within the text.
     * @param replacement The value to replace the search value with.
     * @return The updated TextBuilder instance after the replacement is performed.
     */
    fun replace(search: Any, replacement: Any): TextBuilder {
        this.message = this.message.replace(search.toString(), replacement.toString())
        return this
    }

    /**
     * Replaces occurrences of specified keys in the text with their corresponding values
     * from the provided map.
     *
     * @param map A map where keys represent the text to be replaced, and values represent
     * the replacement text.
     * @return The updated instance of TextBuilder after all replacements have been applied.
     */
    fun replace(map: Map<Any, Any>): TextBuilder {
        map.forEach { (search, replacement) -> replace(search, replacement) }
        return this
    }

    /**
     * Sends the constructed message to the specified player with optional formatting.
     *
     * @param player the player to whom the message should be sent.
     * @param colorized specifies whether the message should be colorized. Default is true.
     * @param prefixOnNewLine determines if the prefix should appear on a new line. Default is true.
     * @return the current instance of [TextBuilder] for method chaining.
     */
    fun sendTo(player: Player, colorized: Boolean = true, prefixOnNewLine: Boolean = true): TextBuilder {
        player.sendMessage(if (colorized) colorize(prefixOnNewLine = prefixOnNewLine) else message)
        return this
    }

    /**
     * Sends the message to a list of players.
     *
     * @param players The list of players to whom the message will be sent.
     * @param colorized Determines if the message should be colorized. Default is true.
     * @param prefixOnNewLine Determines if the prefix should appear on a new line. Default is true.
     * @return The current instance of [TextBuilder].
     */
    fun sendTo(players: List<Player>, colorized: Boolean = true, prefixOnNewLine: Boolean = true): TextBuilder {
        players.forEach { player: Player ->
            player.sendMessage(if (colorized) colorize(prefixOnNewLine = prefixOnNewLine) else message)
        }
        return this
    }

    /**
     * Sends a colorized message, optionally including the prefix on a new line, to the console.
     *
     * @param prefixOnNewLine Determines whether the prefix is added on a new line before the message. Default is true.
     * @return Returns the current instance of TextBuilder for method chaining.
     */
    fun sendToConsole(prefixOnNewLine: Boolean = true): TextBuilder {
        Bukkit.getConsoleSender().sendMessage(colorize(prefixOnNewLine = prefixOnNewLine))
        return this
    }

    /**
     * Translates alternate color codes in the message using the specified legacy character
     * and returns the formatted string.
     *
     * @param legacyCharacter The character used for legacy color codes translation. Default is '&'.
     * @param prefixOnNewLine Whether to apply the prefix on a new line. Default is true.
     * @return The colorized message as a String.
     */
    fun colorize(legacyCharacter: Char = '&', prefixOnNewLine: Boolean = true): String {
        return ChatColor.translateAlternateColorCodes(legacyCharacter, get(prefixOnNewLine))
    }

    /**
     * Retrieves the current message with an optional prefix applied.
     * If the prefix is enabled and `prefixOnNewLine` is true, the prefix is added to each new line of the message.
     *
     * @param prefixOnNewLine Determines whether the prefix should be added to the start of each new line in the message. Defaults to true.
     * @return The formatted message as a String, potentially with prefixes applied to new lines.
     */
    fun get(prefixOnNewLine: Boolean = true): String {
        var message = this.message

        if (prefixEnabled) {
            addPrefix()
        }
        if (prefixOnNewLine) {
            message = message.replace("\n", "\n${NDCore.prefix} ")
        }

        return message
    }

    /**
     * Converts the current state of the TextBuilder into a string representation with colorization applied.
     *
     * @return A string representation of the TextBuilder, with optional color formatting applied.
     */
    override fun toString(): String {
        return colorize(prefixOnNewLine = false)
    }
}