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

package me.atilt.buddy.registry.type;

import com.google.common.collect.Iterators;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import me.atilt.buddy.closeable.Closeable;
import me.atilt.buddy.registry.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import com.google.common.base.Preconditions;
import java.util.Set;

/**
 * A fastutil implementation of {@link Registry}. <p>
 * Removes the auto-[un]boxing associated with a standard {@link Map}.
 * <p>
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 *
 * @param <V> the registry value type
 */
public class Int2ObjectRegistry<V> implements Registry<Integer, V> {

    private final Int2ObjectMap<V> handle;
    private boolean closed;

    public Int2ObjectRegistry(@Nonnull Int2ObjectMap<V> handle) {
        Preconditions.checkNotNull(handle, "handle");
        this.handle = handle;
    }

    @Nullable
    public V register(int key, @Nonnull V v) {
        Preconditions.checkNotNull(v, "value");
        return this.handle.put(key, v);
    }

    public void register(@Nonnull Int2ObjectMap<V> all) {
        Preconditions.checkNotNull(all, "all");
        this.handle.putAll(all);
    }
    
    @Nullable
    public V unregister(int key) {
        return this.handle.remove(key);
    }
    
    @Nullable
    public V get(int key) {
        return this.handle.get(key);
    }

    @Nonnull
    public V getOrDefault(int key, @Nonnull V v) {
        Preconditions.checkNotNull(v, "value");
        return this.handle.getOrDefault(key, v);
    }

    @Nonnull
    public ObjectIterator<Int2ObjectMap.Entry<V>> intIterator() {
        return ObjectIterators.unmodifiable(Int2ObjectMaps.fastIterator(this.handle));
    }

    @Deprecated
    @Nullable
    @Override
    public V register(@Nonnull Integer key, @Nonnull V v) {
        Preconditions.checkNotNull(key, "key");
        Preconditions.checkNotNull(v, "value");
        return register(key.intValue(), v);
    }

    @Override
    public void register(@Nonnull Map<? extends Integer, ? extends V> all) {
        Preconditions.checkNotNull(all, "all");
        this.handle.putAll(all);
    }

    @Deprecated
    @Nullable
    @Override
    public V unregister(@Nonnull Integer key) {
        Preconditions.checkNotNull(key, "key");
        return unregister(key.intValue());
    }

    @Deprecated
    @Nullable
    @Override
    public V get(@Nonnull Integer key) {
        Preconditions.checkNotNull(key, "key");
        return get(key.intValue());
    }

    @Deprecated
    @Nonnull
    @Override
    public V getOrDefault(@Nonnull Integer key, @Nonnull V v) {
        Preconditions.checkNotNull(key, "key");
        Preconditions.checkNotNull(v, "value");
        return getOrDefault(key.intValue(), v);
    }

    @Override
    public int size() {
        return this.handle.size();
    }

    @Deprecated
    @Nonnull
    @Override
    public Set<Integer> keys() {
        return IntSets.unmodifiable(this.handle.keySet());
    }

    @Nonnull
    @Override
    public Collection<V> values() {
        return Collections.unmodifiableCollection(this.handle.values());
    }

    @Nonnull
    @Override
    public Map<Integer, V> handle() {
        return Int2ObjectMaps.unmodifiable(this.handle);
    }

    @Override
    public void close() throws Exception {
        for (V value : this.handle.values()) {
            if (value instanceof Closeable) {
                ((Closeable) value).close();
            }
        }
        this.closed = true;
    }

    @Override
    public boolean closed() {
        return this.closed;
    }

    @Deprecated
    @Override
    public Iterator<Map.Entry<Integer, V>> iterator() {
        return Iterators.unmodifiableIterator(this.handle.entrySet().iterator());
    }
}