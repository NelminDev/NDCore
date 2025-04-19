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
        Objects.requireNonNull(key, "Key cannot be null");
        Object value = get(key);
        Objects.requireNonNull(value, "Value for key " + key + " cannot be null");
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
        Objects.requireNonNull(key, "Key cannot be null");
        String value = getString(key);
        Objects.requireNonNull(value, "Value for key " + key + " cannot be null");
        if (value.trim().isEmpty())
            throw new ConfigurationValueNullOrEmptyException("Value for key " + key + " cannot be empty");
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
        Objects.requireNonNull(key, "Key cannot be null");
        Integer value = getInt(key);
        Objects.requireNonNull(value, "Value for key " + key + " cannot be null");
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
        Objects.requireNonNull(key, "Key cannot be null");
        Boolean value = getBoolean(key);
        Objects.requireNonNull(value, "Value for key " + key + " cannot be null");
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
        Objects.requireNonNull(key, "Key cannot be null");
        Double value = getDouble(key);
        Objects.requireNonNull(value, "Value for key " + key + " cannot be null");
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
        Objects.requireNonNull(key, "Key cannot be null");
        Long value = getLong(key);
        Objects.requireNonNull(value, "Value for key " + key + " cannot be null");
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
        Objects.requireNonNull(key, "Key cannot be null");
        List<?> value = getList(key);
        Objects.requireNonNull(value, "Value for key " + key + " cannot be null");
        if (value.isEmpty())
            throw new ConfigurationValueNullOrEmptyException("Value for key " + key + " cannot be empty");
        return value;
    }
}
