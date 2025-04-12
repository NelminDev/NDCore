package dev.nelmin.minecraft.builders

import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * A builder class for creating customizable `PotionEffect` instances in a more readable and flexible manner.
 *
 * This class allows you to specify and configure properties such as duration, amplifier, ambient effect,
 * particle visibility, and the associated status icon before creating the `PotionEffect` object.
 *
 * The default values for the configuration are:
 * - Duration: 20 ticks (1 second)
 * - Amplifier: 0 (the first level of the effect)
 * - Ambient: false (not considered an ambient effect)
 * - Particles: true (particles are visible)
 * - Icon: true (status icon is displayed)
 *
 * @param type The `PotionEffectType` that represents the type of the potion effect.
 */
class PotionEffectBuilder(private val type: PotionEffectType) {
    /**
     * Represents the duration of the potion effect in ticks.
     *
     * The value determines how long the effect will last when applied.
     * Defaults to 20 ticks (equivalent to 1 second in-game time).
     */
    private var duration: Int = 20 // Default duration (1 second)

    /**
     * Represents the amplifier level for the potion effect.
     * The amplifier determines the strength or potency of the potion effect.
     * Default value is 0, which corresponds to the base level of the effect.
     * For example, higher amplifier values will increase the effect's intensity.
     */
    private var amplifier: Int = 0 // Default amplifier (level 1)

    /**
     * Determines whether the potion effect is considered ambient.
     * Ambient effects have a subtle visual effect compared to non-ambient ones.
     */
    private var ambient: Boolean = false

    /**
     * Determines whether the potion effect will display particles when applied.
     * If set to `true`, particles will be shown; otherwise, no particles will be visible.
     */
    private var particles: Boolean = true

    /**
     * Determines whether the potion effect's icon is displayed in the HUD (Heads-Up Display).
     * When set to `true`, the icon will be visible; otherwise, it will be hidden.
     */
    private var icon: Boolean = true

    /**
     * Sets the duration of the potion effect in game ticks (1 second = 20 ticks).
     *
     * @param ticks The duration of the effect in ticks.
     * @return The current instance of PotionEffectBuilder for method chaining.
     */
    fun duration(ticks: Int): PotionEffectBuilder {
        this.duration = ticks
        return this
    }

    /**
     * Sets the duration of the potion effect in seconds.
     *
     * @param seconds The duration of the potion effect in seconds.
     * This will be internally multiplied by 20 to convert seconds to ticks.
     * @return The current instance of [PotionEffectBuilder] for chaining.
     */
    fun durationSeconds(seconds: Int): PotionEffectBuilder {
        this.duration = seconds * 20
        return this
    }

    /**
     * Sets the amplifier (level) of the potion effect.
     *
     * @param level the amplifier level to set, where 0 represents level 1, 1 represents level 2, and so on
     * @return the updated PotionEffectBuilder instance
     */
    fun amplifier(level: Int): PotionEffectBuilder {
        this.amplifier = level
        return this
    }

    /**
     * Sets whether the potion effect is ambient. Ambient potion effects produce fewer visible particles.
     *
     * @param ambient true if the potion effect should be ambient, false otherwise
     * @return the current instance of PotionEffectBuilder for chaining
     */
    fun ambient(ambient: Boolean): PotionEffectBuilder {
        this.ambient = ambient
        return this
    }

    /**
     * Sets whether particles should be visible for the potion effect.
     *
     * @param particles whether particles should be visible (true to show particles, false to hide them)
     * @return the updated PotionEffectBuilder instance
     */
    fun particles(particles: Boolean): PotionEffectBuilder {
        this.particles = particles
        return this
    }

    /**
     * Sets whether the potion effect will have an icon displayed in the HUD.
     *
     * @param icon true to display the icon in the HUD, false otherwise
     * @return the current instance of PotionEffectBuilder for chaining
     */
    fun icon(icon: Boolean): PotionEffectBuilder {
        this.icon = icon
        return this
    }

    /**
     * Builds and returns a new instance of `PotionEffect` based on the configured properties.
     *
     * @return A newly created `PotionEffect` instance configured with the specified type, duration, amplifier,
     * ambient state, particles visibility, and icon visibility.
     */
    fun build(): PotionEffect {
        return PotionEffect(
            type,
            duration,
            amplifier,
            ambient,
            particles,
            icon
        )
    }

    /**
     * Companion object providing utility methods to create instances of PotionEffectBuilder.
     */
    companion object {
        /**
         * Creates a new instance of `PotionEffectBuilder` with the specified potion effect type.
         *
         * @param type The type of potion effect to be applied.
         * @return A new `PotionEffectBuilder` instance for building potion effects.
         */
        @JvmStatic
        fun of(type: PotionEffectType): PotionEffectBuilder {
            return PotionEffectBuilder(type)
        }
    }
}