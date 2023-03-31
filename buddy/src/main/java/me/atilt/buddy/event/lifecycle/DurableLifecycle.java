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

package me.atilt.buddy.event.lifecycle;

import me.atilt.buddy.supplier.Lazy;
import org.bukkit.event.Event;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;

public class DurableLifecycle implements Lifecycle {

    private final Lazy<Instant> lazy;
    private final Duration duration;
    private boolean closed;

    public DurableLifecycle(@Nonnull Lazy<Instant> lazy, @Nonnull Duration duration) {
        this.lazy = lazy;
        this.duration = duration;
    }

    @Override
    public boolean test(Event event) {
        return closed();
    }

    @Override
    public void close() {
        this.closed = true;
    }

    @Override
    public boolean closed() {
        return this.closed || Instant.now().isAfter(this.lazy.get().plus(this.duration));
    }
}