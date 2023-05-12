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

package me.atilt.buddy.event;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.Function;

public interface Property<E> extends Function<E, Object> {

    @NonNull
    static <T> Property<T> of(@NonNull String key, @NonNull Function<T, Object> function) {
        return new DefaultProperty<>(key, function);
    }

    @NonNull
    String key();

    @NonNull
    Object value();

    boolean undefined();

    static class DefaultProperty<E> implements Property<E> {

        private final String key;
        private final Function<E, Object> function;

        private Object value;

        public DefaultProperty(String key,  Function<E, Object> function) {
            this.key = key;
            this.function = function;
        }

        @Override
        public @NonNull String key() {
            return this.key;
        }

        @Override
        public @NonNull Object value() {
            return this.value;
        }

        @Override
        public boolean undefined() {
            return this.value == null;
        }

        @Override
        public Object apply(E e) {
            if (undefined()) {
                this.value = this.function.apply(e);
            }
            return this.value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DefaultProperty)) return false;
            DefaultProperty<?> that = (DefaultProperty<?>) o;
            return key.equals(that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }
}