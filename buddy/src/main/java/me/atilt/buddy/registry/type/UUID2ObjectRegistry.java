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
import java.util.UUID;

/**
 * An implementation of {@link Registry} with {@link UUID}s as keys.
 * <p>
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 *
 * @param <V> the registry value type
 */
public class UUID2ObjectRegistry<V> implements Registry<UUID, V> {

    private final Map<UUID, V> handle;
    private boolean closed;

    public UUID2ObjectRegistry(@Nonnull Map<UUID, V> handle) {
        Preconditions.checkNotNull(handle, "handle");
        this.handle = handle;
    }

    @Nullable
    @Override
    public V register(@Nonnull UUID uuid, @Nonnull V v) {
        return this.handle.put(uuid, v);
    }

    @Override
    public void register(@Nonnull Map<? extends UUID, ? extends V> all) {
        this.handle.putAll(all);
    }

    @Nullable
    @Override
    public V unregister(@Nonnull UUID uuid) {
        return this.handle.remove(uuid);
    }

    @Nullable
    @Override
    public V get(@Nonnull UUID uuid) {
        return this.handle.get(uuid);
    }

    @Nonnull
    @Override
    public V getOrDefault(@Nonnull UUID uuid, @Nonnull V v) {
        return this.handle.getOrDefault(uuid, v);
    }

    @Override
    public int size() {
        return this.handle.size();
    }

    @Nonnull
    @Override
    public Set<UUID> keys() {
        return Collections.unmodifiableSet(this.handle.keySet());
    }

    @Nonnull
    @Override
    public Collection<V> values() {
        return Collections.unmodifiableCollection(this.handle.values());
    }

    @Nonnull
    @Override
    public Map<UUID, V> handle() {
        return Collections.unmodifiableMap(this.handle);
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

    @Override
    public Iterator<Map.Entry<UUID, V>> iterator() {
        return handle().entrySet().iterator();
    }
}