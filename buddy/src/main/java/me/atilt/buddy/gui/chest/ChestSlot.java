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

package me.atilt.buddy.gui.chest;

import me.atilt.buddy.function.Functions;
import me.atilt.buddy.function.Suppliers;
import me.atilt.buddy.gui.Click;
import me.atilt.buddy.gui.Slot;
import me.atilt.buddy.gui.chest.item.ChestItem;
import me.atilt.buddy.gui.state.DefaultStateHolder;
import me.atilt.buddy.gui.state.State;
import me.atilt.buddy.gui.state.StateHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.annotation.Nonnull;
import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ChestSlot implements Slot<InventoryClickEvent> {

    private final StateHolder stateHolder;
    private final Click<InventoryClickEvent> click;
    private final Function<Player, ChestItem> item;

    private ChestSlot(@Nonnull Click<InventoryClickEvent> click, @Nonnull Function<Player, ChestItem> item) {
        this.stateHolder = new DefaultStateHolder(Collections.emptyList(), 0);
        this.click = click;
        this.item = item;
    }

    @Nonnull
    public static Builder newBuilder() {
        return new Builder();
    }

    @Nonnull
    @Override
    public StateHolder stateHolder() {
        return this.stateHolder;
    }

    @Nonnull
    @Override
    public Click<InventoryClickEvent> click() {
        return this.click;
    }

    @Nonnull
    public Function<Player, ChestItem> item() {
        return this.item;
    }

    public static class Builder implements me.atilt.buddy.pattern.Builder<ChestSlot> {

        private Click<InventoryClickEvent> click;
        private Function<Player, ChestItem> item;

        private Builder() {
            this.click = InventoryClick.empty();
            this.item = Functions.empty();
        }

        @Nonnull
        public Builder click(@Nonnull Click<InventoryClickEvent> click) {
            Preconditions.checkNotNull(click, "click");
            this.click = click;
            return this;
        }

        @Nonnull
        public Builder click(@Nonnull BiConsumer<Player, InventoryClickEvent> click) {
            Preconditions.checkNotNull(click, "click");
            return click(new InventoryClick(click));
        }

        @Nonnull
        public Builder item(@Nonnull Function<Player, ChestItem> item) {
            Preconditions.checkNotNull(item, "item");
            this.item = item;
            return this;
        }

        @Nonnull
        public Builder item(@Nonnull Supplier<ChestItem> item) {
            Preconditions.checkNotNull(item, "item");
            this.item = Suppliers.functionalized(item);
            return this;
        }

        @Nonnull
        @Override
        public Builder inherit(@Nonnull ChestSlot type) {
            this.click = type.click();
            this.item = type.item();
            return this;
        }

        @Nonnull
        @Override
        public ChestSlot build() {
            return new ChestSlot(this.click, this.item);
        }
    }
}