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

package me.atilt.buddy.registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Represents a registry to register objects to associated keys.
 * Backed by a {@link Map} with immutable reads.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 *
 * @param <K> the registry key type
 * @param <V> the registry value type
 */
public interface Registry<K, V> extends Iterable<Map.Entry<K, V>>, AutoCloseable {

    @Nullable
    V register(@Nonnull K k, @Nonnull V v);

    void register(@Nonnull Map<? extends K, ? extends V> all);

    @Nullable
    V unregister(@Nonnull K k);

    @Nullable
    V get(@Nonnull K k);

    @Nonnull
    V getOrDefault(@Nonnull K k, @Nonnull V v);

    int size();

    @Nonnull
    Set<K> keys();

    @Nonnull
    Collection<V> values();

    @Nonnull
    Map<K, V> handle();
}