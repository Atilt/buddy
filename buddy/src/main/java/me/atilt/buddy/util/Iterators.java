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

package me.atilt.buddy.util;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

public final class Iterators {

    @Nonnull
    public static <T> Iterator<T> unmodifiable(@Nonnull Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator, "iterator");
        return iterator instanceof ImmutableIterator ? iterator : new ImmutableIterator<>(iterator);
    }

    @Nonnull
    public static <T> Iterator<T> unmodifiable(@Nonnull Collection<T> collection) {
        Preconditions.checkNotNull(collection, "collection");
        return unmodifiable(collection.iterator());
    }

    private Iterators() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    private static final class ImmutableIterator<T> implements Iterator<T> {

        private final Iterator<T> iterator;

        ImmutableIterator(Iterator<T> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public T next() {
            return this.iterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            this.iterator.forEachRemaining(action);
        }
    }
}