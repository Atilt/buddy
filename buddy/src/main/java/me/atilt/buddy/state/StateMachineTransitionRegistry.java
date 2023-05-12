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

import me.atilt.buddy.state.trigger.TriggerKey;

import org.checkerframework.checker.nullness.qual.NonNull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class StateMachineTransitionRegistry<T extends State> implements TransitionRegistry<T> {

    private final Map<TriggerKey, Transition<T>> transitions;

    public StateMachineTransitionRegistry(@NonNull Map<TriggerKey, Transition<T>> transitions) {
        this.transitions = new HashMap<>(transitions);
    }

    @NonNull
    @Override
    public Map<TriggerKey, Transition<T>> asMap() {
        return Collections.unmodifiableMap(this.transitions);
    }

    @Override
    public Transition<T> apply(TriggerKey triggerKey) {
        Transition<T> transition = this.transitions.get(triggerKey);
        if (transition == null) {
            return null;
        }
        return transition;
    }
}