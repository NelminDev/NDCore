package dev.nelmin.ndcore.other;

import org.jetbrains.annotations.Nullable;

/**
 * A record that holds three related values of different types.
 * This is similar to the {@link Pair} record but holds an additional third value.
 * All three values can be null.
 *
 * @param first  The first value that can be null
 * @param second The second value that can be null
 * @param third  The third value that can be null
 * @param <X>    Type of the first value
 * @param <Y>    Type of the second value
 * @param <Z>    Type of the third value
 */
public record Triplet<X, Y, Z>(
        @Nullable X first,
        @Nullable Y second,
        @Nullable Z third
) {
}