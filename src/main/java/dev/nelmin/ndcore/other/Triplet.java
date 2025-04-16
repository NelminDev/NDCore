package dev.nelmin.ndcore.other;

import org.jetbrains.annotations.Nullable;

/**
 * A record that holds three related values of different types
 * <p>
 *
 * @param <X> Type of the first value
 *            <p>
 * @param <Y> Type of the second value
 *            <p>
 * @param <Z> Type of the third value
 */
public record Triplet<X, Y, Z>(
        @Nullable X first,
        @Nullable Y second,
        @Nullable Z third
) {
}