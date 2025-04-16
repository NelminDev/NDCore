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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Default implementation of LoggingStrategy for handling log messages
 */
public class DefaultLoggingStrategy implements LoggingStrategy {
    // Variables
    private final @NotNull JavaPlugin plugin;
    private final @NotNull AtomicReference<String> format;
    private final @NotNull String loggerName;
    private final @NotNull String strategyName;
    private final @NotNull String colorCode;
    private final @NotNull Path logBasePath;

    // Constructor
    public DefaultLoggingStrategy(
            @NotNull JavaPlugin plugin,
            @NotNull String format,
            @NotNull String loggerName,
            @NotNull String strategyName,
            @NotNull String colorCode
    ) {
        this.plugin = plugin;
        this.format = new AtomicReference<>(format);
        this.loggerName = loggerName;
        this.strategyName = strategyName;
        this.colorCode = colorCode;
        this.logBasePath = Path.of(System.getProperty("user.dir"))
                .resolve("logs")
                .resolve("NDCore");
    }

    // Functions
    @Override
    public void format(@NotNull String format) {
        this.format.set(format);
    }

    @Override
    public void log(boolean silent, @NotNull Object... content) {
        Pair<String, TextBuilder> messagePair = generateMessage(content);
        if (messagePair.first() != null) {
            writeToFile(messagePair.first());
        }
        if (!silent && messagePair.second() != null) {
            messagePair.second().sendToConsole();
        }
    }

    @Override
    @NotNull
    public Pair<String, TextBuilder> generateMessage(@NotNull Object... content) {
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
        ), false);
        return new Pair<>(message, consoleMessage);
    }

    @Override
    public void writeToFile(@NotNull String message) {
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