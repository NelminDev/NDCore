package dev.nelmin.ndcore.logger.strategy;

import dev.nelmin.ndcore.builders.TextBuilder;
import dev.nelmin.ndcore.other.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * Strategy interface for handling logging operations
 * <p>
 * Defines the contract for formatting, generating, and writing log messages
 */
public interface LoggingStrategy {
    /**
     * Updates the message format pattern
     */
    void format(@NotNull String format);

    /**
     * Logs content with optional silent mode
     */
    void log(boolean silent, @NotNull Object... content);

    /**
     * Generates a message pair containing raw text and formatted builder
     *
     * @return Pair of raw message string and formatted TextBuilder
     */
    @NotNull Pair<String, TextBuilder> generateMessage(@NotNull Object... content);

    /**
     * Writes a message to the log file
     */
    void writeToFile(@NotNull String message);
}