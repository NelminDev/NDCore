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
 * A delegate class for managing mutable lists in a `PersistentDataContainer`.
 *
 * This class provides functionality to store and retrieve mutable lists of complex types
 * in a `PersistentDataContainer` within the Minecraft Bukkit API. It ensures data persistence
 * and thread safety for managing the data across gameplay sessions.
 *
 * @param P The primitive type used in the `PersistentDataType` to store the list.
 * @param C The complex type of the elements in the list, as defined by `PersistentDataType`.
 * @param namespacedKey The unique identifier for accessing the list data in the container.
 * @param type The `PersistentDataType` defining how the list data is encoded and decoded.
 * @param defaultValue The default list value to be used if no data is present in the container.
 * @param container The `PersistentDataContainer` where the list data is stored.
 */
class PersistentMutableListProperty<P, C : Any>(
    private val namespacedKey: NamespacedKey,
    private val type: PersistentDataType<List<P>, List<C>>,
    private val defaultValue: MutableList<C>,
    private val container: PersistentDataContainer
) : ReadWriteProperty<Any?, MutableList<C>> {

    /**
     * Secondary constructor for `PersistentMutableListProperty`.
     *
     * This constructor enables the creation of a `PersistentMutableListProperty` by generating
     * a `NamespacedKey` using the specified `namespacedKeyName` and `plugin` instance.
     *
     * @param namespacedKeyName The name used to generate a `NamespacedKey` for identifying the data.
     * @param type The `PersistentDataType` defining how the data will be serialized and deserialized.
     * @param defaultValue The default value of the property when no value is present in the container.
     *                     Must be a mutable list.
     * @param container The `PersistentDataContainer` where the data is stored.
     * @param plugin The plugin instance used for generating the `NamespacedKey`. Defaults to the singleton instance of `NDCore`.
     */
    constructor(
        namespacedKeyName: String,
        type: PersistentDataType<List<P>, List<C>>,
        defaultValue: MutableList<C>,
        container: PersistentDataContainer,
        plugin: Plugin = NDCore.instance()
    ) : this(NDUtils.getNamespacedKey(namespacedKeyName, plugin), type, defaultValue, container)

    /**
     * Retrieves the value of the delegated property from a `PersistentDataContainer` as a mutable list.
     * If no value exists for the specified key and type, the `defaultValue` is saved and returned.
     *
     * The operation is executed in a thread-safe manner using a coroutine and a locking mechanism.
     *
     * @param thisRef The reference to the object using the property delegate. Can be `null`.
     * @param property Metadata about the property being accessed.
     * @return The current value of the property as a mutable list, or the `defaultValue` if no value is present.
     */
    override fun getValue(thisRef: Any?, property: KProperty<*>): MutableList<C> {
        fun save(): MutableList<C> {
            setValue(thisRef, property, defaultValue)
            return defaultValue
        }

        return runBlocking {
            NDCore.mutex.withLock {
                (container.get(namespacedKey, type)?.toMutableList() ?: save())
            }
        }
    }

    /**
     * Updates the value of the delegated property in the associated `PersistentDataContainer`.
     *
     * This method asynchronously sets a new value in the `PersistentDataContainer` for the provided namespaced key
     * using a coroutine running on the `Dispatchers.IO` context. The operation is thread-safe as it is synchronized
     * using a mutex.
     *
     * @param thisRef The reference to the object using the property delegate. Can be `null`.
     * @param property Metadata about the property being updated.
     * @param value The new value to be stored in the `PersistentDataContainer` for the defined key and type.
     */
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: MutableList<C>) {
        NDCore.coroutineScope.launch(Dispatchers.IO) {
            NDCore.mutex.withLock {
                container.set(namespacedKey, type, value)
            }
        }
    }
}