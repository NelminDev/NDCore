package dev.nelmin.ndcore.builders;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

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
    private final @NotNull PotionEffectType type;
    private int duration = 20; // Default duration (1 second)
    private int amplifier = 0; // Default amplifier (level 1)
    private boolean ambient = false;
    private boolean particles = true;
    private boolean icon = true;

    public PotionEffectBuilder(@NotNull PotionEffectType type) {
        this.type = type;
    }

    public static @NotNull PotionEffectBuilder of(@NotNull PotionEffectType type) {
        return new PotionEffectBuilder(type);
    }

    /**
     * Sets the duration in ticks (20 ticks = 1 second)
     */
    public PotionEffectBuilder duration(int ticks) {
        this.duration = ticks;
        return this;
    }

    /**
     * Sets the duration in seconds (automatically converts to ticks)
     */
    public PotionEffectBuilder durationSeconds(int seconds) {
        this.duration = seconds * 20;
        return this;
    }

    public PotionEffectBuilder amplifier(int level) {
        this.amplifier = level;
        return this;
    }

    public PotionEffectBuilder ambient(boolean ambient) {
        this.ambient = ambient;
        return this;
    }

    public PotionEffectBuilder particles(boolean particles) {
        this.particles = particles;
        return this;
    }

    public PotionEffectBuilder icon(boolean icon) {
        this.icon = icon;
        return this;
    }

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