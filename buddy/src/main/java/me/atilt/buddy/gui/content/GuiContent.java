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

package me.atilt.buddy.gui.content;

import me.atilt.buddy.gui.content.slot.Slot;
import me.atilt.buddy.gui.content.slot.SlotContent;
import me.atilt.buddy.state.State;

import javax.annotation.Nonnegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Arrays;

public class GuiContent implements Content<Slot[]> {

    private final Slot[] raw;

    public GuiContent(@NonNull Slot[] raw) {
        this.raw = raw;
    }

    @Nonnegative
    public int size() {
        return this.raw.length;
    }

    @NonNull
    @Override
    public Slot[] raw() {
        return this.raw;
    }

    @Override
    public boolean enter() {
        return true;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean exit() {
        return true;
    }

    @Override
    public <T extends State> T self() {
        return (T) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiContent)) return false;
        GuiContent that = (GuiContent) o;
        return Arrays.equals(raw, that.raw);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(raw);
    }
}
