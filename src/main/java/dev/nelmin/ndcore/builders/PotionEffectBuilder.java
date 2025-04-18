package dev.nelmin.ndcore.builders;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A builder class to simplify the creation and configuration of {@link PotionEffect} objects
 * for use in Bukkit-based plugins. This class allows constructing potion effects with
 * customizable properties such as type, duration, amplifier, visibility of particles, and more.
 * <p>
 * Each method in this class modifies specific properties of the potion effect and supports
 * method chaining by returning the current instance of {@code PotionEffectBuilder}.
 * <p>
 * The {@code build()} method returns the constructed {@link PotionEffect} instance.
 */
public class PotionEffectBuilder {
    private @NotNull PotionEffectType type;
    private int duration = 20; // Default duration (1 second)
    private int amplifier = 0; // Default amplifier (level 1)
    private boolean ambient = false;
    private boolean particles = true;
    private boolean icon = true;

    /**
     * Creates a new PotionEffectBuilder with the specified effect type.
     *
     * @param type the potion effect type
     * @throws NullPointerException if type is null
     */
    public PotionEffectBuilder(@NotNull PotionEffectType type) {
        this.type = Objects.requireNonNull(type, "type cannot be null");
    }

    /**
     * Creates a new PotionEffectBuilder with a default GLOWING effect type.
     */
    public PotionEffectBuilder() {
        this(PotionEffectType.GLOWING);
    }

    /**
     * Sets the effect type of the potion effect.
     *
     * @param type the potion effect type to set
     * @return this builder instance
     * @throws NullPointerException if type is null
     */
    public PotionEffectBuilder effect(@NotNull PotionEffectType type) {
        this.type = Objects.requireNonNull(type, "type cannot be null");
        return this;
    }

    /**
     * Creates a new PotionEffectBuilder with the specified effect type.
     *
     * @param type the potion effect type
     * @return a new PotionEffectBuilder instance
     * @throws NullPointerException if type is null
     */
    public static @NotNull PotionEffectBuilder of(@NotNull PotionEffectType type) {
        return new PotionEffectBuilder(type);
    }

    /**
     * Sets the duration in ticks (20 ticks = 1 second)
     *
     * @param ticks the duration in ticks
     * @return this builder instance
     * @throws IllegalArgumentException if ticks is negative
     */
    public PotionEffectBuilder duration(int ticks) {
        if (ticks < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        this.duration = ticks;
        return this;
    }

    /**
     * Sets the duration in seconds (automatically converts to ticks)
     *
     * @param seconds the duration in seconds
     * @return this builder instance
     * @throws IllegalArgumentException if seconds is negative
     */
    public PotionEffectBuilder durationSeconds(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        this.duration = seconds * 20;
        return this;
    }

    /**
     * Sets the amplifier (power level) of the effect
     *
     * @param level the amplifier level
     * @return this builder instance
     * @throws IllegalArgumentException if level is negative
     */
    public PotionEffectBuilder amplifier(int level) {
        if (level < 0) {
            throw new IllegalArgumentException("Amplifier cannot be negative");
        }
        this.amplifier = level;
        return this;
    }

    /**
     * Sets whether the effect is ambient (has more transparent particles)
     *
     * @param ambient true if the effect should be ambient
     * @return this builder instance
     */
    public PotionEffectBuilder ambient(boolean ambient) {
        this.ambient = ambient;
        return this;
    }

    /**
     * Sets whether the effect should show particles
     *
     * @param particles true if particles should be shown
     * @return this builder instance
     */
    public PotionEffectBuilder particles(boolean particles) {
        this.particles = particles;
        return this;
    }

    /**
     * Sets whether the effect should show an icon in the player's inventory
     *
     * @param icon true if the icon should be shown
     * @return this builder instance
     */
    public PotionEffectBuilder icon(boolean icon) {
        this.icon = icon;
        return this;
    }

    /**
     * Builds and returns a new PotionEffect with the configured properties
     *
     * @return a new PotionEffect instance
     */
    public @NotNull PotionEffect build() {
        return new PotionEffect(
                type,
                duration,
                amplifier,
                ambient,
                particles,
                icon
        );
    }
}