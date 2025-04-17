package dev.nelmin.ndcore.logger;

import dev.nelmin.ndcore.logger.strategy.DefaultLoggingStrategy;
import dev.nelmin.ndcore.logger.strategy.LoggingStrategy;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Utility for handling logging tasks within a plugin
 * <p>
 * Provides different logging levels (INFO, WARN, ERROR, FATAL) with customizable format and behavior
 * using underlying LoggingStrategy to define the behavior of each logging level.
 * Messages can be logged silently (to file only) or with console output.
 */
public class NDLogger {
    private static final String FORMAT = "[%timestamp] - %strategyName - %loggerName - %content";

    private final @NotNull LoggingStrategy errorStrategy;
    private final @NotNull LoggingStrategy fatalStrategy;
    private final @NotNull LoggingStrategy infoStrategy;
    private final @NotNull LoggingStrategy warnStrategy;

    /**
     * Creates a new logger instance with the given name and plugin
     *
     * @param loggerName name of the logger instance
     * @param plugin     plugin instance that owns this logger
     * @throws NullPointerException if loggerName or plugin is null
     */
    public NDLogger(@NotNull String loggerName, @NotNull JavaPlugin plugin) {
        Objects.requireNonNull(loggerName, "loggerName cannot be null");
        Objects.requireNonNull(plugin, "plugin cannot be null");

        this.errorStrategy = new DefaultLoggingStrategy(plugin, FORMAT, loggerName, "ERROR", "&c");
        this.fatalStrategy = new DefaultLoggingStrategy(plugin, FORMAT, loggerName, "FATAL", "&4&l");
        this.infoStrategy = new DefaultLoggingStrategy(plugin, FORMAT, loggerName, "INFO", "&b");
        this.warnStrategy = new DefaultLoggingStrategy(plugin, FORMAT, loggerName, "WARN", "&e");
    }

    /**
     * Logs message using specified strategy
     *
     * @param strategy logging strategy to use
     * @param silent   whether to suppress console output
     * @param content  message content to log
     * @throws NullPointerException if strategy or content is null
     */
    public void log(@NotNull LoggingStrategy strategy, boolean silent, @NotNull Object... content) {
        Objects.requireNonNull(strategy, "strategy cannot be null");
        Objects.requireNonNull(content, "content cannot be null");
        strategy.log(silent, content);
    }

    /**
     * Logs a warning message with console output
     *
     * @param content message content to log
     * @throws NullPointerException if content is null
     */
    public void warn(@NotNull Object... content) {
        Objects.requireNonNull(content, "content cannot be null");
        log(warnStrategy, false, content);
    }

    /**
     * Logs a warning message without console output
     *
     * @param content message content to log
     * @throws NullPointerException if content is null
     */
    public void warnSilent(@NotNull Object... content) {
        Objects.requireNonNull(content, "content cannot be null");
        log(warnStrategy, true, content);
    }

    /**
     * Logs an info message with console output
     *
     * @param content message content to log
     * @throws NullPointerException if content is null
     */
    public void info(@NotNull Object... content) {
        Objects.requireNonNull(content, "content cannot be null");
        log(infoStrategy, false, content);
    }

    /**
     * Logs an info message without console output
     *
     * @param content message content to log
     * @throws NullPointerException if content is null
     */
    public void infoSilent(@NotNull Object... content) {
        Objects.requireNonNull(content, "content cannot be null");
        log(infoStrategy, true, content);
    }

    /**
     * Logs an error message with console output
     *
     * @param content message content to log
     * @throws NullPointerException if content is null
     */
    public void error(@NotNull Object... content) {
        Objects.requireNonNull(content, "content cannot be null");
        log(errorStrategy, false, content);
    }

    /**
     * Logs an error message without console output
     *
     * @param content message content to log
     * @throws NullPointerException if content is null
     */
    public void errorSilent(@NotNull Object... content) {
        Objects.requireNonNull(content, "content cannot be null");
        log(errorStrategy, true, content);
    }

    /**
     * Logs a fatal error message with console output
     *
     * @param content message content to log
     * @throws NullPointerException if content is null
     */
    public void fatal(@NotNull Object... content) {
        Objects.requireNonNull(content, "content cannot be null");
        log(fatalStrategy, false, content);
    }

    /**
     * Logs a fatal error message without console output
     *
     * @param content message content to log
     * @throws NullPointerException if content is null
     */
    public void fatalSilent(@NotNull Object... content) {
        Objects.requireNonNull(content, "content cannot be null");
        log(fatalStrategy, true, content);
    }
}