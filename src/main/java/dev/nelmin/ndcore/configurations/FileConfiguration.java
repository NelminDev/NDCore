package dev.nelmin.ndcore.configurations;

import dev.nelmin.ndcore.exceptions.ConfigurationValueNullOrEmptyException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public abstract class FileConfiguration extends org.bukkit.configuration.file.FileConfiguration {

    /**
     * Gets any value for a given key or throws exceptions if not valid
     *
     * @param key The key to get the value for
     * @return The value object
     * @throws NullPointerException if key is null or value is null
     */
    public @NotNull Object getOrThrow(@NotNull String key) throws NullPointerException {
        return getOrThrow(key, "Key cannot be null", "Value for key " + key + " cannot be null");
    }

    /**
     * Gets any value for a given key or throws exceptions if not valid
     *
     * @param key               The key to get the value for
     * @param keyCannotBeNull   Error message when key is null
     * @param valueCannotBeNull Error message when value is null
     * @return The value object
     * @throws NullPointerException if key is null or value is null
     */
    public @NotNull Object getOrThrow(@NotNull String key, @NotNull String keyCannotBeNull, @NotNull String valueCannotBeNull) throws NullPointerException {
        Objects.requireNonNull(key, keyCannotBeNull);
        Object value = get(key);
        Objects.requireNonNull(value, valueCannotBeNull);
        return value;
    }

    /**
     * Gets a string value for a given key or throws exceptions if not valid
     *
     * @param key The key to get the value for
     * @return The non-empty string value
     * @throws NullPointerException                   if key is null or value is null
     * @throws ConfigurationValueNullOrEmptyException if value is empty or blank
     */
    public @NotNull String getStringOrThrow(@NotNull String key) throws NullPointerException, ConfigurationValueNullOrEmptyException {
        return getStringOrThrow(key, "Key cannot be null", "Value for key " + key + " cannot be null", "Value for key " + key + " cannot be empty");
    }

    /**
     * Gets a string value for a given key or throws exceptions if not valid
     *
     * @param key                The key to get the value for
     * @param keyCannotBeNull    Error message when key is null
     * @param valueCannotBeNull  Error message when value is null
     * @param valueCannotBeEmpty Error message when value is empty
     * @return The non-empty string value
     * @throws NullPointerException                   if key is null or value is null
     * @throws ConfigurationValueNullOrEmptyException if value is empty or blank
     */
    public @NotNull String getStringOrThrow(@NotNull String key, @NotNull String keyCannotBeNull, @NotNull String valueCannotBeNull, @NotNull String valueCannotBeEmpty) throws NullPointerException, ConfigurationValueNullOrEmptyException {
        Objects.requireNonNull(key, keyCannotBeNull);
        String value = getString(key);
        Objects.requireNonNull(value, valueCannotBeNull);
        if (value.trim().isEmpty())
            throw new ConfigurationValueNullOrEmptyException(valueCannotBeEmpty);
        return value;
    }

    /**
     * Gets an integer value for a given key or throws exceptions if not valid
     *
     * @param key The key to get the value for
     * @return The integer value, never null
     * @throws NullPointerException if key is null or value is null
     */
    public @NotNull Integer getIntOrThrow(@NotNull String key) throws NullPointerException {
        return getIntOrThrow(key, "Key cannot be null", "Value for key " + key + " cannot be null");
    }

    /**
     * Gets an integer value for a given key or throws exceptions if not valid
     *
     * @param key               The key to get the value for
     * @param keyCannotBeNull   Error message when key is null
     * @param valueCannotBeNull Error message when value is null
     * @return The integer value, never null
     * @throws NullPointerException if key is null or value is null
     */
    public @NotNull Integer getIntOrThrow(@NotNull String key, @NotNull String keyCannotBeNull, @NotNull String valueCannotBeNull) throws NullPointerException {
        Objects.requireNonNull(key, keyCannotBeNull);
        Integer value = getInt(key);
        Objects.requireNonNull(value, valueCannotBeNull);
        return value;
    }

    /**
     * Gets a boolean value for a given key or throws exceptions if not valid
     *
     * @param key The key to get the value for
     * @return The boolean value, never null
     * @throws NullPointerException if key is null or value is null
     */
    public @NotNull Boolean getBooleanOrThrow(@NotNull String key) throws NullPointerException {
        return getBooleanOrThrow(key, "Key cannot be null", "Value for key " + key + " cannot be null");
    }

    /**
     * Gets a boolean value for a given key or throws exceptions if not valid
     *
     * @param key               The key to get the value for
     * @param keyCannotBeNull   Error message when key is null
     * @param valueCannotBeNull Error message when value is null
     * @return The boolean value, never null
     * @throws NullPointerException if key is null or value is null
     */
    public @NotNull Boolean getBooleanOrThrow(@NotNull String key, @NotNull String keyCannotBeNull, @NotNull String valueCannotBeNull) throws NullPointerException {
        Objects.requireNonNull(key, keyCannotBeNull);
        Boolean value = getBoolean(key);
        Objects.requireNonNull(value, valueCannotBeNull);
        return value;
    }

    /**
     * Gets a double value for a given key or throws exceptions if not valid
     *
     * @param key The key to get the value for
     * @return The double value, never null
     * @throws NullPointerException if key is null or value is null
     */
    public @NotNull Double getDoubleOrThrow(@NotNull String key) throws NullPointerException {
        return getDoubleOrThrow(key, "Key cannot be null", "Value for key " + key + " cannot be null");
    }

    /**
     * Gets a double value for a given key or throws exceptions if not valid
     *
     * @param key               The key to get the value for
     * @param keyCannotBeNull   Error message when key is null
     * @param valueCannotBeNull Error message when value is null
     * @return The double value, never null
     * @throws NullPointerException if key is null or value is null
     */
    public @NotNull Double getDoubleOrThrow(@NotNull String key, @NotNull String keyCannotBeNull, @NotNull String valueCannotBeNull) throws NullPointerException {
        Objects.requireNonNull(key, keyCannotBeNull);
        Double value = getDouble(key);
        Objects.requireNonNull(value, valueCannotBeNull);
        return value;
    }

    /**
     * Gets a long value for a given key or throws exceptions if not valid
     *
     * @param key The key to get the value for
     * @return The long value, never null
     * @throws NullPointerException if key is null or value is null
     */
    public @NotNull Long getLongOrThrow(@NotNull String key) throws NullPointerException {
        return getLongOrThrow(key, "Key cannot be null", "Value for key " + key + " cannot be null");
    }

    /**
     * Gets a long value for a given key or throws exceptions if not valid
     *
     * @param key               The key to get the value for
     * @param keyCannotBeNull   Error message when key is null
     * @param valueCannotBeNull Error message when value is null
     * @return The long value, never null
     * @throws NullPointerException if key is null or value is null
     */
    public @NotNull Long getLongOrThrow(@NotNull String key, @NotNull String keyCannotBeNull, @NotNull String valueCannotBeNull) throws NullPointerException {
        Objects.requireNonNull(key, keyCannotBeNull);
        Long value = getLong(key);
        Objects.requireNonNull(value, valueCannotBeNull);
        return value;
    }

    /**
     * Gets a list value for a given key or throws exceptions if not valid
     *
     * @param key The key to get the value for
     * @return The non-empty list value
     * @throws NullPointerException                   if key is null or value is null
     * @throws ConfigurationValueNullOrEmptyException if the list is empty
     */
    public @NotNull List<?> getListOrThrow(@NotNull String key) throws ConfigurationValueNullOrEmptyException, NullPointerException {
        return getListOrThrow(key, "Key cannot be null", "Value for key " + key + " cannot be null", "Value for key " + key + " cannot be empty");
    }

    /**
     * Gets a list value for a given key or throws exceptions if not valid
     *
     * @param key                The key to get the value for
     * @param keyCannotBeNull    Error message when key is null
     * @param valueCannotBeNull  Error message when value is null
     * @param valueCannotBeEmpty Error message when list is empty
     * @return The non-empty list value
     * @throws NullPointerException                   if key is null or value is null
     * @throws ConfigurationValueNullOrEmptyException if the list is empty
     */
    public @NotNull List<?> getListOrThrow(@NotNull String key, @NotNull String keyCannotBeNull, @NotNull String valueCannotBeNull, @NotNull String valueCannotBeEmpty) throws ConfigurationValueNullOrEmptyException, NullPointerException {
        Objects.requireNonNull(key, keyCannotBeNull);
        List<?> value = getList(key);
        Objects.requireNonNull(value, valueCannotBeNull);
        if (value.isEmpty())
            throw new ConfigurationValueNullOrEmptyException(valueCannotBeEmpty);
        return value;
    }
}