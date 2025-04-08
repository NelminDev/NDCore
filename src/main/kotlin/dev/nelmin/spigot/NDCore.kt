package dev.nelmin.minecraft

import org.bukkit.plugin.java.JavaPlugin

class NelminPluginCore : JavaPlugin() {

    override fun onEnable() {
        Logger.setName("ND Core")
        Logger.info("Plugin enabled!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
