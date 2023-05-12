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

package me.atilt.buddy.event.bukkit;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import me.atilt.buddy.event.EventContext;
import me.atilt.buddy.event.Execution;
import me.atilt.buddy.event.Lifecycle;
import me.atilt.buddy.event.Property;
import me.atilt.buddy.event.Subscriber;
import me.atilt.buddy.event.Subscription;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BukkitEventBus implements Subscriber<Event>, EventExecutor, Listener  {

/*    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final MethodType METHOD_TYPE = MethodType.methodType(HandlerList.class);
    private static final LoadingCache<Class<? extends Event>, MethodHandle> HANDLE_CACHE = Caffeine
            .newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .initialCapacity(20)
            .build(new CacheLoader<Class<? extends Event>, MethodHandle>() {
                @Override
                public @Nullable MethodHandle load(Class<? extends Event> clazz) throws NoSuchMethodException, IllegalAccessException {
                    return LOOKUP.findStatic(clazz, "getHandlerList", METHOD_TYPE);
                }
            });*/

    private final Reference2ReferenceMap<Class<? extends Event>, List<Subscription<? super Event>>> subscriptions = new Reference2ReferenceOpenHashMap<>(20, 0.95f);

    private final Plugin plugin;

    public BukkitEventBus(@NonNull Plugin plugin) {
        super();
        this.plugin = plugin;
    }

/*    private void unregisterListener(@NonNull Class<? extends Event> eventClass, @NonNull Listener listener) throws Throwable {
        MethodHandle methodHandle = HANDLE_CACHE.get(eventClass);

        HandlerList handlerList = (HandlerList) methodHandle.invokeExact();
        handlerList.unregister(listener);
    }

    private void unregisterListener(@NonNull Event event, @NonNull Listener listener) {
        event.getHandlers().unregister(listener);
    }*/

    @Override
    public void subscribe(@NonNull Subscription<? extends Event> subscription) {
        List<Subscription<? super Event>> subscriptions = this.subscriptions.computeIfAbsent(subscription.type(), new Function<Class<? extends Event>, List<Subscription<? super Event>>>() {
            @Override
            public List<Subscription<? super Event>> apply(Class<? extends Event> aClass) {
                BukkitEventBus.this.plugin.getServer().getPluginManager().registerEvent(subscription.type(), BukkitEventBus.this, EventPriority.NORMAL, BukkitEventBus.this, BukkitEventBus.this.plugin, false);
                return new ArrayList<>();
            }
        });
        if (subscriptions.isEmpty()) {
            System.out.println("1: " + (subscriptions.isEmpty() ? "Empty" : "== INTERMEDIATE"));
            subscriptions.add((Subscription<? super Event>) subscription);
        } else {
            int index = Collections.binarySearch(subscriptions, subscription);
            if (index < 0) {
                subscriptions.add(-index - 1, (Subscription<? super Event>) subscription);
                System.out.println("2");
            } else {
                subscriptions.add(index + 1, (Subscription<? super Event>) subscription);
                System.out.println("3");
            }
        }
        System.out.println(subscriptions.stream().map(s -> s.getClass().getSimpleName() + " " + s.execution().getAsInt()).collect(Collectors.joining(", ")));
    }

    @Override
    public <T extends Event> Predicate<T> filter() {
        return null;
    }

    @Override
    public <T extends Event> void publish(@NonNull T event) {
        List<Subscription<? super Event>> subscriptions = this.subscriptions.get(event.getClass());
        if (subscriptions != null) {
            Map<String, Object> data = new HashMap<>();
            subscriptions.removeIf(subscription -> {
                List<? extends Property<? super Event>> properties = subscription.properties();
                for (Property<? super Event> property : properties) {
                    if (property.undefined()) {
                        Object evaluate = property.apply(event);
                        data.put(property.key(), evaluate);
                    }
                }
                Lifecycle lifecycle = subscription.lifecycle();
                lifecycle.run();
                if (lifecycle.dead()) {
                    return true;
                }
                Consumer<EventContext<?>> on = (Consumer<EventContext<?>>) subscription.on();
                on.accept(new EventContext.DefaultEventContext<>(event, data));
                return false;
            });
            if (subscriptions.isEmpty()) {
                event.getHandlers().unregister(this);
                this.subscriptions.remove(event.getClass());
            }
        }
    }

    /*
        @Override
    public void publish(@NonNull Event event) {
        List<Subscription<? extends Event>> subscriptions = this.subscriptions.get(event.getClass());
        if (subscriptions != null) {
            Map<String, Object> data = new HashMap<>();
            subscriptions.removeIf(subscription -> {
                for (Property<? extends Event, Object> property : subscription.properties()) {
                    Object evaluate = property.evaluate(event);
                    data.put(property.key(), evaluate);
                }
                Lifecycle lifecycle = subscription.lifecycle();
                lifecycle.run();
                if (lifecycle.dead()) {
                    return true;
                }
                Consumer<EventContext<? extends Event>> on = (Consumer<EventContext<? extends Event>>) subscription.on();
                on.accept(new EventContext.DefaultEventContext<>(event, data));
                return false;
            });
            if (subscriptions.isEmpty()) {
                event.getHandlers().unregister(this);
                this.subscriptions.remove(event.getClass());
            }
        }
    }*/

    @Override
    public void execute(Listener listener, Event event) {
        publish(event);
    }
}