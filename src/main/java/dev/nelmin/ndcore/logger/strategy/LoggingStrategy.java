package dev.nelmin.ndcore.logger.strategy;

import dev.nelmin.ndcore.builders.TextBuilder;
import dev.nelmin.ndcore.other.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Strategy interface for handling logging operations.
 * <p>
 * Defines the contract for formatting, generating, and writing log messages.
 * Implementations should provide thread-safe logging capabilities.
 */
public interface LoggingStrategy {
    /**
     * Updates the message format pattern used for log messages.
     * The format may contain placeholders that will be replaced with actual values.
     *
     * @param format The new format pattern to use
     * @throws NullPointerException if format is null
     */
    void format(@NotNull String format);

    /**
     * Logs content with optional silent mode.
     * When silent is true, the message is only written to file without console output.
     *
     * @param silent  Whether to suppress console output
     * @param content The content to log
     * @throws NullPointerException if content is null
     */
    void log(boolean silent, @NotNull Object... content);

    /**
     * Generates a message pair containing raw text and formatted builder.
     * The raw text is suitable for file logging while the TextBuilder contains
     * formatting for console output.
     *
     * @param content The content to generate message from
     * @return Pair of raw message string and formatted TextBuilder
     * @throws NullPointerException if content is null
     */
    @NotNull Pair<String, TextBuilder> generateMessage(@NotNull Object... content);

    /**
     * Writes a message to the log file.
     * The message will be appended to the current log file for the day.
     *
     * @param message The message to write to the log file
     * @throws NullPointerException if message is null
     * @throws IOException          if there is an error writing to the file
     */
    void writeToFile(@NotNull String message) throws IOException;
}