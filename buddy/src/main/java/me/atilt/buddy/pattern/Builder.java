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

package me.atilt.buddy.pattern;

import javax.annotation.Nonnull;

/**
 * Provides building pattern access to objects as
 * well as attribute adoption from objects of the same type
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 *
 * @param <B> the type of buildable object
 */
public interface Builder<B> {

    /**
     * Adopts attributes from the handle object into
     * the builder
     *
     * @since 1.0.0
     *
     * @param type the type of buildable object
     * @return the builder
     */
    @Nonnull
    Builder<B> inherit(@Nonnull B type);

    /**
     * Builds the object
     *
     * @since 1.0.0
     *
     * @return the object to build
     */
    @Nonnull
    B build();
}