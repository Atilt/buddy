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

public abstract class DefaultState implements State {

    private boolean closed;
    private boolean started;
    private boolean updating;
    private boolean completed;

    @Override
    public boolean started() {
        return this.started;
    }

    @Override
    public boolean updating() {
        return this.updating;
    }

    @Override
    public boolean completed() {
        return this.completed;
    }

    @Override
    public void start() {
        this.started = true;
    }

    @Override
    public void complete() {
        this.updating = false;
        this.completed = true;
    }

    @Override
    public void update() {
        this.updating = true;
    }

    @Override
    public void reset() {
        this.started = false;
        this.closed = false;
        this.updating = false;
    }

    @Override
    public void close() throws Exception {
        this.closed = true;
    }

    @Override
    public boolean closed() {
        return this.closed;
    }
}