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

import me.atilt.buddy.supplier.Lazy;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Duration;
import java.time.Instant;
import java.util.function.BooleanSupplier;

public interface Lifecycle extends Runnable {

    static Lifecycle indefinite() {
        return () -> false;
    }

    @NonNull
    static Lifecycle conditional(@NonNull BooleanSupplier condition) {
        return new Conditional(condition);
    }

    @NonNull
    static Lifecycle counted(@NonNegative int ceiling) {
        return new Counted(ceiling);
    }

    @NonNull
    static Lifecycle timed(@NonNull Duration duration) {
        return new Timed(duration);
    }

    @Override
    default void run() {}

    boolean dead();

    final class Conditional implements Lifecycle {

        private final BooleanSupplier condition;

        public Conditional(@NonNull BooleanSupplier condition) {
            this.condition = condition;
        }

        @Override
        public boolean dead() {
            return this.condition.getAsBoolean();
        }
    }

    final class Counted implements Lifecycle {

        private int count;
        private final int ceiling;

        public Counted(@NonNegative int ceiling) {
            this.ceiling = ceiling;
        }

        @Override
        public void run() {
            this.count++;
        }

        @Override
        public boolean dead() {
            return this.count > this.ceiling;
        }
    }

    final class Timed implements Lifecycle {

        private Instant start;
        private final Duration duration;

        public Timed(@NonNull Duration duration) {
            this.duration = duration;
        }

        @Override
        public void run() {
            if (this.start == null) {
                this.start = Instant.now();
            }
        }

        @Override
        public boolean dead() {
            if (this.start == null) {
                return false;
            }
            Instant end = this.start.plus(this.duration);
            return Instant.now().compareTo(end) >= 0;
        }
    }
}