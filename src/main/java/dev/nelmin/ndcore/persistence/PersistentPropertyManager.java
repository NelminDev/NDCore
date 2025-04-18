package dev.nelmin.ndcore.persistence;

import dev.nelmin.ndcore.NDCore;
import dev.nelmin.ndcore.NDPlugin;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Manages persistent properties for players.
 *
 * <p>Handles creation and validation of persistent properties that survive server restarts.
 * This manager provides a type-safe way to store and retrieve player data.
 *
 * <p>Thread-safe for property creation and validation.
 *
 * @see PersistentProperty for the individual property implementation
 */
public final class PersistentPropertyManager {
    private final @NotNull JavaPlugin plugin;
    private final @NotNull PersistentDataContainer persistentDataContainer;

    /**
     * Creates a new property manager for the given plugin and data container.
     *
     * @param plugin                  The plugin that owns this manager
     * @param persistentDataContainer The data container to store properties in
     * @throws NullPointerException If any parameter is null
     */
    public PersistentPropertyManager(
            @NotNull JavaPlugin plugin,
            @NotNull PersistentDataContainer persistentDataContainer
    ) throws NullPointerException {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null");
        this.persistentDataContainer = Objects.requireNonNull(persistentDataContainer, "persistentDataContainer must not be null");
    }

    /**
     * Creates or retrieves a property manager for a player.
     *
     * <p>If a manager already exists, returns the existing one. This method ensures
     * that only one property manager exists per player per plugin.
     *
     * @param player The player to get a property manager for
     * @param plugin The plugin that owns the manager
     * @return A property manager for the player (never null)
     * @throws NullPointerException If player or plugin is null
     */
    @Contract(value = "_, _ -> new", pure = false)
    public static @NotNull PersistentPropertyManager of(
            @NotNull Player player,
            @NotNull NDPlugin plugin
    ) throws NullPointerException {
        Objects.requireNonNull(player, "player must not be null");
        Objects.requireNonNull(plugin, "plugin must not be null");

        UUID uuid = player.getUniqueId();
        Map<UUID, PersistentPropertyManager> ppm = Objects.requireNonNullElseGet(
                plugin.getPlayerPropertyManagers(),
                JavaPlugin.getPlugin(NDCore.class)::getPlayerPropertyManagers
        );

        PersistentPropertyManager propertyManager = ppm.get(uuid);
        if (null == propertyManager) {
            propertyManager = new PersistentPropertyManager(plugin, player.getPersistentDataContainer());
            ppm.put(uuid, propertyManager);
        }
        return propertyManager;
    }

    /**
     * Returns the plugin that owns this manager.
     *
     * @return The plugin instance
     */
    public @NotNull JavaPlugin plugin() {
        return plugin;
    }

    /**
     * Returns the persistent data container used by this manager.
     *
     * @return The persistent data container
     */
    public @NotNull PersistentDataContainer persistentDataContainer() {
        return persistentDataContainer;
    }

    /**
     * Creates a new persistent property with custom value merging behavior.
     *
     * <p>The property will be stored in the persistent data container with the given key.
     * When the property is updated, the action function will be applied to merge the old
     * and new values.
     *
     * @param <C>               The type of value to store
     * @param namespacedKeyName The name for the property (must follow namespaced key format)
     * @param defaultValue      The default value for the property
     * @param action            The function to apply when merging values (prev, cur) -> result
     * @return A new persistent property (never null)
     * @throws IllegalArgumentException If namespacedKeyName is invalid or defaultValue is empty
     * @throws NullPointerException     If any parameter is null
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public <C> @NotNull PersistentProperty<C> create(
            @NotNull String namespacedKeyName,
            @NotNull C defaultValue,
            @NotNull BiFunction<@NotNull C, @Nullable C, @NotNull C> action
    ) throws IllegalArgumentException, NullPointerException {
        Objects.requireNonNull(namespacedKeyName, "namespacedKeyName must not be null");
        Objects.requireNonNull(defaultValue, "defaultValue must not be null");
        Objects.requireNonNull(action, "action must not be null");

        validateNamespacedKeyName(namespacedKeyName);
        validateDefaultValue(defaultValue);

        return new PersistentProperty<>(
                namespacedKeyName,
                defaultValue,
                action,
                persistentDataContainer,
                plugin
        );
    }

    /**
     * Creates a new persistent property that always returns to default value when modified.
     *
     * <p>The property will be stored in the persistent data container with the given key.
     * This is a simplified version of {@link #create(String, Object, BiFunction)} that
     * always returns the default value regardless of the current value.
     *
     * @param <C>               The type of value to store
     * @param namespacedKeyName The name for the property (must follow namespaced key format)
     * @param defaultValue      The default value for the property
     * @return A new persistent property (never null)
     * @throws IllegalArgumentException If namespacedKeyName is invalid or defaultValue is empty
     * @throws NullPointerException     If any parameter is null
     * @see #create(String, Object, BiFunction) for more control over value merging
     */
    @Contract(value = "_, _ -> new", pure = true)
    public <C> @NotNull PersistentProperty<C> create(
            @NotNull String namespacedKeyName,
            @NotNull C defaultValue
    ) throws IllegalArgumentException, NullPointerException {
        Objects.requireNonNull(namespacedKeyName, "namespacedKeyName must not be null");
        Objects.requireNonNull(defaultValue, "defaultValue must not be null");

        validateNamespacedKeyName(namespacedKeyName);
        validateDefaultValue(defaultValue);

        return create(namespacedKeyName, defaultValue, (prev, cur) -> defaultValue);
    }

    /**
     * Validates that a default value is not empty when it's a string or character sequence.
     *
     * <p>This method ensures that string-based values are not empty or blank, which would
     * make them unusable as default values.
     *
     * @param <C>          The type of value to validate
     * @param defaultValue The value to validate
     * @throws IllegalArgumentException If the value is empty or blank
     * @throws NullPointerException     If defaultValue is null
     */
    @Contract(pure = true)
    private <C> void validateDefaultValue(@NotNull C defaultValue) throws IllegalArgumentException, NullPointerException {
        Objects.requireNonNull(defaultValue, "defaultValue must not be null");

        if (defaultValue instanceof String string && string.isBlank()) {
            throw new IllegalArgumentException("Default value cannot be blank when type is String");
        }
        if (defaultValue instanceof CharSequence charSequence && charSequence.isEmpty()) {
            throw new IllegalArgumentException("Default value cannot be empty when type is CharSequence");
        }
    }

    /**
     * Validates that a namespaced key follows the required format.
     *
     * <p>Namespaced keys must follow Bukkit's naming convention: only lowercase letters,
     * numbers, dots, underscores and hyphens are allowed.
     *
     * @param namespacedKeyName The key name to validate
     * @throws IllegalArgumentException If the key name is invalid, empty or blank
     * @throws NullPointerException     If namespacedKeyName is null
     */
    @Contract(pure = true)
    private void validateNamespacedKeyName(@NotNull String namespacedKeyName) throws IllegalArgumentException, NullPointerException {
        Objects.requireNonNull(namespacedKeyName, "namespacedKeyName must not be null");

        if (namespacedKeyName.isBlank()) {
            throw new IllegalArgumentException("NamespacedKeyName cannot be empty or blank");
        }
        if (!namespacedKeyName.matches("^[a-z0-9._-]+$")) {
            throw new IllegalArgumentException("NamespacedKeyName must only contain lowercase letters, numbers, dots, underscores and hyphens");
        }
    }
}