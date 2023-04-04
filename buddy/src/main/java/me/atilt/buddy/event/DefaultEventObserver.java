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

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import me.atilt.buddy.event.builder.ObservableEventBuilder;
import me.atilt.buddy.event.lifecycle.IndefiniteLifecycle;
import me.atilt.buddy.event.lifecycle.stage.ExpirationPolicy;
import me.atilt.buddy.event.lifecycle.Lifecycle;
import me.atilt.buddy.function.Consumers;
import me.atilt.buddy.pattern.Builder;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

class DefaultEventObserver<E extends Event> implements ObservableEvent<E>, EventExecutor, Listener {

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final LoadingCache<Class<? extends Event>, MethodHandle> HANDLE_CACHE = Caffeine
            .newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .initialCapacity(20)
            .build(new CacheLoader<Class<? extends Event>, MethodHandle>() {
                @Override
                public @Nullable MethodHandle load(Class<? extends Event> clazz) throws NoSuchMethodException, IllegalAccessException {
                    return LOOKUP.findStatic(clazz, "getHandlerList", MethodType.methodType(HandlerList.class));
                }
            });

    private final Lifecycle<E> lifecycle;
    private final Class<E> eventType;
    private final EventPriority priority;
    private final List<Predicate<E>> only = new ArrayList<>();
    private final Consumer<E> on;

    private DefaultEventObserver(@Nonnull Lifecycle<E> lifecycle, @Nonnull Class<E> eventType, @Nonnull EventPriority priority, @Nonnull List<Predicate<E>> only, @Nonnull Consumer<E> on) {
        Objects.requireNonNull(lifecycle, "lifecycle");
        Objects.requireNonNull(eventType, "eventType");
        Objects.requireNonNull(only, "only");
        Objects.requireNonNull(on, "on");
        this.lifecycle = lifecycle;
        this.eventType = eventType;
        this.priority = priority;
        this.only.addAll(only);
        this.on = on;
    }

    @Nonnull
    protected static <E extends Event> ObservableEventBuilder<E> newBuilder(@Nonnull Plugin plugin, @Nonnull Class<E> eventType) {
        Objects.requireNonNull(plugin, "plugin");
        Objects.requireNonNull(eventType, "eventType");
        return new DefaultObservableEventBuilder<>(plugin, eventType);
    }

    private void unregisterListener(@Nonnull Class<? extends Event> eventClass, @Nonnull Listener listener) throws Throwable {
        MethodHandle methodHandle = HANDLE_CACHE.get(eventClass);

        HandlerList handlerList = (HandlerList) methodHandle.invokeExact();
        handlerList.unregister(listener);
    }

    private void unregisterListener(@Nonnull Event event, @Nonnull Listener listener) {
        event.getHandlers().unregister(listener);
    }

    @Override
    public void close() {
        this.lifecycle.close();

        try {
            unregisterListener(this.eventType, this);
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public boolean closed() {
        return this.lifecycle.closed();
    }

    @Nonnull
    @Override
    public Lifecycle<E> lifecycle() {
        return this.lifecycle;
    }

    @Nonnull
    @Override
    public Class<E> eventType() {
        return this.eventType;
    }

    @Nonnull
    @Override
    public EventPriority priority() {
        return this.priority;
    }

    @Nonnull
    @Override
    public List<Predicate<E>> only() {
        return Collections.unmodifiableList(this.only);
    }

    @Nonnull
    @Override
    public Consumer<E> on() {
        return this.on;
    }

    @Override
    public void execute(Listener listener, Event event) {
        if (!this.eventType.isInstance(event)) {
            return;
        }
        if (closed()) {
            unregisterListener(event, listener);
            return;
        }
        E found = this.eventType.cast(event);

        ExpirationPolicy expirationPolicy = this.lifecycle.expirationPolicy();
        if (expirationPolicy.hard()) {
            if (this.lifecycle.test(found)) {
                this.lifecycle.close();
                unregisterListener(event, listener);
                return;
            }
        }

        for (Predicate<E> predicate : this.only) {
            if (!predicate.test(found)) {
                return;
            }
        }

        if (expirationPolicy.soft()) {
            if (this.lifecycle.test(found)) {
                this.lifecycle.close();
                unregisterListener(event, listener);
                return;
            }
        }

        this.on.accept(found);
    }

    private static final class DefaultObservableEventBuilder<E extends Event> implements ObservableEventBuilder<E> {

        private final Plugin plugin;

        private Lifecycle<E> lifecycle;
        private Class<E> eventType;
        private EventPriority priority;
        private List<Predicate<E>> only = new ArrayList<>();
        private Consumer<E> on;

        private DefaultObservableEventBuilder(@Nonnull Plugin plugin, @Nonnull Class<E> eventType) {
            Objects.requireNonNull(plugin, "plugin");
            Objects.requireNonNull(eventType, "eventType");
            this.plugin = plugin;
            this.lifecycle = new IndefiniteLifecycle<>(ExpirationPolicy.HARD);
            this.eventType = eventType;
            this.priority = EventPriority.NORMAL;
            this.on = Consumers.empty();
        }

        @Nonnull
        @Override
        public ObservableEventBuilder<E> lifecycle(@Nonnull Lifecycle<E> lifecycle) {
            Objects.requireNonNull(lifecycle, "lifecycle");
            this.lifecycle = lifecycle;
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
            Objects.requireNonNull(on, "on");
            this.on = on;
            return this;
        }

        @Nonnull
        @Override
        public Builder<ObservableEvent<E>> inherit(@Nonnull ObservableEvent<E> type) {
            Objects.requireNonNull(type, "type");
            this.lifecycle = type.lifecycle();
            this.eventType = type.eventType();
            this.priority = type.priority();
            this.only = type.only();
            this.on = type.on();
            return this;
        }

        @Nonnull
        @Override
        public ObservableEvent<E> build() {
            final DefaultEventObserver<E> defaultEventObserver = new DefaultEventObserver<>(this.lifecycle, this.eventType, this.priority, this.only, this.on);
            Bukkit.getPluginManager().registerEvent(this.eventType, defaultEventObserver, this.priority, defaultEventObserver, this.plugin, false);
            return defaultEventObserver;
        }
    }
}