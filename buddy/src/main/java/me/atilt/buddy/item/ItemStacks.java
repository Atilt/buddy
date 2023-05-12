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

package me.atilt.buddy.item;

import me.atilt.buddy.pattern.Builder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnegative;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Preconditions;
import java.util.function.UnaryOperator;

/**
 * Utility for Bukkit's {@link ItemStack}
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 */
public final class ItemStacks {

    public static final ItemFlag[] ITEM_FLAGS = ItemFlag.values();

    /**
     * Creates a new builder for Bukkit's {@link ItemStack}
     * @return the builder {@link ItemStackBuilder} for the {@link ItemStack}
     */
    @NonNull
    public static ItemStackBuilder newBuilder() {
        return new ItemStackBuilder();
    }

    @NonNull
    public static List<Component> lore(@NonNull ItemMeta itemMeta) {
        Preconditions.checkNotNull(itemMeta, "itemMeta");
        List<Component> lore = itemMeta.lore();
        return lore != null ? lore : new ArrayList<>();
    }

    public static final class ItemStackBuilder implements Builder<ItemStack> {

        private final ItemStack itemStack;

        private ItemStackBuilder() {
            this.itemStack = new ItemStack(Material.STONE, 1);
        }

        @NonNull
        public ItemStackBuilder type(@NonNull Material type) {
            Preconditions.checkNotNull(type, "type");
            this.itemStack.setType(type);
            return this;
        }

        @NonNull
        public ItemStackBuilder amount(@Nonnegative int amount) {
            Preconditions.checkArgument(amount > 0, "amount must be positive: %s", amount);
            this.itemStack.setAmount(amount);
            return this;
        }

        @NonNull
        public <T extends ItemMeta> ItemStackBuilder meta(Class<T> metaType, @NonNull UnaryOperator<T> operator) {
            Preconditions.checkNotNull(metaType, "metaType");
            Preconditions.checkNotNull(operator, "function");

            ItemMeta itemMeta = this.itemStack.getItemMeta();
            if (!metaType.isInstance(itemMeta)) {
                throw new IllegalStateException("meta mismatch. expected: " + metaType.getName() + ", found: " + (itemMeta == null ? "null" : itemMeta.getClass().getName()));
            }
            this.itemStack.setItemMeta(operator.apply(metaType.cast(itemMeta)));
            return this;
        }

        @NonNull
        public ItemStackBuilder meta(@NonNull UnaryOperator<ItemMeta> function) {
            Preconditions.checkNotNull(function, "function");
            return meta(ItemMeta.class, function);
        }

        @NonNull
        public ItemStackBuilder damage(int amount) {
            return meta(Damageable.class, damageable -> {
                damageable.setDamage(amount);
                return damageable;
            });
        }

        @NonNull
        public ItemStackBuilder displayName(@NonNull String displayName) {
            Preconditions.checkNotNull(displayName, "displayName");
            return meta(itemMeta -> {
                itemMeta.setDisplayName(displayName);
                return itemMeta;
            });
        }

        @NonNull
        public ItemStackBuilder lore(@NonNull List<String> lore) {
            Preconditions.checkNotNull(lore, "lore");
            return meta(itemMeta -> {
                itemMeta.setLore(lore);
                return itemMeta;
            });
        }

        @NonNull
        public ItemStackBuilder enchant(@NonNull Enchantment enchantment, @Nonnegative int level, boolean bypass) {
            Preconditions.checkNotNull(enchantment, "enchantment");
            Preconditions.checkArgument(level > 0, "level out of bounds: %s", level);
            return meta(itemMeta -> {
                itemMeta.addEnchant(enchantment, level, bypass);
                return itemMeta;
            });
        }

        @NonNull
        public ItemStackBuilder flag(@NonNull ItemFlag... flags) {
            Preconditions.checkNotNull(flags, "flags");
            return meta(itemMeta -> {
                itemMeta.addItemFlags(flags);
               return itemMeta;
            });
        }

        @NonNull
        @Override
        public ItemStackBuilder inherit(@NonNull ItemStack itemStack) {
            this.itemStack.setType(itemStack.getType());
            this.itemStack.setItemMeta(itemStack.getItemMeta().clone());
            this.itemStack.setData(itemStack.getData().clone());
            this.itemStack.setDurability(itemStack.getDurability());
            this.itemStack.setAmount(itemStack.getAmount());
            return this;
        }

        @NonNull
        @Override
        public ItemStack build() {
            return this.itemStack;
        }
    }

    private ItemStacks() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}