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

package me.atilt.buddy.gui;

import me.atilt.buddy.gui.content.GuiContent;
import me.atilt.buddy.gui.content.slot.EmptySlot;
import me.atilt.buddy.gui.content.slot.Slot;
import me.atilt.buddy.gui.content.slot.SlotContent;
import me.atilt.buddy.state.FiniteStateMachine;
import me.atilt.buddy.state.TransitionRegistryBuilder;
import me.atilt.buddy.state.trigger.TriggerKey;
import me.atilt.buddy.supplier.Lazy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class UnifiedGui extends FiniteStateMachine<GuiContent> implements Gui {

    private static final TriggerKey DEFAULT_TRIGGER_KEY = TriggerKey.of("default");

    private final Lazy<Inventory> inventory;

    private Player viewer;

    public UnifiedGui(@NonNull String title, @Nonnegative int rows, @NonNull Supplier<GuiContent> guiContent, Function<GuiContent, TransitionRegistryBuilder<GuiContent>> transitionerBuilder) {
        super(guiContent.get(), transitionerBuilder);
        this.inventory = Lazy.of(() -> Bukkit.createInventory(null, source().size(), title));
    }

    public void update(int index) {
        Inventory inventory = this.inventory.nullable();
        if (inventory != null) {
            Slot slot = current().raw()[index];
            if (slot == null || slot instanceof EmptySlot) {
                inventory.clear(index);
            } else {
                inventory.setItem(index, slot.current().item());
            }
        }
    }

    @Override
    public boolean trigger(TriggerKey key) {
        boolean triggered = super.trigger(key);
        if (triggered) {
            Inventory inventory = this.inventory.nullable();
            if (inventory != null) {
                inventory.clear();
            }
            for (int index = 0; index < current().size(); index++) {
                update(index);
            }
        }
        return triggered;
    }

    @Override
    public void close() {
        if (this.viewer != null) {
            this.viewer.closeInventory();
        }
    }

    @Override
    public boolean closed() {
        return false;
    }

    @Nullable
    @Override
    public Player viewer() {
        return this.viewer;
    }

    @Override
    public boolean open(@NonNull Player player) {
        if (this.viewer != null) {
            return false;
        }
        construct();

        Inventory inventory = this.inventory.get();

        for (int index = 0; index < current().size(); index++) {
            Slot slot = current().raw()[index];
            if (slot == null || slot instanceof EmptySlot) {
                inventory.clear(index);
            } else {
                inventory.setItem(index, slot.current().item());
            }
        }

        this.viewer = player;
        this.viewer.openInventory(inventory);
        return true;
    }
}