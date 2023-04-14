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
import me.atilt.buddy.FocusIndex;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class FocusList<T> implements FocusIndex<T>, List<T>, Predicate<Predicate<T>> {

    private final List<T> handle;
    private int focused = 0;

    public FocusList() {
        this.handle = new ArrayList<>();
    }

    public FocusList(@Nonnegative int initialCapacity) {
        Preconditions.checkArgument(initialCapacity >= 0, "capacity out of range: %s", initialCapacity);
        this.handle = new ArrayList<>(initialCapacity);
    }

    public FocusList(@Nonnull List<T> elements) {
        Preconditions.checkNotNull(elements, "elements");
        this.handle = new ArrayList<>(elements);
    }

    /**
     * Get the current index.
     *
     * @return The current index.
     */
    @Nonnegative
    public int focused() {
        return this.focused;
    }

    public boolean spans(@Nonnegative int index) {
        Preconditions.checkArgument(index >= 0, "negative index");
        return index < size();
    }

    /**
     * Set the current index.
     *
     * @param index the index to set as current.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public T focus(@Nonnegative int index) {
        Preconditions.checkArgument(index >= 0, "negative index");
        Preconditions.checkArgument(spans(index), "index out of bounds: %s", index);
        this.focused = index;
        return this.handle.get(this.focused);
    }

    /**
     * Get the current object.
     *
     * @return The current object.
     */
    @Nullable
    public T current() {
        Preconditions.checkArgument(spans(this.focused), "index out of bounds: %s" + this.focused);
        return this.handle.get(this.focused);
    }

    /**
     * Perform an action on the current object.
     *
     * @param action The action to perform on the current object.
     */
    public void forFocus(@Nonnull Consumer<T> action) {
        Preconditions.checkNotNull(action, "action");
        action.accept(current());
    }

    @Override
    public boolean test(Predicate<T> predicate) {
        for (T element : this.handle) {
            if (!predicate.test(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        return this.handle.size();
    }

    @Override
    public boolean isEmpty() {
        return this.handle.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.handle.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.handle.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.handle.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return this.handle.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return this.handle.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return this.handle.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(this.handle).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return this.handle.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return this.handle.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.handle.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.handle.retainAll(c);
    }

    @Override
    public void clear() {
        this.handle.clear();
    }

    @Override
    public T get(int index) {
        return this.handle.get(index);
    }

    @Override
    public T set(int index, T element) {
        return this.handle.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        this.handle.add(index, element);
    }

    @Override
    public T remove(int index) {
        return this.handle.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.handle.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.handle.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return this.handle.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return this.handle.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return this.handle.subList(fromIndex, toIndex);
    }
}