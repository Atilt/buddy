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

import me.atilt.buddy.function.Iterables;
import me.atilt.buddy.pattern.Builder;
import me.atilt.buddy.state.trigger.Trigger;

import org.checkerframework.checker.nullness.qual.NonNull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TransitionRegistryBuilder<T extends State> implements Builder<TransitionRegistry<T>> {

    private final List<Transition<T>> transitions = new ArrayList<>();

    @NonNull
    public TransitionRegistryBuilder<T> transition(@NonNull Transition<T> transition) {
        this.transitions.add(transition);
        return this;
    }

    @NonNull
    @Override
    public TransitionRegistryBuilder<T> inherit(@NonNull TransitionRegistry<T> type) {
        this.transitions.addAll(type.asMap().values());
        return this;
    }

    @NonNull
    @Override
    public TransitionRegistry<T> build() {
        return new StateMachineTransitionRegistry<>(Iterables.toMap(this.transitions, transition -> transition.when().key(), HashMap::new));
    }
}