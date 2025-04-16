package dev.nelmin.ndcore.other;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/**
 * Utility class for handling date and time operations with formatting capabilities
 */
public class DateTime {
    @Getter
    private final Instant instant;

    /**
     * Formatters for date (dd-MM-yyyy) and time (HH:mm:ss.SSS)
     */
    private final Pair<DateTimeFormatter, DateTimeFormatter> formatters =
            new Pair<>(DateTimeFormatter.ofPattern("dd-MM-yyyy"), DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

    public DateTime() {
        this.instant = Instant.now(Clock.systemDefaultZone());
    }

    public DateTime(@NotNull Instant instant) {
        this.instant = instant;
    }

    public DateTime(@NotNull LocalDateTime localDateTime) {
        this.instant = Instant.from(localDateTime.atZone(ZoneId.systemDefault()));
    }

    public DateTime(long epochMilli) {
        this.instant = Instant.ofEpochMilli(epochMilli);
    }

    public DateTime modifyInstant(@NotNull Consumer<Instant> consumer) {
        consumer.accept(instant);
        return this;
    }

    @NotNull
    public String formatTime() {
        return formatters.second().format(localDateTime());
    }

    @NotNull
    public String formatDate() {
        return formatters.first().format(localDateTime());
    }

    @NotNull
    public Pair<String, String> dateTime() {
        return new Pair<>(formatDate(), formatTime());
    }

    @NotNull
    public String format(@NotNull String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime());
    }

    @NotNull
    public String format(@NotNull DateTimeFormatter formatter) {
        return formatter.format(localDateTime());
    }

    @NotNull
    public LocalDateTime localDateTime() {
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}