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
import me.atilt.buddy.util.FocusList;
import me.atilt.buddy.util.Iterators;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class StateFocus implements FocusIndex<State>, Composite<State>, State {

    private boolean closed;
    private boolean started;
    private boolean updating;
    private boolean completed;

    private final FocusList<State> states = new FocusList<>();

    public StateFocus(@Nonnull Collection<State> states) {
        Preconditions.checkNotNull(states, "states");
        this.states.addAll(states);
    }

    protected boolean completedAll() {
        return this.states.test(state -> state.started() && state.completed());
    }

    @Nullable
    public State current() {
        return this.states.current();
    }

    @Nonnull
    @Override
    public Collection<State> latches() {
        return Collections.unmodifiableList(this.states);
    }

    @Override
    public void latch(@Nonnull State composite) {
        Preconditions.checkNotNull(composite, "composite");
        this.states.add(composite);
    }

    @Override
    public void latchAll(@Nonnull Collection<State> composites) {
        Preconditions.checkNotNull(composites, "composites");
        this.states.addAll(composites);
    }

    @Override
    public int focused() {
        return this.states.focused();
    }

    @Nullable
    @Override
    public State focus(int index) {
        int focused = focused();
        State state = this.states.focus(index);

        if (index != focused) {
            state.reset();
        }
        if (this.completed) {
            this.completed = false;
        }
        return state;
    }

    @Override
    public Iterator<State> iterator() {
        return Iterators.unmodifiable(this.states);
    }

    @Override
    public void close() throws Exception {
        for (State state : this.states) {
            state.close();
        }
        this.closed = true;
    }

    @Override
    public boolean closed() {
        return this.closed || completed();
    }

    @Override
    public boolean started() {
        return this.started;
    }

    @Override
    public boolean updating() {
        return this.updating;
    }

    @Override
    public boolean completed() {
        return this.completed;
    }

    @Override
    public void start() {
        if (closed()) {
            throw new IllegalStateException("state closed");
        }
        if (completed()) {
            throw new IllegalStateException("already completed");
        }
        if (started()) {
            throw new IllegalStateException("already started");
        }
        if (this.states.isEmpty()) {
            throw new IllegalStateException("empty state swap");
        }
        this.started = true;

        State current = this.states.current();
        current.start();
    }

    @Override
    public void complete() {
        if (closed()) {
            throw new IllegalStateException("state closed");
        }
        if (!started()) {
            throw new IllegalStateException("not started");
        }
        if (completed()) {
            throw new IllegalStateException("already completed");
        }
        this.updating = false;
        for (State state : this.states) {
            if (!state.completed()) {
                state.complete();
            }
        }
        this.completed = true;
    }

    @Override
    public void update() {
        if (closed()) {
            throw new IllegalStateException("state closed");
        }
        if (!started()) {
            throw new IllegalStateException("not started");
        }
        if (completed()) {
            throw new IllegalStateException("already completed");
        }
        if (updating()) {
            throw new IllegalStateException("already updating");
        }
        this.updating = true;

        State current = this.states.current();
        current.update();

        if (current.completed()) {
            current.complete();
            if (completedAll()) {
                this.completed = true;
            }
        }
        this.updating = false;
    }

    @Override
    public void reset() {
        this.started = false;
        this.closed = false;
        this.updating = false;
        this.completed = false;
    }
}