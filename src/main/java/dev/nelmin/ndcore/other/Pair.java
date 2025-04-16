package dev.nelmin.ndcore.other;

import org.jetbrains.annotations.Nullable;

/**
 * A record that holds two related values of different types
 * <p>
 *
 * @param <X> Type of the first value
 *            <p>
 * @param <Y> Type of the second value
 */
public record Pair<X, Y>(
        @Nullable X first,
        @Nullable Y second
) {
}