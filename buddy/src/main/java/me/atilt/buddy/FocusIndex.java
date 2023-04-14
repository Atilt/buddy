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

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

/**
 * Represents an index which can be tracked
 * and focused on. Used in {@link me.atilt.buddy.util.FocusList}
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 *
 * @param <F> the type
 */
public interface FocusIndex<F> {

    /**
     * The current index being focused
     *
     * @since 1.0.0
     *
     * @return the index
     */
    @Nonnegative
    int focused();

    /**
     * The index to switch focus towards.
     *
     * @since 1.0.0
     *
     * @param index the index
     * @return the value at the specified index
     */
    @Nullable
    F focus(@Nonnegative int index);
}