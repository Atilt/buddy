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

package me.atilt.buddy.event;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface Subscription<E> extends Comparable<Subscription<?>> {

    @NonNull
    static <E> SubscriptionBuilder<E> on(@NonNull Class<E> type) {
        return new SubscriptionBuilder<>(type);
    }

    @Override
    default int compareTo(Subscription<?> other) {
        return execution().compareTo(other.execution());
    }

    @NonNull
    Class<? extends E> type();

    @NonNull
    default Lifecycle lifecycle() {
        return () -> false;
    }

    default Execution execution() {
        return Execution.INTERMEDIATE;
    }

    List<Property<E>> properties();

    @NonNull
    Consumer<EventContext<? extends E>> on();

    class ExpiringSubscription<E> implements Subscription<E> {

        private final Class<? extends E> type;
        private final Lifecycle lifecycle;
        private final Execution execution;
        private final List<Property<E>> properties;
        private final Consumer<EventContext<? extends E>> on;

        public ExpiringSubscription(Class<? extends E> type, Lifecycle lifecycle, Execution execution, List<Property<E>> properties, Consumer<EventContext<? extends E>> on) {
            this.type = type;
            this.lifecycle = lifecycle;
            this.execution = execution;
            this.properties = properties;
            this.on = on;
        }

        @Override
        public @NonNull Class<? extends E> type() {
            return this.type;
        }

        @NonNull
        @Override
        public Lifecycle lifecycle() {
            return this.lifecycle;
        }

        @Override
        public Execution execution() {
            return this.execution;
        }

        @Override
        public List<Property<E>> properties() {
            return this.properties;
        }

        @Override
        public @NonNull Consumer<EventContext<? extends E>> on() {
            return this.on;
        }
    }
}