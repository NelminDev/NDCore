package dev.nelmin.ndcore.persistence;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Manages persistent data properties with type safety and default values
 * <p>
 *
 * @param <C> The type of value to store
 */
public class PersistentProperty<C> {
    private final @NotNull NamespacedKey namespacedKey;
    private final @NotNull PersistentDataType<?, C> persistentDataType;
    private final @NotNull C defaultValue;
    private final @NotNull PersistentDataContainer container;
    private final @NotNull JavaPlugin plugin;
    private final @NotNull BiFunction<@NotNull C, @Nullable C, @NotNull C> action;

    public PersistentProperty(@NotNull NamespacedKey namespacedKey, @NotNull C defaultValue,
                              @NotNull BiFunction<@NotNull C, @Nullable C, @NotNull C> action,
                              @NotNull PersistentDataContainer container,
                              @NotNull JavaPlugin plugin) {
        this.namespacedKey = namespacedKey;
        this.persistentDataType = PersistentDataTypeHelper.instance().type(defaultValue.getClass());
        this.defaultValue = defaultValue;
        this.container = container;
        this.plugin = plugin;
        this.action = action;
    }

    public PersistentProperty(@NotNull String namespacedKeyName, @NotNull C defaultValue,
                              @NotNull BiFunction<@NotNull C, @Nullable C, @NotNull C> action,
                              @NotNull PersistentDataContainer container,
                              @NotNull JavaPlugin plugin) {
        this(new NamespacedKey(plugin, namespacedKeyName), defaultValue, action, container, plugin);
    }

    public PersistentProperty(@NotNull String namespacedKeyName, @NotNull C defaultValue,
                              @NotNull PersistentDataContainer container, @NotNull JavaPlugin plugin) {
        this(new NamespacedKey(plugin, namespacedKeyName), defaultValue, (prev, cur) -> defaultValue, container, plugin);
    }

    /**
     * Retrieves the stored value with a post-retrieval action
     */
    public @NotNull C get(@NotNull Runnable after, @NotNull Consumer<Exception> errorHandler) {
        C value = container.get(namespacedKey, persistentDataType);
        if (value == null) {
            set(defaultValue, errorHandler);
            after.run();
            return defaultValue;
        }
        return value;
    }

    /**
     * Retrieves the stored value
     */
    public @NotNull C get(@NotNull Consumer<Exception> errorHandler) {
        return get(() -> {
        }, errorHandler);
    }

    /**
     * Updates the stored value with a post-update action
     */
    public void set(@NotNull C value, @NotNull Runnable after, @NotNull Consumer<Exception> errorHandler) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                C currentValue = get(errorHandler);
                C newValue = action.apply(value, currentValue);
                container.set(namespacedKey, persistentDataType, newValue);
                after.run();
            } catch (Exception e) {
                errorHandler.accept(e);
            }
        });
    }

    /**
     * Updates the stored value
     */
    public void set(@NotNull C value, @NotNull Consumer<Exception> errorHandler) {
        set(value, () -> {
        }, errorHandler);
    }

    /**
     * Removes the stored value
     */
    public void remove(@NotNull Consumer<Exception> errorHandler) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                container.remove(namespacedKey);
            } catch (Exception e) {
                errorHandler.accept(e);
            }
        });
    }
}