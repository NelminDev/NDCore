package dev.nelmin.ndcore.other;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Utility class for handling date and time operations with formatting capabilities.
 * All date/time operations use the system default time zone.
 */
public class DateTime {
    @Getter
    private final Instant instant;

    /**
     * Formatters for date (dd-MM-yyyy) and time (HH:mm:ss.SSS)
     */
    private final Pair<DateTimeFormatter, DateTimeFormatter> formatters =
            new Pair<>(DateTimeFormatter.ofPattern("dd-MM-yyyy"), DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

    /**
     * Creates a new DateTime instance with the current system time
     */
    public DateTime() {
        this.instant = Instant.now(Clock.systemDefaultZone());
    }

    /**
     * Creates a new DateTime instance from the given Instant
     *
     * @param instant the instant to create from
     * @throws NullPointerException if instant is null
     */
    public DateTime(@NotNull Instant instant) {
        this.instant = Objects.requireNonNull(instant, "instant must not be null");
    }

    /**
     * Creates a new DateTime instance from the given LocalDateTime
     *
     * @param localDateTime the local date time to create from
     * @throws NullPointerException if localDateTime is null
     */
    public DateTime(@NotNull LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime, "localDateTime must not be null");
        this.instant = Instant.from(localDateTime.atZone(ZoneId.systemDefault()));
    }

    /**
     * Creates a new DateTime instance from epoch milliseconds
     *
     * @param epochMilli milliseconds from the epoch of 1970-01-01T00:00:00Z
     */
    public DateTime(long epochMilli) {
        this.instant = Instant.ofEpochMilli(epochMilli);
    }

    /**
     * Applies the given consumer to modify the instant
     *
     * @param consumer the consumer that modifies the instant
     * @return this DateTime instance for chaining
     * @throws NullPointerException if consumer is null
     */
    public DateTime modifyInstant(@NotNull Consumer<Instant> consumer) {
        Objects.requireNonNull(consumer, "consumer must not be null");
        consumer.accept(instant);
        return this;
    }

    /**
     * Formats the time portion using pattern HH:mm:ss.SSS
     *
     * @return the formatted time string
     */
    @NotNull
    public String formatTime() {
        return formatters.second().format(localDateTime());
    }

    /**
     * Formats the date portion using pattern dd-MM-yyyy
     *
     * @return the formatted date string
     */
    @NotNull
    public String formatDate() {
        return formatters.first().format(localDateTime());
    }

    /**
     * Returns both formatted date and time as a pair
     *
     * @return Pair containing formatted date and time strings
     */
    @NotNull
    public Pair<String, String> dateTime() {
        return new Pair<>(formatDate(), formatTime());
    }

    /**
     * Formats the date/time using a custom pattern
     *
     * @param pattern the pattern to use for formatting
     * @return the formatted date/time string
     * @throws NullPointerException     if pattern is null
     * @throws IllegalArgumentException if pattern is invalid
     * @throws DateTimeParseException   if the date cannot be formatted with the given pattern
     */
    @NotNull
    public String format(@NotNull String pattern) throws IllegalArgumentException, DateTimeParseException {
        Objects.requireNonNull(pattern, "pattern must not be null");
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime());
    }

    /**
     * Formats the date/time using the provided formatter
     *
     * @param formatter the formatter to use
     * @return the formatted date/time string
     * @throws NullPointerException   if formatter is null
     * @throws DateTimeParseException if the date cannot be formatted with the given formatter
     */
    @NotNull
    public String format(@NotNull DateTimeFormatter formatter) throws DateTimeParseException {
        Objects.requireNonNull(formatter, "formatter must not be null");
        return formatter.format(localDateTime());
    }

    /**
     * Converts the instant to a LocalDateTime using system default time zone
     *
     * @return the LocalDateTime representation
     */
    @NotNull
    public LocalDateTime localDateTime() {
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}