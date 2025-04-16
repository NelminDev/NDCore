package dev.nelmin.ndcore.logger;

import dev.nelmin.ndcore.logger.strategy.DefaultLoggingStrategy;
import dev.nelmin.ndcore.logger.strategy.LoggingStrategy;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Utility for handling logging tasks within a plugin
 * <p>
 * Provides different logging levels (INFO, WARN, ERROR, FATAL) with customizable format and behavior
 * using underlying LoggingStrategy to define the behavior of each logging level
 */
public class NDLogger {
    private static final String FORMAT = "[%timestamp] - %strategyName - %loggerName - %content";

    private final @NotNull LoggingStrategy errorStrategy;
    private final @NotNull LoggingStrategy fatalStrategy;
    private final @NotNull LoggingStrategy infoStrategy;
    private final @NotNull LoggingStrategy warnStrategy;

    public NDLogger(@NotNull String loggerName, @NotNull JavaPlugin plugin) {
        this.errorStrategy = new DefaultLoggingStrategy(plugin, FORMAT, loggerName, "ERROR", "&c");
        this.fatalStrategy = new DefaultLoggingStrategy(plugin, FORMAT, loggerName, "FATAL", "&4&l");
        this.infoStrategy = new DefaultLoggingStrategy(plugin, FORMAT, loggerName, "INFO", "&b");
        this.warnStrategy = new DefaultLoggingStrategy(plugin, FORMAT, loggerName, "WARN", "&e");
    }

    /**
     * Logs message using specified strategy
     */
    public void log(@NotNull LoggingStrategy strategy, boolean silent, @NotNull Object... content) {
        strategy.log(silent, content);
    }

    public void warn(@NotNull Object... content) {
        log(warnStrategy, false, content);
    }

    public void warnSilent(@NotNull Object... content) {
        log(warnStrategy, true, content);
    }

    public void info(@NotNull Object... content) {
        log(infoStrategy, false, content);
    }

    public void infoSilent(@NotNull Object... content) {
        log(infoStrategy, true, content);
    }

    public void error(@NotNull Object... content) {
        log(errorStrategy, false, content);
    }

    public void errorSilent(@NotNull Object... content) {
        log(errorStrategy, true, content);
    }

    public void fatal(@NotNull Object... content) {
        log(fatalStrategy, false, content);
    }

    public void fatalSilent(@NotNull Object... content) {
        log(fatalStrategy, true, content);
    }
}