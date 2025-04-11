package dev.nelmin.spigot.persistence

import org.bukkit.persistence.PersistentDataType

object PersistentListDataType {
    val STRING = PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING)
    val BYTE = PersistentDataType.LIST.listTypeFrom(PersistentDataType.BYTE)
    val SHORT = PersistentDataType.LIST.listTypeFrom(PersistentDataType.SHORT)
    val INTEGER = PersistentDataType.LIST.listTypeFrom(PersistentDataType.INTEGER)
    val LONG = PersistentDataType.LIST.listTypeFrom(PersistentDataType.LONG)
    val FLOAT = PersistentDataType.LIST.listTypeFrom(PersistentDataType.FLOAT)
    val DOUBLE = PersistentDataType.LIST.listTypeFrom(PersistentDataType.DOUBLE)
    val BYTE_ARRAY = PersistentDataType.LIST.listTypeFrom(PersistentDataType.BYTE_ARRAY)
    val TAG_CONTAINER = PersistentDataType.LIST.listTypeFrom(PersistentDataType.TAG_CONTAINER)
    val TAG_CONTAINER_ARRAY = PersistentDataType.LIST.listTypeFrom(PersistentDataType.TAG_CONTAINER_ARRAY)
}