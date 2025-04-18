package dev.nelmin.ndcore.builders;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Builder class for creating and manipulating text messages with various formatting options. <p>
 * Supports legacy colors, gradients, MiniMessage format, and common color patterns.
 */
public class TextBuilder {
    private final MiniMessage miniMessage;
    @NotNull
    private StringBuilder messageBuilder;
    private boolean prefixEnabled;

    /**
     * Creates a new TextBuilder with a message and prefix setting
     *
     * @param message       The message to build from
     * @param prefixEnabled Whether prefix is enabled
     */
    public TextBuilder(@NotNull String message, boolean prefixEnabled) {
        this.messageBuilder = new StringBuilder(Objects.requireNonNull(message));
        this.prefixEnabled = prefixEnabled;
        this.miniMessage = MiniMessage.miniMessage();
    }

    /**
     * Creates a new TextBuilder with a message and prefix enabled by default
     *
     * @param message The message to build from
     */
    public TextBuilder(@NotNull String message) {
        this(message, true);
    }

    /**
     * Converts a Component to plain text
     *
     * @param component The component to convert
     * @return The plain text representation
     */
    public static @NotNull String componentToPlainText(@NotNull Component component) {
        return PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(component));
    }

    /**
     * Converts the message to a colored Component using legacy color codes
     *
     * @param legacyCharacter The character used for color codes
     * @return The colored component
     */
    public @NotNull Component colorize(char legacyCharacter) {
        return LegacyComponentSerializer.legacy(legacyCharacter).deserialize(messageBuilder.toString());
    }

    /**
     * Applies a gradient effect between two colors
     *
     * @param startColor Starting color
     * @param endColor   Ending color
     * @return The TextBuilder for chaining
     */
    public TextBuilder gradient(@NotNull Color startColor, @NotNull Color endColor) {
        Objects.requireNonNull(startColor);
        Objects.requireNonNull(endColor);
        String miniMessageFormat = String.format("<gradient:%06x:%06x>%s</gradient>",
                startColor.getRGB() & 0xFFFFFF,
                endColor.getRGB() & 0xFFFFFF,
                messageBuilder);
        return parseMiniMessage(miniMessageFormat);
    }

    /**
     * Applies alternating colors to each character
     *
     * @param colors The colors to alternate between
     * @return The TextBuilder for chaining
     * @throws IllegalArgumentException if less than 2 colors are provided
     */
    public TextBuilder alternate(@NotNull TextColor... colors) throws IllegalArgumentException {
        Objects.requireNonNull(colors);
        if (colors.length < 2) {
            throw new IllegalArgumentException("At least two colors are required for alternating pattern");
        }

        StringBuilder formatted = new StringBuilder();
        char[] chars = messageBuilder.toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            TextColor color = colors[i % colors.length];
            formatted.append("<").append(color.asHexString()).append(">")
                    .append(chars[i]);
        }

        return parseMiniMessage(formatted.toString());
    }

    /**
     * Parses and applies MiniMessage formatting
     *
     * @param miniMessageFormat The message in MiniMessage format
     * @return The TextBuilder for chaining
     */
    public TextBuilder parseMiniMessage(@NotNull String miniMessageFormat) {
        Component component = miniMessage.deserialize(Objects.requireNonNull(miniMessageFormat));
        messageBuilder = new StringBuilder(componentToPlainText(component));
        return this;
    }

    /**
     * Gets the current message
     *
     * @return The current message
     */
    public @NotNull String message() {
        return messageBuilder.toString();
    }

    /**
     * Sets a new message
     *
     * @param message The new message
     * @return The TextBuilder for chaining
     */
    public TextBuilder message(@NotNull String message) {
        this.messageBuilder = new StringBuilder(Objects.requireNonNull(message));
        return this;
    }

    /**
     * Adds a prefix to the message
     *
     * @param prefix The prefix to add
     * @return The TextBuilder for chaining
     * @throws IllegalArgumentException if prefix is empty
     */
    public TextBuilder prefix(@NotNull String prefix) throws IllegalArgumentException {
        Objects.requireNonNull(prefix);
        if (prefix.isEmpty()) {
            throw new IllegalArgumentException("Prefix cannot be empty");
        }
        messageBuilder.insert(0, prefix + " ");
        return this;
    }

    public TextBuilder prefix(@NotNull FileConfiguration fileConfiguration) throws IllegalArgumentException {
        return this.prefix(Objects.requireNonNull(fileConfiguration.getString("prefix"), "Prefix cannot be empty or null. Please check your config.yml file and try again. If the problem persists, contact an administrator."));
    }

    /**
     * Sets whether prefix is enabled
     *
     * @param enabled Whether prefix should be enabled
     * @return The TextBuilder for chaining
     */
    public TextBuilder prefixEnabled(boolean enabled) {
        this.prefixEnabled = enabled;
        return this;
    }

    /**
     * Replaces all occurrences of search with replacement
     *
     * @param search      The string to search for
     * @param replacement The string to replace with
     * @return The TextBuilder for chaining
     */
    public TextBuilder replace(@NotNull Object search, @Nullable Object replacement) {
        Objects.requireNonNull(search);
        String searchStr = search.toString();
        if (replacement == null) return this;
        String replaceStr = replacement.toString();

        int start = 0;
        while ((start = messageBuilder.indexOf(searchStr, start)) != -1) {
            messageBuilder.replace(start, start + searchStr.length(), replaceStr);
            start += replaceStr.length();
        }
        return this;
    }

    /**
     * Replaces multiple strings using a map of search/replacement pairs
     *
     * @param replacements Map of search strings to replacement strings
     * @return The TextBuilder for chaining
     */
    public TextBuilder replace(@NotNull Map<Object, Object> replacements) {
        Objects.requireNonNull(replacements).forEach(this::replace);
        return this;
    }

    /**
     * Sends the message to a player
     *
     * @param player    The player to send to
     * @param colorized Whether to apply color codes
     * @return The TextBuilder for chaining
     */
    public TextBuilder sendTo(@NotNull Player player, boolean colorized) {
        Objects.requireNonNull(player).sendMessage(colorized ? colorize('&') : Component.text(messageBuilder.toString()));
        return this;
    }

    /**
     * Sends the message to multiple players
     *
     * @param players   The list of players
     * @param colorized Whether to apply color codes
     * @return The TextBuilder for chaining
     */
    public TextBuilder sendTo(@NotNull List<Player> players, boolean colorized) {
        Objects.requireNonNull(players).forEach(player -> sendTo(player, colorized));
        return this;
    }

    /**
     * Sends the message to console with color codes
     *
     * @return The TextBuilder for chaining
     */
    public TextBuilder sendToConsole() {
        Bukkit.getConsoleSender().sendMessage(colorize('&'));
        return this;
    }

    /**
     * Adds a suffix to the message
     *
     * @param suffix The suffix to add
     * @return The TextBuilder for chaining
     * @throws IllegalArgumentException if suffix is empty
     */
    public TextBuilder suffix(@NotNull String suffix) throws IllegalArgumentException {
        Objects.requireNonNull(suffix);
        if (suffix.isEmpty()) {
            throw new IllegalArgumentException("Suffix cannot be empty");
        }
        messageBuilder.append(" ").append(suffix);
        return this;
    }

    /**
     * Converts legacy color codes and returns the message as a string
     *
     * @return The formatted message string
     */
    @Override
    public @NotNull String toString() {
        int length = messageBuilder.length();
        if (length < 2) return messageBuilder.toString();

        // Encode 0-9, a-f, k-o, r, x in the mask
        long validCodes = 0x3FF_FCFF_FFF_0000L;

        for (int i = 0; i < length - 1; i++) {
            if (messageBuilder.charAt(i) == '&') {
                char next = Character.toLowerCase(messageBuilder.charAt(i + 1));
                // Check if the character is a valid color code using bit operations
                if (next >= '0' && next <= 'z' && ((1L << (next - '0')) & validCodes) != 0) {
                    messageBuilder.setCharAt(i, '§');
                    messageBuilder.setCharAt(i + 1, next);
                }
            }
        }

        return messageBuilder.toString();
    }
}