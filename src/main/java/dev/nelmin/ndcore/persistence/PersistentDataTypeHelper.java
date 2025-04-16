package dev.nelmin.ndcore.persistence;

import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class for managing PersistentDataType conversions using the Singleton pattern
 * <p>
 * This class provides utilities for converting between primitive and complex types
 * and their corresponding PersistentDataType representations
 */
public final class PersistentDataTypeHelper {
    private volatile static PersistentDataTypeHelper instance;

    private PersistentDataTypeHelper() {
    }

    /**
     * Gets the singleton instance of PersistentDataTypeHelper
     */
    public static @NotNull PersistentDataTypeHelper instance() {
        if (instance == null) {
            synchronized (PersistentDataTypeHelper.class) {
                if (instance == null) {
                    instance = new PersistentDataTypeHelper();
                }
            }
        }
        return instance;
    }

    /**
     * Converts a class type to its corresponding PersistentDataType
     * <p>
     * Handles both primitive and complex types, converting primitives as needed
     */
    @SuppressWarnings("unchecked")
    public <C> @NotNull PersistentDataType<?, C> type(@NotNull Class<?> clazz) {
        Class<?> complexClass = clazz.isPrimitive() ? primitiveToComplex(clazz) : clazz;
        return switch (complexClass.getSimpleName()) {
            case "Integer" -> (PersistentDataType<?, C>) PersistentDataType.INTEGER;
            case "Integer[]" -> (PersistentDataType<?, C>) PersistentDataType.INTEGER_ARRAY;
            case "Double" -> (PersistentDataType<?, C>) PersistentDataType.DOUBLE;
            case "Float" -> (PersistentDataType<?, C>) PersistentDataType.FLOAT;
            case "Long" -> (PersistentDataType<?, C>) PersistentDataType.LONG;
            case "Long[]" -> (PersistentDataType<?, C>) PersistentDataType.LONG_ARRAY;
            case "Short" -> (PersistentDataType<?, C>) PersistentDataType.SHORT;
            case "Byte" -> (PersistentDataType<?, C>) PersistentDataType.BYTE;
            case "Byte[]" -> (PersistentDataType<?, C>) PersistentDataType.BYTE_ARRAY;
            case "Boolean" -> (PersistentDataType<?, C>) PersistentDataType.BOOLEAN;
            default -> throw new IllegalStateException("Unexpected value: " + clazz.getSimpleName());
        };
    }

    /**
     * Converts primitive class types to their complex equivalents
     * <p>
     * For example: int.class -> Integer.class
     */
    @SuppressWarnings("unchecked")
    private <C> @NotNull Class<C> primitiveToComplex(@NotNull Class<?> clazz) {
        return switch (clazz.getSimpleName()) {
            case "int" -> (Class<C>) Integer.class;
            case "int[]" -> (Class<C>) Integer[].class;
            case "double" -> (Class<C>) Double.class;
            case "float" -> (Class<C>) Float.class;
            case "long" -> (Class<C>) Long.class;
            case "long[]" -> (Class<C>) Long[].class;
            case "short" -> (Class<C>) Short.class;
            case "byte" -> (Class<C>) Byte.class;
            case "byte[]" -> (Class<C>) Byte[].class;
            case "boolean" -> (Class<C>) Boolean.class;
            case "char" -> (Class<C>) Character.class;
            default -> throw new IllegalStateException("Unexpected value: " + clazz.getSimpleName());
        };
    }
}