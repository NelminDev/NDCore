package dev.nelmin.spigot

import org.bukkit.configuration.file.YamlConfiguration

/**
 * The NDCConfig object serves as a configuration holder for the plugin.
 * It provides access to a YAML-based configuration and a default or configurable prefix used throughout the plugin.
 */
object NDCConfig {
    /**
     * Represents the main configuration file for the plugin, which is loaded as a YamlConfiguration.
     * This property may initially be null until properly initialized or loaded.
     */
    var config: YamlConfiguration? = null

    /**
     * Holds the default prefix configuration for messages, typically used
     * to prepend a consistent format or branding to text outputs.
     *
     * Default value is retrieved from the configuration by key "prefix". If
     * not specified in the configuration, a predefined formatted string
     * `&9&lNelmin &8&l»&r` is used as the fallback value.
     */
    var prefix: String = config?.getString("prefix") ?: "&9&lNelmin &8&l»&r"

}