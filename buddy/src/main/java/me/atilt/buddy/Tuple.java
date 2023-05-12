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

package me.atilt.buddy;

import org.checkerframework.checker.nullness.qual.NonNull;
import javax.annotation.Nullable;
import java.util.Objects;

public class Tuple<T, U> {
    private final T first;
    private final U second;

    private Tuple(@Nullable T first, @Nullable U second) {
        this.first = first;
        this.second = second;
    }

    @NonNull
    public static <T, U> Tuple<T, U> of(@Nullable T first, @Nullable U second) {
        return new Tuple<>(first, second);
    }

    @Nullable
    public T first() {
        return this.first;
    }

    @Nullable
    public U second() {
        return this.second;
    }

    @NonNull
    public Tuple<U, T> reverse() {
        return of(this.second, this.first);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(this.first, tuple.first()) && Objects.equals(this.second, tuple.second());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "item1=" + this.first +
                ", item2=" + this.second +
                '}';
    }
}