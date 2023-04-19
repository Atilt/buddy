package me.atilt.buddy.util;

import com.google.common.base.Preconditions;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import java.util.function.Predicate;

/**
 * Represents an {@link ArrayList} with a tracked
 * cursor to represent an index of the list. Automatically adapts
 * when the cursor position is less than that of the list size.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 *
 * @param <T> the type
 */
public final class CursorList<T> extends ArrayList<T> {

    private static final long serialVersionUID = 768916307993803442L;
    private int cursor = -1;

    public CursorList(@Nonnegative int initialCapacity) {
        super(initialCapacity);
    }

    public CursorList(@Nonnull Collection<? extends T> collection) {
        super(Preconditions.checkNotNull(collection, "collection"));
        if (collection.size() > 0) {
            this.cursor = 0;
        }
    }

    public CursorList() {
        super();
    }

    private void rebound() {
        this.cursor = isEmpty() ? -1 : Math.min(this.cursor, size() - 1);
    }

    /**
     * The current cursor position.
     *
     * @since 1.0.0
     *
     * @return the position
     */
    @CheckReturnValue
    public int cursor() {
        return this.cursor;
    }

    /**
     * Points the cursor to a new position.
     *
     * @since 1.0.0
     *
     * @param cursor the new position
     * @return the element at the specified position
     */
    public T cursor(@Nonnegative int cursor) {
        Preconditions.checkArgument(cursor >= 0 && cursor < size(), "cursor out of range: %s/%s", cursor, size());
        this.cursor = cursor;
        return get(cursor);
    }

    /**
     * Points the cursor to a new position relative to
     * the current position.
     *
     * @since 1.0.0
     *
     * @param amount the relative amount
     * @return the element at the new index
     */
    @Nullable
    public T shift(int amount) {
        int attemptedCursor = this.cursor + amount;
        Preconditions.checkArgument(attemptedCursor >= 0 && attemptedCursor < size(), "shift out of range: %s (%s)", amount, attemptedCursor);
        this.cursor = attemptedCursor;
        return get(attemptedCursor);
    }

    /**
     * Sets the value at the current cursor position.
     * If no elements exist in the list, this will get treated
     * as an add operation.
     *
     * @since 1.0.0
     *
     * @param element the new element
     * @return the existing element
     */
    @Nullable
    public T cursor(@Nullable T element) {
        if (this.cursor == -1) {
            add(element);
            return element;
        } else {
            return set(this.cursor, element);
        }
    }

    /**
     * The element at the current cursor position.
     *
     * @since 1.0.0
     *
     * @return the element
     */
    @Nullable
    public T current() {
        return this.cursor == -1 ? null : get(this.cursor);
    }

    /**
     * Provides an iterator starting at the current cursor
     * position.
     *
     * @since 1.0.0
     *
     * @return the iterator
     */
    @Nonnull
    public ListIterator<T> cursoredIterator() {
        if (this.cursor != -1 && this.cursor < size()) {
            return listIterator(this.cursor);
        } else {
            return Iterators.emptyList();
        }
    }

    @Override
    public void clear() {
        super.clear();
        this.cursor = -1;
    }

    @Override
    public T remove(@Nonnegative int index) {
        Preconditions.checkArgument(index >= 0 && index < size(), "index");
        T removal = super.remove(index);
        rebound();
        return removal;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        boolean removal = super.remove(o);
        if (removal) {
            rebound();
        }
        return removal;
    }

    @Override
    public boolean removeIf(@Nonnull Predicate<? super T> filter) {
        Preconditions.checkNotNull(filter, "filter");
        boolean removed = super.removeIf(filter);
        if (removed) {
            rebound();
        }
        return removed;
    }

    @Override
    public boolean removeAll(@Nonnull Collection<?> collection) {
        Preconditions.checkNotNull(collection, "collection");
        boolean removedAll = super.removeAll(collection);
        rebound();
        return removedAll;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        Preconditions.checkNotNull(collection, "collection");
        boolean retainedAll = super.retainAll(collection);
        rebound();
        return retainedAll;
    }
}