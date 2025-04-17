package dev.nelmin.ndcore.players;

import dev.nelmin.ndcore.NDPlugin;
import dev.nelmin.ndcore.builders.TextBuilder;
import dev.nelmin.ndcore.events.PlayerFreezeEvent;
import dev.nelmin.ndcore.events.PlayerUnfreezeEvent;
import dev.nelmin.ndcore.objects.LanguageCode;
import dev.nelmin.ndcore.objects.LocalizedMessage;
import dev.nelmin.ndcore.persistence.PersistentProperty;
import dev.nelmin.ndcore.persistence.PersistentPropertyManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents a basic player with persistent properties and localization support.
 * <p>
 * This class provides functionality for managing player state, including:
 * <ul>
 *   <li>Language preferences</li>
 *   <li>Freeze state</li>
 *   <li>Localized messaging</li>
 * </ul>
 */
public class BasicNDPlayer {
    private final Player bukkitPlayer;
    private final PersistentPropertyManager propertyManager;

    @Getter
    private PersistentProperty<String> languageCode;
    @Getter
    private PersistentProperty<Boolean> isFrozen;

    /**
     * Creates a new BasicNDPlayer instance.
     *
     * @param bukkitPlayer    The Bukkit player instance
     * @param propertyManager The property manager for persistent data
     * @throws NullPointerException if bukkitPlayer or propertyManager is null
     */
    public BasicNDPlayer(@NotNull Player bukkitPlayer, @NotNull PersistentPropertyManager propertyManager) {
        this.bukkitPlayer = Objects.requireNonNull(bukkitPlayer, "bukkitPlayer cannot be null");
        this.propertyManager = Objects.requireNonNull(propertyManager, "propertyManager cannot be null");

        setupProperties();
    }

    /**
     * Creates a BasicNDPlayer instance from a Player and NDPlugin.
     *
     * @param player The player to create the instance for
     * @param plugin The plugin instance
     * @return A new BasicNDPlayer instance
     * @throws NullPointerException if player or plugin is null
     */
    public static @NotNull BasicNDPlayer of(@NotNull Player player, @NotNull NDPlugin plugin) {
        Objects.requireNonNull(player, "player cannot be null");
        Objects.requireNonNull(plugin, "plugin cannot be null");
        return new BasicNDPlayer(
                player,
                PersistentPropertyManager.of(player, plugin)
        );
    }

    /**
     * Sends a localized message to the player.
     *
     * @param localizedMessage The message to send
     * @param errorHandler     Handler for any exceptions during translation
     * @throws NullPointerException if localizedMessage or errorHandler is null
     */
    public void sendLocalizedMessage(@NotNull LocalizedMessage localizedMessage, @NotNull Consumer<Exception> errorHandler) {
        Objects.requireNonNull(localizedMessage, "localizedMessage cannot be null");
        Objects.requireNonNull(errorHandler, "errorHandler cannot be null");
        new TextBuilder(localizedMessage.getTranslation(LanguageCode.valueOf(languageCode.get(errorHandler))))
                .sendTo(bukkitPlayer, true);
    }

    /**
     * Sets up persistent properties for the player.
     */
    private void setupProperties() {
        this.languageCode = propertyManager.create(
                "languageCode",
                "en"
        );
        this.isFrozen = propertyManager.create(
                "isFrozen",
                false,
                (prev, cur) -> {
                    if (cur)
                        Bukkit.getPluginManager().callEvent(new PlayerFreezeEvent(this.bukkitPlayer));
                    else Bukkit.getPluginManager().callEvent(new PlayerUnfreezeEvent(this.bukkitPlayer));

                    return cur;
                }
        );
    }
}