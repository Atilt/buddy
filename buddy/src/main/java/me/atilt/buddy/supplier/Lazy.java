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

package me.atilt.buddy.supplier;

import org.checkerframework.checker.nullness.qual.NonNull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A generic Lazy implementation.
 * Unlike standard {@link Supplier<L>}, this will cache the
 * result upon first request.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 *
 * @param <L> the type of object
 */
public final class Lazy<L> implements Supplier<L> {
    
    private final Supplier<L> handle;
    private L value;

    private Lazy(@NonNull Supplier<L> handle) {
        this.handle = handle;
    }

    public static <L> Lazy<L> of(@NonNull Supplier<L> handle) {
        return new Lazy<>(handle);
    }


    /**
     * The underlying supplier that will provide
     * the cached value.
     *
     * @since 1.0.0
     *
     * @return the original supplier
     */
    @NonNull
    public Supplier<L> handle() {
        return this.handle;
    }

    /**
     * Determines if the cached value has been evaluated
     * or generated.
     *
     * @since 1.0.0
     *
     * @return true if the cached value is present
     */
    public boolean evaluated() {
        return this.value != null;
    }

    /**
     * Returns the cached value or null if it has not yet
     * been evaluated. No evaluation is performed.
     *
     * @since 1.0.0
     *
     * @return the cached value
     */
    @Nullable
    public L nullable() {
        return this.value;
    }

    /**
     * Returns the cached value, or caches then returns
     * the value if not yet cached.
     *
     * @since 1.0.0
     *
     * @return the cached value
     */
    @Override
    public L get() {
        if (this.value == null) {
            this.value = this.handle.get();
        }
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lazy)) return false;
        Lazy<?> lazy = (Lazy<?>) o;
        return Objects.equals(this.value, lazy.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
}