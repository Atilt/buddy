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

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Consumer;

public final class Iterators {

    @NonNull
    public static <T> Iterator<T> unmodifiable(@NonNull Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator, "iterator");
        return iterator instanceof ImmutableIterator ? iterator : new ImmutableIterator<>(iterator);
    }

    @NonNull
    public static <T> Iterator<T> unmodifiable(@NonNull Collection<T> collection) {
        Preconditions.checkNotNull(collection, "collection");
        return unmodifiable(collection.iterator());
    }

    @NonNull
    public static Iterator empty() {
        return EmptyIterator.EMPTY;
    }

    public static ListIterator emptyList() {
        return EmptyListIterator.EMPTY;
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

    private static final class EmptyIterator<T> implements Iterator<T> {

        static Iterator EMPTY = new EmptyIterator();

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }
    }

    private static final class EmptyListIterator<T> implements ListIterator<T> {

        static ListIterator EMPTY = new EmptyListIterator();

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public T previous() {
            return null;
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }
}