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

import me.atilt.buddy.pattern.Builder;
import me.atilt.buddy.state.trigger.Trigger;

import org.checkerframework.checker.nullness.qual.NonNull;
import javax.annotation.Nullable;

public class TransitionBuilder<T extends State> implements Builder<Transition<T>> {

    private T source;
    private T destination;
    private Trigger<?> trigger;
    private boolean reversible;

    @NonNull
    public TransitionBuilder<T> from(@Nullable T source) {
        this.source = source;
        return this;
    }

    @NonNull
    public TransitionBuilder<T> to(@NonNull T destination) {
        this.destination = destination;
        return this;
    }

    @NonNull
    public TransitionBuilder<T> when(@NonNull Trigger<?> trigger) {
        this.trigger = trigger;
        return this;
    }

    @NonNull
    public TransitionBuilder<T> reversible() {
        this.reversible = true;
        return this;
    }

    @NonNull
    @Override
    public Builder<Transition<T>> inherit(@NonNull Transition<T> type) {
        this.destination = type.destination();
        return this;
    }

    @NonNull
    @Override
    public Transition<T> build() {
        if (this.destination == null) {
            throw new IllegalStateException("Destination state is not set");
        }
        return new StateTransition<>(this.source, this.destination, this.trigger, this.reversible);
    }
}