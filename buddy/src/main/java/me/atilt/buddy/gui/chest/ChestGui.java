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

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import me.atilt.buddy.event.EventManager;
import me.atilt.buddy.event.lifecycle.SuppliedConditionalLifecycle;
import me.atilt.buddy.event.lifecycle.stage.ExpirationPolicy;
import me.atilt.buddy.gui.Gui;
import me.atilt.buddy.gui.Interaction;
import me.atilt.buddy.pattern.Builder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;

public class ChestGui implements Gui<ChestSlot> {

    private final String title;
    private final Interaction interaction;
    private final Int2ObjectMap<ChestSlot> slots;

    private final int size;
    private final Inventory inventory;

    private boolean closed;
    private boolean open;
    private Player player;

    public ChestGui(@Nonnull String title, @Nonnull Interaction interaction, @Nonnegative int size) {
        Preconditions.checkNotNull(title, "title");
        Preconditions.checkArgument((size >= 0) && (size <= 54) && (size % 9 == 0), "size out of bounds: %s" + size);
        this.title = title;
        this.interaction = interaction;
        this.slots = new Int2ObjectOpenHashMap<>(54);
        this.size = size;
        this.inventory = Bukkit.createInventory(null, 54, title);
    }

    @Nullable
    protected Player player() {
        return this.player;
    }

    protected void set(@Nonnegative int index, @Nonnull ChestSlot slot) {
        Preconditions.checkArgument((index >= 0) && (index < this.size), "index out of bounds: %s", index);
        Preconditions.checkNotNull(slot, "slot");
        this.slots.put(index, slot);
        if (this.open) {
            this.inventory.setItem(index, slot.item().apply(this.player).bukkitItem().get());
        }
    }

    protected void reset(@Nonnegative int index) {
        Preconditions.checkArgument((index >= 0) && (index < this.size), "index out of bounds: %s", index);
        if (this.open) {
            this.inventory.setItem(index, this.slots.get(index).item().apply(this.player).bukkitItem().get());
        }
    }

    protected void reset(@Nonnegative int indexStart, @Nonnegative int indexEnd) {
        Preconditions.checkArgument((indexStart >= 0) && (indexStart < this.size), "index out of bounds: %s", indexStart);
        Preconditions.checkArgument((indexEnd >= 0) && (indexEnd < this.size), "index out of bounds: %s", indexEnd);
        if (this.open) {
            int min = Math.min(indexStart, indexEnd);
            int max = Math.max(indexStart, indexEnd);

            for (int index = min; index <= max; index++) {
                this.inventory.setItem(index, this.slots.get(index).item().apply(this.player).bukkitItem().get());
            }
        }
    }


    @Override
    public void close() {
        this.closed = true;
        if (this.open) {
            this.player.closeInventory();
        }
        this.open = false;
        this.player = null;
    }

    @Override
    public boolean closed() {
        return this.closed;
    }

    @Nonnull
    @Override
    public String title() {
        return this.title;
    }

    @Nonnull
    @Override
    public Interaction interaction() {
        return this.interaction;
    }

    @Nonnull
    @Override
    public Int2ObjectMap<ChestSlot> slots() {
        return Int2ObjectMaps.unmodifiable(this.slots);
    }

    @Override
    public void open(@Nonnull Player player) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkArgument(player.isOnline(), "player not online");
        Preconditions.checkState(!this.open, "gui already opened");

        build();

        for (Int2ObjectMap.Entry<ChestSlot> entry : this.slots.int2ObjectEntrySet()) {
            int index = entry.getIntKey();
            ChestSlot slot = entry.getValue();
            this.inventory.setItem(index, slot.item().apply(player).bukkitItem().get());
        }

        this.interaction.open().accept(player);

        player.openInventory(this.inventory);

        this.open = true;
        this.closed = false;
        this.player = player;
    }

    @Override
    public void register(@Nonnull EventManager eventManager) {
        eventManager.observe(InventoryClickEvent.class)
                .priority(EventPriority.HIGHEST)
                .lifecycle(new SuppliedConditionalLifecycle<>(ExpirationPolicy.HARD, () -> this.closed))
                .only(event -> this.open && this.inventory == event.getInventory() && event.getWhoClicked().equals(this.player))
                .on(event -> {
                    event.setCancelled(true);
                    ChestSlot chestSlot = this.slots.get(event.getSlot());
                    if (chestSlot != null) {
                        chestSlot.click().on().accept((Player) event.getWhoClicked(), event);
                    }
                })
                .build();

        eventManager.observe(InventoryCloseEvent.class)
                .priority(EventPriority.HIGHEST)
                .lifecycle(new SuppliedConditionalLifecycle<>(ExpirationPolicy.HARD, () -> this.closed))
                .only(event -> event.getInventory() == this.inventory && event.getPlayer().equals(this.player))
                .on(event -> {
                    this.open = false;

                    this.interaction.close().accept((Player) event.getPlayer());
                    close();
                })
                .build();

    }

    @Nonnull
    @Override
    public Builder<Gui<ChestSlot>> inherit(@Nonnull Gui<ChestSlot> type) {
        this.slots.clear();
        this.slots.putAll(type.slots());
        return this;
    }

    @Nonnull
    @Override
    public Gui<ChestSlot> build() {
        return this;
    }
}