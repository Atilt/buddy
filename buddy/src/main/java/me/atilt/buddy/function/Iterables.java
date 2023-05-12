/*
 * MIT License
 *
 * Copyright (c) 2023, Atilt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.atilt.buddy.function;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Iterables {

    @NonNull
    public static <T extends Map<K, V>, K, V> T toMap(@NonNull Iterable<V> iterable, @NonNull Function<V, K> mapper, @NonNull Supplier<T> destination) {
        T map = destination.get();
        for (V value : iterable) {
            K key = mapper.apply(value);
            map.put(key, value);
        }
        return map;
    }

    @NonNull
    public static <T extends Map<K, V>, K, V> T toMap(@NonNull V[] array, @NonNull Function<V, K> mapper, @NonNull Supplier<T> destination) {
        T map = destination.get();
        for (V value : array) {
            K key = mapper.apply(value);
            map.put(key, value);
        }
        return map;
    }

    @NonNull
    public static <T extends Collection<V>, K, V> T transform(@NonNull K[] array, @NonNull Function<K, V> mapper, @NonNull Supplier<T> destination) {
        T collection = destination.get();
        for (K k : array) {
            V value = mapper.apply(k);
            collection.add(value);
        }
        return collection;
    }

    private Iterables() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}
