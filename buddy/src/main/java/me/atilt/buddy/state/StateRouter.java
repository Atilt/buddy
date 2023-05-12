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

import me.atilt.buddy.supplier.Lazy;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class StateRouter<T extends State, U extends State> implements Router<T, U> {

    private final T attempt;
    private final U fallback;
    private final Predicate<T> condition;
    private final Supplier<Boolean> success;

    public StateRouter(T attempt, U fallback, Predicate<T> condition) {
        this.attempt = attempt;
        this.fallback = fallback;
        this.condition = condition;
        this.success = () -> this.condition.test(this.attempt);
    }

    @NonNull
    @Override
    public T attempt() {
        return this.attempt;
    }

    @NonNull
    @Override
    public U fallback() {
        return this.fallback;
    }

    @NonNull
    @Override
    public Predicate<T> condition() {
        return this.condition;
    }

    @Override
    public boolean enter() {
        Boolean value = this.success.get();
        if (value) {
            return this.attempt.enter();
        } else {
            return this.fallback.enter();
        }
    }

    @Override
    public boolean update() {
        Boolean value = this.success.get();
        if (value) {
            return this.attempt.update();
        } else {
            return this.fallback.update();
        }
    }

    @Override
    public boolean exit() {
        Boolean value = this.success.get();
        if (value) {
            return this.attempt.exit();
        } else {
            return this.fallback.exit();
        }
    }

    @Override
    public <V extends State> V self() {
        Boolean value = this.success.get();
        if (value) {
            return (V) this.attempt;
        } else {
            return (V) this.fallback;
        }
    }
}