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

package me.atilt.buddy.state.trigger;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Objects;

class StateTriggerKey implements TriggerKey {

    private final String domain;

    StateTriggerKey(String domain) {
        this.domain = domain;
    }

    @NonNull
    @Override
    public String domain() {
        return this.domain;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof StateTriggerKey)) return false;
        StateTriggerKey that = (StateTriggerKey) other;
        return this.domain.equalsIgnoreCase(that.domain());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.domain);
    }

    @Override
    public String toString() {
        return "StateTriggerKey{" +
                "domain='" + this.domain + '\'' +
                '}';
    }
}