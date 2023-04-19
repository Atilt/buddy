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

package me.atilt.buddy.state;

import com.google.common.base.Preconditions;
import me.atilt.buddy.Composite;
import me.atilt.buddy.FocusIndex;
import me.atilt.buddy.state.exception.StateOperationException;
import me.atilt.buddy.util.CursorList;
import me.atilt.buddy.util.Iterators;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CursoredState extends DefaultState implements Composite<State> {

    private boolean completed;

    private final CursorList<State> states = new CursorList<>();

    public CursoredState(@Nonnull Collection<State> states) {
        Preconditions.checkNotNull(states, "states");
        this.states.addAll(states);
    }

    protected boolean completedAll() {
        return this.states.allMatch(state -> state.started() && state.completed());
    }

    protected void validate(@Nonnull Status status, boolean expected) {
        if (((status == Status.STARTED && started()) ||
                (status == Status.CLOSED && closed()) ||
                (status == Status.COMPLETED && completed()) ||
                (status == Status.UPDATING && updating())) != expected) {
            throw new StateOperationException(
                    "Status \"" + status + "\" expected \"" + expected + "\" but found \"" + !expected + "\""
            );
        }
    }

    @Nullable
    public State state() {
        return this.states.get(this.states.cursor());
    }

    @CheckReturnValue
    public int cursor() {
        return this.states.cursor();
    }

    @Nonnull
    public State cursor(int cursor) {
        validate(Status.STARTED, true);
        validate(Status.CLOSED, false);

        int oldCursor = this.states.cursor();
        State state = this.states.cursor(cursor);

        if (cursor != oldCursor) {
            state.reset();
        }
        if (this.completed) {
            this.completed = false;
        }
        return state;
    }

    @Nonnull
    protected State get(int index) {
        Preconditions.checkArgument(index >= 0 && index < size(), "index out of bounds: %s", index);
        return this.states.get(index);
    }

    @Nonnull
    @Override
    public Collection<State> latches() {
        return Collections.unmodifiableList(this.states);
    }

    @Override
    public int size() {
        return this.states.size();
    }

    @Override
    public boolean latch(@Nonnull State composite) {
        Preconditions.checkNotNull(composite, "composite");
        return this.states.add(composite);
    }

    @Override
    public Collection<State> latchAll(@Nonnull Collection<State> composites) {
        Preconditions.checkNotNull(composites, "composites");
        this.states.addAll(composites);
        return Collections.emptyList();
    }

    @Override
    public Iterator<State> iterator() {
        return Iterators.unmodifiable(this.states);
    }

    @Nonnull
    public Iterator<State> cursoredIterator() {
        return Iterators.unmodifiable(this.states.cursoredIterator());
    }

    @Override
    public void close() throws Exception {
        validate(Status.CLOSED, false);

        for (State state : this.states) {
            state.close();
        }
        super.close();
    }

    @Override
    public boolean closed() {
        return super.closed() || completed();
    }

    @Override
    public void start() {
        validate(Status.STARTED, false);
        validate(Status.CLOSED, false);
        validate(Status.COMPLETED, false);
        Preconditions.checkState(!this.states.isEmpty(), "no states found");

        super.start();

        State current = this.states.current();
        current.start();
    }

    @Override
    public void complete() {
        validate(Status.CLOSED, false);
        validate(Status.COMPLETED, false);

        for (State state : this.states) {
            if (!state.completed()) {
                state.complete();
            }
        }
        super.complete();
    }

    @Override
    public void update() {
        validate(Status.STARTED, true);
        validate(Status.COMPLETED, false);

        if (!updating()) {
            super.update();
        }

        State current = this.states.current();
        current.update();

        if (current.completed()) {
            super.complete();
            current.complete();

            if (completedAll()) {
                this.completed = true;
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.completed = false;
    }
}