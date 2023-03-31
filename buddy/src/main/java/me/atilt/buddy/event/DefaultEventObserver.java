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

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.atilt.buddy.event.lifecycle.Lifecycle;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

class DefaultEventObserver<E extends Event> implements ObservableEvent<E>, EventExecutor, Listener {

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final Map<Class<? extends Event>, MethodHandle> HANDLE_CACHE = new Object2ObjectOpenHashMap<>(10);

    private final Lifecycle lifecycle;
    private final Class<E> eventType;
    private final EventPriority priority;
    private final List<Predicate<E>> only = new ArrayList<>();
    private final Consumer<E> on;

    public DefaultEventObserver(@Nonnull Lifecycle lifecycle, @Nonnull Class<E> eventType, @Nonnull EventPriority priority, @Nonnull List<Predicate<E>> only, @Nonnull Consumer<E> on) {
        this.lifecycle = lifecycle;
        this.eventType = eventType;
        this.priority = priority;
        this.only.addAll(only);
        this.on = on;
    }

    private void unregisterListener(Class<? extends Event> eventClass, Listener listener) throws Throwable {
        MethodHandle methodHandle = HANDLE_CACHE.computeIfAbsent(eventClass,
                clazz -> {
                    try {
                        return LOOKUP.findStatic(clazz, "getHandlerList", MethodType.methodType(HandlerList.class));
                    } catch (NoSuchMethodException | IllegalAccessException exception) {
                        throw new RuntimeException(exception);
                    }
                });

        HandlerList handlerList = (HandlerList) methodHandle.invokeExact();
        handlerList.unregister(listener);
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
    public Lifecycle lifecycle() {
        return this.lifecycle;
    }

    @Override
    public void execute(Listener listener, Event event) {
        if (!this.eventType.isInstance(event)) {
            return;
        }

        if (closed() || !this.lifecycle.test(event)) {
            this.lifecycle.close();
            event.getHandlers().unregister(listener);
            return;
        }

        E found = this.eventType.cast(event);
        for (Predicate<E> predicate : this.only) {
            if (!predicate.test(found)) {
                return;
            }
        }
        this.on.accept(found);
    }

    void register(@Nonnull Plugin plugin) {
        Bukkit.getPluginManager().registerEvent(this.eventType, this, this.priority, this, plugin, false);
    }
}