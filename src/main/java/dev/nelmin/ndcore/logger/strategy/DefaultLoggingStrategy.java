package dev.nelmin.ndcore.logger.strategy;

import dev.nelmin.ndcore.builders.TextBuilder;
import dev.nelmin.ndcore.other.DateTime;
import dev.nelmin.ndcore.other.Pair;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Default implementation of LoggingStrategy for handling log messages.
 * Provides thread-safe logging with file output and optional console display.
 */
public class DefaultLoggingStrategy implements LoggingStrategy {
    private final @NotNull JavaPlugin plugin;
    private final @NotNull AtomicReference<String> format;
    private final @NotNull String loggerName;
    private final @NotNull String strategyName;
    private final @NotNull String colorCode;
    private final @NotNull Path logBasePath;

    /**
     * Constructs a new DefaultLoggingStrategy with the specified parameters.
     *
     * @param plugin       The JavaPlugin instance for task scheduling
     * @param format       The initial format pattern for log messages
     * @param loggerName   The name of the logger instance
     * @param strategyName The name of this logging strategy
     * @param colorCode    The color code used for console output
     * @throws NullPointerException if any parameter is null
     */
    public DefaultLoggingStrategy(
            @NotNull JavaPlugin plugin,
            @NotNull String format,
            @NotNull String loggerName,
            @NotNull String strategyName,
            @NotNull String colorCode
    ) {
        this.plugin = Objects.requireNonNull(plugin, "plugin cannot be null");
        this.format = new AtomicReference<>(Objects.requireNonNull(format, "format cannot be null"));
        this.loggerName = Objects.requireNonNull(loggerName, "loggerName cannot be null");
        this.strategyName = Objects.requireNonNull(strategyName, "strategyName cannot be null");
        this.colorCode = Objects.requireNonNull(colorCode, "colorCode cannot be null");
        this.logBasePath = Path.of(System.getProperty("user.dir"))
                .resolve("logs")
                .resolve("NDCore");
    }

    /**
     * Updates the format pattern used for log messages.
     *
     * @param format The new format pattern to use
     * @throws NullPointerException if format is null
     */
    @Override
    public void format(@NotNull String format) {
        this.format.set(Objects.requireNonNull(format, "format cannot be null"));
    }

    /**
     * Logs the given content using the current format pattern.
     * Can optionally suppress console output while still writing to file.
     *
     * @param silent  Whether to suppress console output
     * @param content The content to log
     * @throws NullPointerException if content is null
     */
    @Override
    public void log(boolean silent, @NotNull Object... content) {
        Objects.requireNonNull(content, "content cannot be null");
        Pair<String, TextBuilder> messagePair = generateMessage(content);
        if (messagePair.first() != null) {
            writeToFile(messagePair.first());
        }
        if (!silent && messagePair.second() != null) {
            messagePair.second().sendToConsole();
        }
    }

    /**
     * Generates a message pair containing the file log message and console message.
     *
     * @param content The content to include in the message
     * @return A pair containing the file message and console TextBuilder
     * @throws NullPointerException if content is null
     */
    @Override
    @NotNull
    public Pair<String, TextBuilder> generateMessage(@NotNull Object... content) {
        Objects.requireNonNull(content, "content cannot be null");
        String currentFormat = format.get();
        String message = Arrays.stream(content)
                .map(Object::toString)
                .collect(Collectors.joining(" "));
        DateTime dateTime = new DateTime();
        message = currentFormat
                .replace("%timestamp", dateTime.formatTime())
                .replace("%strategyName", strategyName)
                .replace("%loggerName", loggerName)
                .replace("%content", message);
        TextBuilder consoleMessage = new TextBuilder(message.replace(
                strategyName,
                String.format("%s%s%s", colorCode, strategyName, "&r")
        ));
        return new Pair<>(message, consoleMessage);
    }

    /**
     * Writes a message to the log file asynchronously.
     * Creates necessary directories if they don't exist.
     *
     * @param message The message to write to the file
     * @throws NullPointerException if message is null
     */
    @Override
    public void writeToFile(@NotNull String message) {
        Objects.requireNonNull(message, "message cannot be null");
        if (!plugin.isEnabled()) return;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Path logFile = logBasePath
                        .resolve(new DateTime().formatDate())
                        .resolve(strategyName.toLowerCase(Locale.ROOT) + ".log");
                Files.createDirectories(logFile.getParent());
                Files.write(
                        logFile,
                        List.of(message),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                );
            } catch (IOException e) {
                System.err.println("Could not write to log file: " + e.getMessage());
            }
        });
    }
}