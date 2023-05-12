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
import me.atilt.buddy.state.trigger.TriggerKey;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.function.Function;

public abstract class FiniteStateMachine<T extends State> implements StateMachine<T> {

    private T current;

    private final T source;
    private final TransitionRegistry<T> transitionRegistry;

    public FiniteStateMachine(@NonNull T source, @NonNull Function<T, TransitionRegistryBuilder<T>> transitionRegistryBuilder) {
        this.source = source;
        this.current = this.source;
        this.transitionRegistry = transitionRegistryBuilder.apply(this.source).build();
    }



    @NonNull
    @Override
    public T source() {
        return this.source;
    }

    @NonNull
    @Override
    public TransitionRegistry<T> transitioner() {
        return this.transitionRegistry;
    }

    @NonNull
    @Override
    public T current() {
        return this.current;
    }

    @Override
    public boolean trigger(TriggerKey key) {
        Transition<T> transition = this.transitionRegistry.apply(key);

        T transitionSource = transition.source();
        if (transitionSource == null) {
            transitionSource = this.source.self();
        } else {
            transitionSource = transitionSource.self();
        }

        if (transitionSource.equals(this.current)) {
            if (!this.current.update()) {
                this.current.exit();
                this.current = transition.destination().self();
                this.current.enter();
            }
        } else if (transition.reversible() && transition.destination().self().equals(this.current)) {
            if (!this.current.update()) {
                this.current.exit();
                this.current = transitionSource;
                this.current.enter();
            }
        } else {
            this.current.exit();
            this.current = transitionSource;
            this.current.enter();
        }
        return true;
    }
}