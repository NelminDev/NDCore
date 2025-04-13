package dev.nelmin.minecraft.builders

import dev.nelmin.minecraft.NDCore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * A utility class for constructing and managing text messages with options for prefix, suffix, styling,
 * and sending the message to players or the console.
 *
 * @property message The base string message to be transformed.
 * @property prefixEnabled Determines if the default prefix should be applied to the message.
 */
class TextBuilder(private var message: String, private var prefixEnabled: Boolean = true) {
    /**
     * Companion object for the TextBuilder class, providing utility functions.
     */
    companion object {
        /**
         * Converts a given `Component` into its plain text representation.
         *
         * @param component The `Component` object to be serialized into plain text.
         * @return A `String` representing the plain text content of the provided `Component`.
         */
        fun componentToPlainText(component: Component): String {
            return PlainTextComponentSerializer.plainText().serialize(component)
        }
    }

    /**
     * Disables the prefix feature for the `TextBuilder` instance.
     *
     * @param prefixEnabled A boolean value to enable or disable prefix functionality.
     *                       Default is `false`, which disables the prefix.
     * @return The current instance of [TextBuilder], allowing for method chaining.
     */
    fun noPrefix(prefixEnabled: Boolean = false): TextBuilder {
        this.prefixEnabled = prefixEnabled
        return this
    }

    /**
     * Adds a prefix to the given message.
     *
     * @param message The message to which the prefix will be added. Defaults to the value of `this.message`.
     * @return A new string with the prefix added to the beginning of the provided message.
     */
    private fun addPrefix(message: String = this.message): String {
        return "${NDCore.prefix} $message"
    }

    /**
     * Adds a prefix to the current message.
     *
     * @param prefix The text to prepend to the current message.
     * @return The current instance of [TextBuilder] to allow for method chaining.
     */
    fun prefix(prefix: String): TextBuilder {
        this.message = "$prefix $message"
        return this
    }

    /**
     * Appends the specified suffix to the current message being built and updates the state of the builder.
     *
     * @param suffix The string to be appended to the existing message.
     * @return The current instance of [TextBuilder] to allow method chaining.
     */
    fun suffix(suffix: String): TextBuilder {
        this.message = "$message $suffix"
        return this
    }

    /**
     * Sets the content of the text builder with the specified message.
     *
     * @param message The message to set as the content of the text builder.
     * @return The current instance of [TextBuilder] for method chaining.
     */
    fun content(message: String): TextBuilder {
        this.message = message
        return this
    }

    /**
     * Updates the content of the `TextBuilder` with the provided message.
     *
     * @param message The new message to be set as the content of the `TextBuilder`.
     * @return The current instance of [TextBuilder] to allow method chaining.
     */
    fun message(message: String): TextBuilder = content(message)

    /**
     * Replaces all occurrences of the specified search value in the current message
     * with the provided replacement value.
     *
     * @param search the value to be replaced in the message
     * @param replacement the value to replace the search value with
     * @return the updated TextBuilder instance with the replacements applied
     */
    fun replace(search: Any, replacement: Any): TextBuilder {
        this.message = this.message.replace(search.toString(), replacement.toString())
        return this
    }

    /**
     * Replaces occurrences of specified keys in the current text with their corresponding values.
     *
     * @param map A map where each key represents the target to be replaced in the text, and
     * each value is the replacement for that target.
     * @return The updated instance of TextBuilder after performing the replacements.
     */
    fun replace(map: Map<Any, Any>): TextBuilder {
        map.forEach { (search, replacement) -> replace(search, replacement) }
        return this
    }

    /**
     * Sends a message to the specified player, with optional colorization and prefix formatting.
     *
     * @param player The player to whom the message will be sent.
     * @param colorized A boolean indicating whether the message should be colorized. Defaults to true.
     * @param prefixOnNewLine A boolean indicating whether the prefix should appear on a new line. Defaults to true.
     * @return The current instance of [TextBuilder], allowing for method chaining.
     */
    fun sendTo(player: Player, colorized: Boolean = true, prefixOnNewLine: Boolean = true): TextBuilder {
        player.sendMessage(if (colorized) colorize(prefixOnNewLine = prefixOnNewLine) else Component.text(message))
        return this
    }

    /**
     * Sends the text content of the current instance to a list of players.
     *
     * @param players The list of [Player] instances to send the message to.
     * @param colorized A boolean indicating if the message should be colorized before sending. Defaults to true.
     * @param prefixOnNewLine A boolean indicating if the prefix should appear on a new line. Defaults to true.
     * @return The current instance of [TextBuilder], allowing for method chaining.
     */
    fun sendTo(players: List<Player>, colorized: Boolean = true, prefixOnNewLine: Boolean = true): TextBuilder {
        players.forEach { player: Player ->
            sendTo(player, colorized, prefixOnNewLine)
        }
        return this
    }

    /**
     * Sends the current message to the server console.
     *
     * @param prefixOnNewLine Determines if the prefix should be placed on a new line.
     *                        Defaults to true.
     * @return The current instance of [TextBuilder] to allow method chaining.
     */
    fun sendToConsole(prefixOnNewLine: Boolean = true): TextBuilder {
        Bukkit.getConsoleSender().sendMessage(colorize(prefixOnNewLine = prefixOnNewLine))
        return this
    }

    /**
     * Converts a text message into a component while applying legacy color codes.
     *
     * @param legacyCharacter The character used to denote legacy color codes in the message. Defaults to '&'.
     * @param prefixOnNewLine Whether to add a prefix to new lines in the message. Defaults to true.
     * @return A [Component] representing the colorized version of the text message.
     */
    fun colorize(legacyCharacter: Char = '&', prefixOnNewLine: Boolean = true): Component {
        return LegacyComponentSerializer.legacy(legacyCharacter).deserialize(get(prefixOnNewLine))
    }

    /**
     * Retrieves the constructed or modified message, optionally adding a prefix and formatting it
     * with specified preferences.
     *
     * @param prefixOnNewLine Determines whether the prefix, if enabled, should be added on a new line.
     *                        Defaults to `true`.
     * @return The formatted message as a [String].
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
     * Converts the object to its string representation.
     *
     * @return A plain text representation of the object's components.
     */
    override fun toString(): String {
        return componentToPlainText(colorize(prefixOnNewLine = false))
    }
}