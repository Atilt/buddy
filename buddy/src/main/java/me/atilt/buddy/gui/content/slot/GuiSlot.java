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

package me.atilt.buddy.gui.content.slot;

import me.atilt.buddy.pattern.Builder;
import me.atilt.buddy.state.FiniteStateMachine;
import me.atilt.buddy.state.Transition;
import me.atilt.buddy.state.TransitionBuilder;
import me.atilt.buddy.state.TransitionRegistryBuilder;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GuiSlot extends FiniteStateMachine<SlotContent> implements Slot {

    public GuiSlot(@NonNull SlotContent source, @NonNull Function<SlotContent, TransitionRegistryBuilder<SlotContent>> transitionRegistryBuilder) {
        super(source, transitionRegistryBuilder);
    }

    public static class GuiSlotBuilder implements Builder<GuiSlot> {

        private SlotContent source;
        private final TransitionRegistryBuilder<SlotContent> transitionRegistryBuilder = new TransitionRegistryBuilder<>();

        @NonNull
        public GuiSlotBuilder source(@NonNull SlotContent source) {
            this.source = source;
            return this;
        }

        @NonNull
        public GuiSlotBuilder transition(@NonNull Transition<SlotContent> transition) {
            this.transitionRegistryBuilder.transition(transition);
            return this;
        }

        @NonNull
        @Override
        public Builder<GuiSlot> inherit(@NonNull GuiSlot type) {
            for (Transition<SlotContent> value : type.transitioner().asMap().values()) {
                this.transitionRegistryBuilder.transition(value);
            }
            return this;
        }

        @NonNull
        @Override
        public GuiSlot build() {
            return new GuiSlot(this.source, slotContent -> this.transitionRegistryBuilder);
        }
    }
}