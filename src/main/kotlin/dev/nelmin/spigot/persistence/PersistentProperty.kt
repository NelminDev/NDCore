package dev.nelmin.spigot.persistence

import dev.nelmin.spigot.NDCore
import dev.nelmin.spigot.NDUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.withLock
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A delegate class for handling persistent properties in a `PersistentDataContainer`.
 *
 * This class simplifies storing and retrieving data associated with custom objects,
 * such as players or other entities, within the Minecraft Bukkit API. It allows you
 * to define and manage properties that automatically persist across sessions using a
 * `PersistentDataContainer`.
 *
 * @param P The primitive type of the data as defined by `PersistentDataType`.
 * @param C The complex type of the data as defined by `PersistentDataType`.
 * @param namespacedKey A unique identifier for the data stored in the `PersistentDataContainer`.
 * @param type The `PersistentDataType` defining how the data will be stored and retrieved.
 * @param defaultValue The default value for the property if no explicit value is found in the container.
 * @param container The container where data will be persisted.
 */
class PersistentProperty<P, C : Any>(
    private val namespacedKey: NamespacedKey,
    private val type: PersistentDataType<P, C>,
    private val defaultValue: C,
    private val container: PersistentDataContainer
) : ReadWriteProperty<Any?, C> {

    /**
     * Secondary constructor for the `PersistentProperty` class.
     *
     * This constructor allows for the creation of a `PersistentProperty` with a namespaced key
     * derived from the given key name and plugin. The namespaced key is generated using the `NDUtils.getNamespacedKey` function.
     *
     * @param namespacedKeyName The name of the key without the namespace, used to generate a namespaced key.
     * @param type The `PersistentDataType` of the property, specifying the type of data stored.
     * @param defaultValue The default value for the property if no value is present in the container.
     * @param container The `PersistentDataContainer` where the property is stored.
     * @param plugin The plugin instance used for creating the namespaced key. Defaults to the singleton instance of `NDCore`.
     */
    constructor(
        namespacedKeyName: String,
        type: PersistentDataType<P, C>,
        defaultValue: C,
        container: PersistentDataContainer,
        plugin: Plugin = NDCore.instance()
    )
            : this(NDUtils.getNamespacedKey(namespacedKeyName, plugin), type, defaultValue, container)

    /**
     * Retrieves the value of the property from the persistent data container associated with the delegate.
     * If no value is found, the `defaultValue` is returned.
     *
     * @param thisRef The object using the property delegate. Can be `null`.
     * @param property Metadata about the property being accessed.
     * @return The current value of the property from the persistent data container, or `defaultValue` if no value is present.
     */
    override fun getValue(thisRef: Any?, property: KProperty<*>): C {
        return runBlocking { NDCore.mutex.withLock { container.getOrDefault(namespacedKey, type, defaultValue) } }
    }

    /**
     * Sets a new value for the property in the associated `PersistentDataContainer`.
     *
     * This method updates the provided property's value asynchronously within a coroutine running on the
     * `Dispatchers.IO` context. The operation is guarded by a mutex to ensure thread safety during the
     * update process.
     *
     * @param thisRef The reference to the owning object which delegated the property.
     *                Can be `null` for non-class properties.
     * @param property The metadata of the delegated property, which includes its name and other attributes.
     * @param value The new value to be stored in the `PersistentDataContainer` associated with
     *              the `namespacedKey` and `type`.
     */
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: C) {
        NDCore.coroutineScope.launch(Dispatchers.IO) {
            NDCore.mutex.withLock {
                container.set(namespacedKey, type, value)
            }
        }
    }
}