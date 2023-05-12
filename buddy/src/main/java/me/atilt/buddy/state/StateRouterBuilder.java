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

import me.atilt.buddy.pattern.Builder;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.function.Predicate;

public class StateRouterBuilder<T extends State, U extends State> implements Builder<Router<T, U>> {

    private T attempt;
    private U fallback;
    private Predicate<T> condition;

    @NonNull
    public StateRouterBuilder<T, U> attempt(@NonNull T attempt) {
        this.attempt = attempt;
        return this;
    }

    @NonNull
    public StateRouterBuilder<T, U> fallback(@NonNull U fallback) {
        this.fallback = fallback;
        return this;
    }

    @NonNull
    public StateRouterBuilder<T, U> condition(@NonNull Predicate<T> condition) {
        this.condition = condition;
        return this;
    }

    @NonNull
    @Override
    public Builder<Router<T, U>> inherit(@NonNull Router<T, U> type) {
        this.attempt = type.attempt();
        this.fallback = type.fallback();
        this.condition = type.condition();
        return this;
    }

    @NonNull
    @Override
    public Router<T, U> build() {
        return new StateRouter<>(this.attempt, this.fallback, this.condition);
    }
}