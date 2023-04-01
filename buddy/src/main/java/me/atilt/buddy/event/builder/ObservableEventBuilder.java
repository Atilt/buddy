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

package me.atilt.buddy.event.builder;

import me.atilt.buddy.event.ObservableEvent;
import me.atilt.buddy.event.lifecycle.Lifecycle;
import me.atilt.buddy.pattern.Builder;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ObservableEventBuilder<E extends Event> extends Builder<ObservableEvent<E>> {

    @Nonnull
    ObservableEventBuilder<E> lifecycle(@Nonnull Lifecycle<E> lifecycle);

    @Nonnull
    ObservableEventBuilder<E> eventType(@Nonnull Class<E> eventType);

    @Nonnull
    ObservableEventBuilder<E> priority(@Nonnull EventPriority priority);

    @Nonnull
    ObservableEventBuilder<E> only(@Nonnull Predicate<E>... only);

    @Nonnull
    ObservableEventBuilder<E> on(@Nonnull Consumer<E> on);
}