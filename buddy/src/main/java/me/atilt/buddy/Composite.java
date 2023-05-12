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

import me.atilt.buddy.closeable.Closeable;

import javax.annotation.Nonnegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Collection;

/**
 * Represents a composite of objects. A composite should
 * extend its compositing type. Capable of latching or binding
 * items to itself.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 *
 * @param <C> the type
 */
public interface Composite<C> extends Iterable<C>, Closeable {

    /**
     * The size of the composite represented by
     * the total number of object latches
     *
     * @since 1.0.0
     *
     * @return the size the composite
     */
    @Nonnegative
    int size();

    /**
     * Latches an object onto the composite.
     *
     * @param composites the object
     * @return whether the latching was successful or not.
     */
    boolean latch(@NonNull C... composites);
}