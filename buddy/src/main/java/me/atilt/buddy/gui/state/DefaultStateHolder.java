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

package me.atilt.buddy.gui.state;

import com.google.common.base.Preconditions;
import me.atilt.buddy.function.Lists;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class DefaultStateHolder implements StateHolder {

    private final List<State> states = new ArrayList<>();
    private final int defaultState;

    private int current;

    public DefaultStateHolder(@Nonnull List<State> states, @Nonnegative int defaultState) {
        Preconditions.checkNotNull(states, "states");
        Preconditions.checkArgument(defaultState >= 0 && defaultState < states.size(), "index out of range: %s", defaultState);
        this.states.addAll(states);
        this.defaultState = defaultState;
    }

    @Nonnull
    @Override
    public List<State> states() {
        return Collections.unmodifiableList(this.states);
    }

    @Override
    public int defaultState() {
        return this.defaultState;
    }

    @Override
    public Iterator<State> iterator() {
        return states().iterator();
    }

    @Nonnull
    @Override
    public State previous() {
        return Lists.previous(this.states, this.current);
    }

    @Nonnull
    @Override
    public State next() {
        return Lists.next(this.states, this.current);
    }
}
