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

package me.atilt.buddy.function;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.List;

public final class Lists {

    @NonNull
    public static <T> T previous(@NonNull List<T> list, @Nonnegative int index) {
        Preconditions.checkNotNull(list, "list");
        Preconditions.checkState(!list.isEmpty(), "empty list");
        Preconditions.checkArgument(index >= 0 && index < list.size(), "index out of range: %s", index);
        int previousIndex = (index - 1 + list.size()) % list.size();
        return list.get(previousIndex);
    }

    @NonNull
    public static <T> T next(@NonNull List<T> list, @Nonnegative int index) {
        Preconditions.checkNotNull(list, "list");
        Preconditions.checkState(!list.isEmpty(), "empty list");
        Preconditions.checkArgument(index >= 0 && index < list.size(), "index out of range: %s", index);
        int next = (index + 1) % list.size();
        return list.get(next);
    }

    private Lists() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}