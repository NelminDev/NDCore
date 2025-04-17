package dev.nelmin.ndcore.persistence;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Manages persistent data properties with type safety and default values.
 *
 * <p>This class provides a type-safe wrapper around Bukkit's PersistentDataContainer
 * with support for default values and custom value merging behavior.
 *
 * <p>Thread-safe for read operations, with async write operations.
 *
 * @param <C> The type of value to store
 * @implNote Uses Bukkit's scheduler for async operations
 */
public final class PersistentProperty<C> {
    // Instance variables
    private final @NotNull NamespacedKey namespacedKey;
    private final @NotNull PersistentDataType<?, C> persistentDataType;
    private final @NotNull C defaultValue;
    private final @NotNull PersistentDataContainer container;
    private final @NotNull JavaPlugin plugin;
    private final @NotNull BiFunction<@NotNull C, @Nullable C, @NotNull C> action;

    /**
     * Creates a new persistent property with the specified key, default value, and action.
     *
     * @param namespacedKey The namespaced key for this property
     * @param defaultValue  The default value to use when no value is stored
     * @param action        The function to apply when merging values
     * @param container     The data container to store the property in
     * @param plugin        The plugin that owns this property
     * @throws NullPointerException     If any parameter is null
     * @throws IllegalArgumentException If PersistentDataType for defaultValue class cannot be found
     */
    public PersistentProperty(@NotNull NamespacedKey namespacedKey, @NotNull C defaultValue,
                              @NotNull BiFunction<@NotNull C, @Nullable C, @NotNull C> action,
                              @NotNull PersistentDataContainer container,
                              @NotNull JavaPlugin plugin) throws NullPointerException, IllegalArgumentException {
        this.namespacedKey = Objects.requireNonNull(namespacedKey, "namespacedKey cannot be null");
        this.defaultValue = Objects.requireNonNull(defaultValue, "defaultValue cannot be null");
        this.container = Objects.requireNonNull(container, "container cannot be null");
        this.plugin = Objects.requireNonNull(plugin, "plugin cannot be null");
        this.action = Objects.requireNonNull(action, "action cannot be null");
        this.persistentDataType = PersistentDataTypeHelper.instance().type(defaultValue.getClass());
    }

    /**
     * Creates a new persistent property with the specified key name, default value, and action.
     *
     * @param namespacedKeyName The name for the namespaced key
     * @param defaultValue      The default value to use when no value is stored
     * @param action            The function to apply when merging values
     * @param container         The data container to store the property in
     * @param plugin            The plugin that owns this property
     * @throws NullPointerException     If any parameter is null
     * @throws IllegalArgumentException If PersistentDataType for defaultValue class cannot be found
     */
    public PersistentProperty(@NotNull String namespacedKeyName, @NotNull C defaultValue,
                              @NotNull BiFunction<@NotNull C, @Nullable C, @NotNull C> action,
                              @NotNull PersistentDataContainer container,
                              @NotNull JavaPlugin plugin) throws NullPointerException, IllegalArgumentException {
        this(new NamespacedKey(plugin, Objects.requireNonNull(namespacedKeyName, "namespacedKeyName cannot be null")),
                defaultValue, action, container, plugin);
    }

    /**
     * Creates a new persistent property with the specified key name and default value.
     *
     * <p>This property will always return to its default value when modified.
     *
     * @param namespacedKeyName The name for the namespaced key
     * @param defaultValue      The default value to use when no value is stored
     * @param container         The data container to store the property in
     * @param plugin            The plugin that owns this property
     * @throws NullPointerException     If any parameter is null
     * @throws IllegalArgumentException If PersistentDataType for defaultValue class cannot be found
     */
    public PersistentProperty(@NotNull String namespacedKeyName, @NotNull C defaultValue,
                              @NotNull PersistentDataContainer container,
                              @NotNull JavaPlugin plugin) throws NullPointerException, IllegalArgumentException {
        this(Objects.requireNonNull(namespacedKeyName, "namespacedKeyName cannot be null"),
                defaultValue, (prev, cur) -> defaultValue, container, plugin);
    }

    /**
     * Retrieves the stored value with a post-retrieval action.
     *
     * <p>If no value is stored, the default value is set and returned.
     *
     * @param after        Action to run after retrieving the value
     * @param errorHandler Handler for any exceptions that occur
     * @return The stored value or default value if none exists
     * @throws NullPointerException If after or errorHandler is null
     */
    public @NotNull C get(@NotNull Runnable after, @NotNull Consumer<Exception> errorHandler) throws NullPointerException {
        Objects.requireNonNull(after, "after cannot be null");
        Objects.requireNonNull(errorHandler, "errorHandler cannot be null");

        C value = container.get(namespacedKey, persistentDataType);
        if (null == value) {
            set(defaultValue, errorHandler);
            after.run();
            return defaultValue;
        }
        after.run();
        return value;
    }

    /**
     * Retrieves the stored value.
     *
     * <p>If no value is stored, the default value is set and returned.
     *
     * @param errorHandler Handler for any exceptions that occur
     * @return The stored value or default value if none exists
     * @throws NullPointerException If errorHandler is null
     */
    public @NotNull C get(@NotNull Consumer<Exception> errorHandler) throws NullPointerException {
        Objects.requireNonNull(errorHandler, "errorHandler cannot be null");
        return get(() -> {
        }, errorHandler);
    }

    /**
     * Updates the stored value with a post-update action.
     *
     * <p>The update is performed asynchronously.
     *
     * @param value        The new value to store
     * @param after        Action to run after updating the value
     * @param errorHandler Handler for any exceptions that occur
     * @throws NullPointerException If any parameter is null
     */
    public void set(@NotNull C value, @NotNull Runnable after, @NotNull Consumer<Exception> errorHandler)
            throws NullPointerException {
        Objects.requireNonNull(value, "value cannot be null");
        Objects.requireNonNull(after, "after cannot be null");
        Objects.requireNonNull(errorHandler, "errorHandler cannot be null");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                C currentValue = this.get(errorHandler);
                C newValue = action.apply(value, currentValue);
                container.set(namespacedKey, persistentDataType, newValue);
                after.run();
            } catch (Exception e) {
                errorHandler.accept(e);
            }
        });
    }

    /**
     * Updates the stored value.
     *
     * <p>The update is performed asynchronously.
     *
     * @param value        The new value to store
     * @param errorHandler Handler for any exceptions that occur
     * @throws NullPointerException If any parameter is null
     */
    public void set(@NotNull C value, @NotNull Consumer<Exception> errorHandler) throws NullPointerException {
        Objects.requireNonNull(value, "value cannot be null");
        Objects.requireNonNull(errorHandler, "errorHandler cannot be null");
        set(value, () -> {
        }, errorHandler);
    }

    /**
     * Removes the stored value.
     *
     * <p>The removal is performed asynchronously.
     *
     * @param errorHandler Handler for any exceptions that occur
     * @throws NullPointerException If errorHandler is null
     */
    public void remove(@NotNull Consumer<Exception> errorHandler) throws NullPointerException {
        Objects.requireNonNull(errorHandler, "errorHandler cannot be null");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                container.remove(namespacedKey);
            } catch (Exception e) {
                errorHandler.accept(e);
            }
        });
    }
}