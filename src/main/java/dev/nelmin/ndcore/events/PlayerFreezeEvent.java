package dev.nelmin.ndcore.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Called when a player is frozen.
 * <p>
 * If cancelled, the player will not be frozen.
 */
public class PlayerFreezeEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;

    /**
     * Creates a new PlayerFreezeEvent.
     *
     * @param who The player who will be frozen
     * @throws NullPointerException if player is null
     */
    public PlayerFreezeEvent(@NotNull Player who) {
        super(Objects.requireNonNull(who, "Player cannot be null"));
    }

    /**
     * Creates a new PlayerFreezeEvent with async flag.
     *
     * @param who   The player who will be frozen
     * @param async Whether the event is asynchronous
     * @throws NullPointerException if player is null
     */
    public PlayerFreezeEvent(@NotNull Player who, boolean async) {
        super(Objects.requireNonNull(who, "Player cannot be null"), async);
    }

    /**
     * Gets the HandlerList for this event type.
     *
     * @return The HandlerList
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets whether this event is cancelled.
     *
     * @return True if event is cancelled, false otherwise
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets whether this event should be cancelled.
     *
     * @param cancel True to cancel the event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the HandlerList for this event.
     *
     * @return The HandlerList
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}