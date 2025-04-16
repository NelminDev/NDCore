package dev.nelmin.ndcore.builders;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Map;

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
     */
    public TextBuilder(@NotNull String message, boolean prefixEnabled) {
        this.messageBuilder = new StringBuilder(message);
        this.prefixEnabled = prefixEnabled;
        this.miniMessage = MiniMessage.miniMessage();
    }

    /**
     * Creates a new TextBuilder with a message and prefix enabled by default
     */
    public TextBuilder(@NotNull String message) {
        this(message, true);
    }

    /**
     * Converts a Component to plain text
     */
    public static @NotNull String componentToPlainText(@NotNull Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    /**
     * Converts the message to a colored Component using legacy color codes
     */
    public @NotNull Component colorize(char legacyCharacter) {
        return LegacyComponentSerializer.legacy(legacyCharacter).deserialize(messageBuilder.toString());
    }

    /**
     * Applies a gradient effect between two colors
     */
    public TextBuilder gradient(@NotNull Color startColor, @NotNull Color endColor) {
        String miniMessageFormat = String.format("<gradient:%06x:%06x>%s</gradient>",
                startColor.getRGB() & 0xFFFFFF,
                endColor.getRGB() & 0xFFFFFF,
                messageBuilder);
        return parseMiniMessage(miniMessageFormat);
    }

    /**
     * Applies alternating colors to each character
     */
    public TextBuilder alternate(@NotNull TextColor... colors) {
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
     */
    public TextBuilder parseMiniMessage(@NotNull String miniMessageFormat) {
        Component component = miniMessage.deserialize(miniMessageFormat);
        messageBuilder = new StringBuilder(componentToPlainText(component));
        return this;
    }

    public @NotNull String message() {
        return messageBuilder.toString();
    }

    public TextBuilder message(@NotNull String message) {
        this.messageBuilder = new StringBuilder(message);
        return this;
    }

    public TextBuilder prefix(@NotNull String prefix) {
        if (prefix.isEmpty()) {
            throw new IllegalArgumentException("Prefix cannot be empty");
        }
        messageBuilder.insert(0, prefix + " ");
        return this;
    }

    public TextBuilder prefixEnabled(boolean enabled) {
        this.prefixEnabled = enabled;
        return this;
    }

    public TextBuilder replace(@NotNull Object search, @Nullable Object replacement) {
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

    public TextBuilder replace(@NotNull Map<Object, Object> replacements) {
        replacements.forEach(this::replace);
        return this;
    }

    public TextBuilder sendTo(@NotNull Player player, boolean colorized) {
        player.sendMessage(colorized ? colorize('&') : Component.text(messageBuilder.toString()));
        return this;
    }

    public TextBuilder sendTo(@NotNull List<Player> players, boolean colorized) {
        players.forEach(player -> sendTo(player, colorized));
        return this;
    }

    public TextBuilder sendToConsole() {
        Bukkit.getConsoleSender().sendMessage(colorize('&'));
        return this;
    }

    public TextBuilder suffix(@NotNull String suffix) {
        if (suffix.isEmpty()) {
            throw new IllegalArgumentException("Suffix cannot be empty");
        }
        messageBuilder.append(" ").append(suffix);
        return this;
    }

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