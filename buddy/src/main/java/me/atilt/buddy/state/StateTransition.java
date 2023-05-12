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

import me.atilt.buddy.state.trigger.Trigger;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Objects;

public class StateTransition<T extends State> implements Transition<T> {

    private final T source;
    private final T destination;
    private final Trigger<?> trigger;

    private final boolean reversible;

    public StateTransition(@NonNull T source, @NonNull T destination, @NonNull Trigger<?> trigger, boolean reversible) {
        this.source = source;
        this.destination = destination;
        this.trigger = trigger;
        this.reversible = reversible;
    }

    @NonNull
    @Override
    public T source() {
        return this.source;
    }

    @NonNull
    @Override
    public T destination() {
        return this.destination;
    }

    @NonNull
    @Override
    public Trigger<?> when() {
        return this.trigger;
    }

    @Override
    public boolean reversible() {
        return this.reversible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateTransition)) return false;
        StateTransition<?> that = (StateTransition<?>) o;
        boolean equals = source.equals(that.source);

        System.out.println("State Transition Equals Check: " + equals);

        return equals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, trigger);
    }
}