package dev.nelmin.minecraft.persistence

import dev.nelmin.minecraft.NDCore
import dev.nelmin.minecraft.NDUtils
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
 * A delegate class for handling a list of persistent properties in a `PersistentDataContainer`.
 *
 * This class allows storing and retrieving lists of custom objects within the Minecraft Bukkit API
 * through the use of a `PersistentDataContainer`. It provides automatic persistence and thread-safe
 * management of list-based properties.
 *
 * @param P The primitive type of the data elements in the list, as defined by `PersistentDataType`.
 * @param C The complex type of the data elements in the list, as defined by `PersistentDataType`.
 * @param namespacedKey A unique identifier for the data stored in the `PersistentDataContainer`.
 * @param type The `PersistentDataType` representing how the list of data elements is stored and retrieved.
 * @param defaultValue The default value for the list property when no explicit value is found in the container.
 * @param container The container where the list data is persisted.
 */
open class PersistentListProperty<P, C : Any>(
    private val namespacedKey: NamespacedKey,
    private val type: PersistentDataType<List<P>, List<C>>,
    private val defaultValue: List<C>,
    private val container: PersistentDataContainer
) : ReadWriteProperty<Any?, List<C>> {

    /**
     * Secondary constructor for the `PersistentListProperty` class.
     *
     * This constructor allows for the creation of a `PersistentListProperty` with a namespaced key
     * derived from a string and plugin instance. The namespaced key is generated using
     * the `NDUtils.getNamespacedKey` function.
     *
     * @param namespacedKeyName The name of the key without the namespace, used to generate a namespaced key.
     * @param type The `PersistentDataType` defining how the list data will be stored and retrieved.
     * @param defaultValue The default list value for the property if no value is present in the container.
     * @param container The `PersistentDataContainer` where the list property is stored.
     * @param plugin The plugin instance used for creating the namespaced key. Defaults to the singleton instance of `NDCore`.
     */
    constructor(
        namespacedKeyName: String,
        type: PersistentDataType<List<P>, List<C>>,
        defaultValue: List<C>,
        container: PersistentDataContainer,
        plugin: Plugin = NDCore.instance()
    ) : this(NDUtils.getNamespacedKey(namespacedKeyName, plugin), type, defaultValue, container)

    /**
     * Retrieves the value of a persistent property from the associated `PersistentDataContainer`.
     * If the value does not exist in the container, it is initialized with a default value
     * and stored back into the container before returning.
     *
     * This retrieval process is synchronized using a mutex to ensure thread safety.
     *
     * @param thisRef The reference to the delegating object associated with the property.
     *                Can be `null` for standalone properties.
     * @param property The metadata of the property, including its name and attributes.
     * @return A list of elements retrieved from the persistent data container, or the `defaultValue`
     *         if no value exists in the container.
     */
    override fun getValue(thisRef: Any?, property: KProperty<*>): List<C> {
        fun save(): List<C> {
            setValue(thisRef, property, defaultValue)
            return defaultValue
        }

        return runBlocking {
            NDCore.mutex.withLock {
                container.get(namespacedKey, type) ?: save()
            }
        }
    }

    /**
     * Sets a new value for the property in the associated `PersistentDataContainer`.
     *
     * This method updates the property's value asynchronously using a coroutine launched on
     * the `Dispatchers.IO` context. The operation is synchronized using a mutex to ensure thread
     * safety for concurrent accesses and modifications.
     *
     * @param thisRef The reference to the delegating object associated with the property.
     *                Can be `null` for standalone properties.
     * @param property Metadata about the property, such as its name and additional attributes.
     * @param value The new value to be stored. This value will be saved in the `PersistentDataContainer`
     *              using the appropriate `namespacedKey` and `type` parameters.
     */
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<C>) {
        NDCore.coroutineScope.launch(Dispatchers.IO) {
            NDCore.mutex.withLock {
                container.set(namespacedKey, type, value)
            }
        }
    }
}
