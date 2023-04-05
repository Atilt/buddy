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

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
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
     * @return the builder {@link Builder} for the {@link ItemStack}
     */
    @Nonnull
    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder implements me.atilt.buddy.pattern.Builder<ItemStack> {

        private final ItemStack itemStack;

        private Builder() {
            this.itemStack = new ItemStack(Material.STONE, 1);
        }

        @Nonnull
        public Builder type(@Nonnull Material type) {
            Preconditions.checkNotNull(type, "type");
            this.itemStack.setType(type);
            return this;
        }

        @Nonnull
        public Builder amount(@Nonnegative int amount) {
            Preconditions.checkArgument(amount > 0, "amount must be positive: %s", amount);
            this.itemStack.setAmount(amount);
            return this;
        }

        @Nonnull
        public <T extends ItemMeta> Builder meta(Class<T> metaType, @Nonnull UnaryOperator<T> operator) {
            Preconditions.checkNotNull(metaType, "metaType");
            Preconditions.checkNotNull(operator, "function");

            ItemMeta itemMeta = this.itemStack.getItemMeta();
            if (!metaType.isInstance(itemMeta)) {
                throw new IllegalStateException("meta mismatch");
            }
            this.itemStack.setItemMeta(operator.apply(metaType.cast(itemMeta)));
            return this;
        }

        @Nonnull
        public Builder meta(@Nonnull UnaryOperator<ItemMeta> function) {
            Preconditions.checkNotNull(function, "function");
            return meta(ItemMeta.class, function);
        }

        @Nonnull
        public Builder damage(int amount) {
            return meta(Damageable.class, damageable -> {
                damageable.setDamage(amount);
                return damageable;
            });
        }

        @Nonnull
        public Builder displayName(@Nonnull String displayName) {
            Preconditions.checkNotNull(displayName, "displayName");
            return meta(itemMeta -> {
                itemMeta.setDisplayName(displayName);
                return itemMeta;
            });
        }

        @Nonnull
        public Builder lore(@Nonnull List<String> lore) {
            Preconditions.checkNotNull(lore, "lore");
            return meta(itemMeta -> {
                itemMeta.setLore(lore);
                return itemMeta;
            });
        }

        @Nonnull
        public Builder enchant(@Nonnull Enchantment enchantment, @Nonnegative int level, boolean bypass) {
            Preconditions.checkNotNull(enchantment, "enchantment");
            Preconditions.checkArgument(level > 0, "level out of bounds: %s", level);
            return meta(itemMeta -> {
                itemMeta.addEnchant(enchantment, level, bypass);
                return itemMeta;
            });
        }

        @Nonnull
        public Builder flag(@Nonnull ItemFlag... flags) {
            Preconditions.checkNotNull(flags, "flags");
            return meta(itemMeta -> {
                itemMeta.addItemFlags(flags);
               return itemMeta;
            });
        }

        @Nonnull
        @Override
        public Builder inherit(@Nonnull ItemStack itemStack) {
            this.itemStack.setType(itemStack.getType());
            this.itemStack.setItemMeta(itemStack.getItemMeta().clone());
            this.itemStack.setData(itemStack.getData().clone());
            this.itemStack.setDurability(itemStack.getDurability());
            this.itemStack.setAmount(itemStack.getAmount());
            return this;
        }

        @Nonnull
        @Override
        public ItemStack build() {
            return this.itemStack;
        }
    }

    private ItemStacks() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}