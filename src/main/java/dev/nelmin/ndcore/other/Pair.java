package dev.nelmin.ndcore.other;

import org.jetbrains.annotations.Nullable;

/**
 * A record that holds two related values of different types.
 *
 * @param first  The first value that can be null
 * @param second The second value that can be null
 * @param <X>    Type of the first value
 * @param <Y>    Type of the second value
 */
public record Pair<X, Y>(
        @Nullable X first,
        @Nullable Y second
) {
}