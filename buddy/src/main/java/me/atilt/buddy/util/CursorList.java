package me.atilt.buddy.util;

import com.google.common.base.Preconditions;
import me.atilt.buddy.state.State;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

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

    @CheckReturnValue
    public int cursor() {
        return this.cursor;
    }

    public T cursor(@Nonnegative int cursor) {
        Preconditions.checkArgument(cursor >= 0, "cursor out of range: %s/%s", cursor, size());
        this.cursor = cursor;
        return get(cursor);
    }

    @Nullable
    public T current() {
        return this.cursor == -1 ? null : get(this.cursor);
    }

    public ListIterator<T> cursoredIterator() {
        if (this.cursor != -1 && this.cursor < size() - 1) {
            return listIterator(this.cursor);
        } else {
            return Iterators.emptyList();
        }
    }

    public boolean allMatch(@Nonnull Predicate<T> predicate) {
        return !anyMatch(predicate.negate());
    }

    public boolean anyMatch(@Nonnull Predicate<T> predicate) {
        for (T element : this) {
            if (predicate.test(element)) {
                return true;
            }
        }
        return false;
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