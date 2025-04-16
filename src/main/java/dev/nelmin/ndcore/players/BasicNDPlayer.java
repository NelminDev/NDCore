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

import java.util.function.Consumer;

public class BasicNDPlayer {
    private final Player bukkitPlayer;
    private final PersistentPropertyManager propertyManager;

    @Getter
    private PersistentProperty<String> languageCode;
    @Getter
    private PersistentProperty<Boolean> isFrozen;

    public BasicNDPlayer(@NotNull Player bukkitPlayer, @NotNull PersistentPropertyManager propertyManager) {
        this.bukkitPlayer = bukkitPlayer;
        this.propertyManager = propertyManager;

        setupProperties();
    }

    public static BasicNDPlayer of(Player player, NDPlugin plugin) {
        return new BasicNDPlayer(
                player,
                PersistentPropertyManager.of(player, plugin)
        );
    }

    public void sendLocalizedMessage(LocalizedMessage localizedMessage, Consumer<Exception> errorHandler) {
        new TextBuilder(localizedMessage.getTranslation(LanguageCode.valueOf(languageCode.get(errorHandler))))
                .sendTo(bukkitPlayer, true);
    }

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
