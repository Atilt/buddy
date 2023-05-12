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

import me.atilt.buddy.pattern.Builder;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SubscriptionBuilder<E> implements Builder<Subscription<E>> {

    private Class<? extends E> type;
    private Lifecycle lifecycle;
    private Execution execution;
    private List<Property<E>> properties;
    private Consumer<EventContext<? extends E>> on;

    public SubscriptionBuilder(Class<? extends E> type) {
        this.type = type;
        this.lifecycle = () -> false;
        this.execution = Execution.INTERMEDIATE;
        this.properties = Collections.emptyList();
    }

    @NonNull
    public SubscriptionBuilder<E> lifecycle(@NonNull Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
        return this;
    }

    @NonNull
    public SubscriptionBuilder<E> execution(Execution execution) {
        this.execution = execution;
        return this;
    }

    @NonNull
    public <T extends E> SubscriptionBuilder<E> properties(@NonNull List<Property<E>> properties) {
        this.properties = properties;
        return this;
    }

    @NonNull
    public <T extends E> SubscriptionBuilder<E> handle(@NonNull Consumer<EventContext<? extends E>> context) {
        this.on = context;
        return this;
    }


    @Override
    public @NonNull Builder<Subscription<E>> inherit(@NonNull Subscription<E> subscription) {
        this.type = subscription.type();
        this.lifecycle = subscription.lifecycle();
        this.execution = subscription.execution();
        this.on = subscription.on();

        return this;
    }

    @Override
    public @NonNull Subscription<E> build() {
        return new Subscription.ExpiringSubscription<>(this.type, this.lifecycle, this.execution, this.properties, this.on);
    }
}