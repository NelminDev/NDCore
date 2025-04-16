package dev.nelmin.ndcore.persistence;

import dev.nelmin.ndcore.NDCore;
import dev.nelmin.ndcore.NDPlugin;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * Manages persistent properties for players
 * <p>
 * Handles creation and validation of persistent properties that survive server restarts
 *
 * @implNote Uses Bukkit's PersistentDataContainer API for data storage
 */
@Getter
public class PersistentPropertyManager {
    private final @NotNull JavaPlugin plugin;
    private final @NotNull PersistentDataContainer persistentDataContainer;

    /**
     * Creates a new property manager for the given plugin and data container
     *
     * @param plugin                  The plugin that owns this manager
     * @param persistentDataContainer The data container to store properties in
     * @throws NullPointerException If any parameter is null
     */
    public PersistentPropertyManager(
            @NotNull JavaPlugin plugin,
            @NotNull PersistentDataContainer persistentDataContainer
    ) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null");
        this.persistentDataContainer = Objects.requireNonNull(persistentDataContainer, "persistentDataContainer must not be null");
    }

    /**
     * Creates or retrieves a property manager for a player
     * <p>
     * If a manager already exists, returns the existing one
     *
     * @param player The player to get a property manager for
     * @param plugin The plugin that owns the manager
     * @return A property manager for the player
     * @throws NullPointerException If player or plugin is null
     */
    @Contract(value = "_, _, _ -> new", pure = false)
    public static @NotNull PersistentPropertyManager of(
            @NotNull Player player,
            @NotNull NDPlugin plugin
    ) {
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
     * Creates a new persistent property with custom value merging behavior
     *
     * @param <C>               The type of value to store
     * @param namespacedKeyName The name for the property (must follow namespaced key format)
     * @param defaultValue      The default value for the property
     * @param action            The function to apply when merging values
     * @return A new persistent property
     * @throws IllegalArgumentException If namespacedKeyName is invalid or defaultValue is empty
     * @throws NullPointerException     If any parameter is null
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    public <C> @NotNull PersistentProperty<C> create(
            @NotNull String namespacedKeyName,
            @NotNull C defaultValue,
            @NotNull BiFunction<C, C, C> action
    ) {
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
     * Creates a new persistent property that always returns to default value when modified
     *
     * @param <C>               The type of value to store
     * @param namespacedKeyName The name for the property (must follow namespaced key format)
     * @param defaultValue      The default value for the property
     * @return A new persistent property
     * @throws IllegalArgumentException If namespacedKeyName is invalid or defaultValue is empty
     * @throws NullPointerException     If any parameter is null
     */
    @Contract(value = "_, _ -> new", pure = true)
    public <C> @NotNull PersistentProperty<C> create(
            @NotNull String namespacedKeyName,
            @NotNull C defaultValue
    ) {
        Objects.requireNonNull(namespacedKeyName, "namespacedKeyName must not be null");
        Objects.requireNonNull(defaultValue, "defaultValue must not be null");

        validateNamespacedKeyName(namespacedKeyName);
        validateDefaultValue(defaultValue);

        return create(namespacedKeyName, defaultValue, (prev, cur) -> defaultValue);
    }

    /**
     * Validates that a default value is not empty when it's a string or character sequence
     *
     * @param <C>          The type of value to validate
     * @param defaultValue The value to validate
     * @throws IllegalArgumentException If the value is empty
     */
    @Contract(pure = true)
    private <C> void validateDefaultValue(@NotNull C defaultValue) {
        if (defaultValue instanceof String string && string.isBlank()) {
            throw new IllegalArgumentException("Default value cannot be blank when type is String");
        }
        if (defaultValue instanceof CharSequence charSequence && charSequence.isEmpty()) {
            throw new IllegalArgumentException("Default value cannot be empty when type is CharSequence");
        }
    }

    /**
     * Validates that a namespaced key follows the required format
     *
     * @param namespacedKeyName The key name to validate
     * @throws IllegalArgumentException If the key name is invalid
     */
    @Contract(pure = true)
    private void validateNamespacedKeyName(@NotNull String namespacedKeyName) {
        if (namespacedKeyName.isBlank()) {
            throw new IllegalArgumentException("NamespacedKeyName cannot be empty or blank");
        }
        if (!namespacedKeyName.matches("^[a-z0-9._-]+$")) {
            throw new IllegalArgumentException("NamespacedKeyName must only contain lowercase letters, numbers, dots, underscores and hyphens");
        }
    }
}
