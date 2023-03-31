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

import me.atilt.buddy.event.builder.ObservableEventBuilder;
import me.atilt.buddy.event.lifecycle.Lifecycle;
import me.atilt.buddy.pattern.Builder;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class DefaultObservableEventBuilder<E extends Event> implements ObservableEventBuilder<E> {

    private final Plugin plugin;

    private Lifecycle lifecycle = Lifecycle.indefinite();
    private Class<E> eventType;
    private EventPriority priority = EventPriority.NORMAL;
    private List<Predicate<E>> only = new ArrayList<>();
    private Consumer<E> on;

    public DefaultObservableEventBuilder(@Nonnull Plugin plugin, @Nonnull Class<E> eventType) {
        Objects.requireNonNull(plugin, "plugin");
        Objects.requireNonNull(eventType, "eventType");
        this.plugin = plugin;
        this.eventType = eventType;
    }

    @Nonnull
    @Override
    public ObservableEventBuilder<E> lifecycle(@Nonnull Lifecycle lifecycle) {
        Objects.requireNonNull(lifecycle, "lifecycle");
        this.lifecycle = lifecycle;
        return this;
    }

    @Nonnull
    @Override
    public ObservableEventBuilder<E> eventType(@Nonnull Class<E> eventType) {
        Objects.requireNonNull(eventType, "eventType");
        this.eventType = eventType;
        return this;
    }

    @Nonnull
    @Override
    public ObservableEventBuilder<E> priority(@Nonnull EventPriority priority) {
        Objects.requireNonNull(priority, "priority");
        this.priority = priority;
        return this;
    }

    @Nonnull
    @Override
    public ObservableEventBuilder<E> only(@Nonnull Predicate<E>... only) {
        Objects.requireNonNull(only, "only");
        this.only = Arrays.asList(only);
        return this;
    }

    @Nonnull
    @Override
    public ObservableEventBuilder<E> on(@Nonnull Consumer<E> on) {
        this.on = on;
        return this;
    }

    @Nonnull
    @Override
    public Builder<ObservableEvent<E>> inherit(@Nonnull ObservableEvent<E> type) {
        return this;
    }

    @Nonnull
    @Override
    public ObservableEvent<E> build() {
        DefaultEventObserver<E> defaultEventObserver = new DefaultEventObserver<>(this.lifecycle, this.eventType, this.priority, this.only, this.on);
        defaultEventObserver.register(this.plugin);
        return defaultEventObserver;
    }
}