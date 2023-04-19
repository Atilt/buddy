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

import me.atilt.buddy.closeable.Closeable;

/**
 * Represents a state component of a
 * state machine.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 */
public interface State extends Closeable {

    /**
     * Determines if the state has been started.
     *
     * @since 1.0.0
     *
     * @return true if the state has been started
     */
    boolean started();

    /**
     * Determines if the state is currently updating.
     *
     * @since 1.0.0
     *
     * @return true if the state is currently updating.
     */
    boolean updating();

    /**
     * Determines if the state is completed.
     *
     * @since 1.0.0
     *
     * @return true if the state is completed.
     */
    boolean completed();

    /**
     * Starts the current state.
     *
     * @since 1.0.0
     */
    void start();

    /**
     * Completes the current state.
     *
     * @since 1.0.0
     */
    void complete();

    /**
     * Updates the current state.
     *
     * @since 1.0.0
     */
    void update();

    /**
     * Resets the current state to its default condition.
     *
     * @since 1.0.0
     */
    void reset();
}