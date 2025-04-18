package dev.nelmin.ndcore.persistence;

import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Helper class for managing PersistentDataType conversions using the Singleton pattern.
 *
 * <p>This class provides utilities for converting between primitive and complex types
 * and their corresponding PersistentDataType representations.
 *
 * <p>Thread-safe implementation using double-checked locking.
 *
 * @since 1.0.0
 */
public final class PersistentDataTypeHelper {
    private volatile static PersistentDataTypeHelper instance;

    /**
     * Private constructor to prevent instantiation outside a singleton pattern.
     */
    private PersistentDataTypeHelper() {
    }

    /**
     * Gets the singleton instance of PersistentDataTypeHelper.
     *
     * @return The singleton instance
     */
    public static @NotNull PersistentDataTypeHelper instance() {
        if (null == instance) {
            synchronized (PersistentDataTypeHelper.class) {
                if (null == instance) {
                    instance = new PersistentDataTypeHelper();
                }
            }
        }
        return instance;
    }

    /**
     * Converts a class type to its corresponding PersistentDataType.
     *
     * <p>Handles both primitive and complex types, converting primitives as needed.
     *
     * @param <C>   The complex type to convert to
     * @param clazz The class to convert
     * @return The corresponding PersistentDataType
     * @throws IllegalStateException If the class type is not supported
     * @throws NullPointerException  If clazz is null
     */
    @SuppressWarnings("unchecked")
    public <C> @NotNull PersistentDataType<?, C> type(@NotNull Class<?> clazz) throws IllegalStateException {
        Objects.requireNonNull(clazz, "Class cannot be null");
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
            default -> throw new IllegalStateException("Unsupported class type: " + clazz.getSimpleName());
        };
    }

    /**
     * Converts primitive class types to their complex equivalents.
     *
     * <p>For example, int.class -> Integer.class
     *
     * @param <C>   The complex type to convert to
     * @param clazz The primitive class to convert
     * @return The corresponding complex class
     * @throws IllegalStateException If the primitive type is not supported
     * @throws NullPointerException  If clazz is null
     */
    @SuppressWarnings("unchecked")
    private <C> @NotNull Class<C> primitiveToComplex(@NotNull Class<?> clazz) throws IllegalStateException {
        Objects.requireNonNull(clazz, "Class cannot be null");
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
            default -> throw new IllegalStateException("Unsupported primitive type: " + clazz.getSimpleName());
        };
    }
}