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

package me.atilt.buddy.color;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.Objects;

/**
 * Associates {@link Color} to Minecraft's in-game
 * block colors. These are associated specifically to colored concrete.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 */
public enum MinecraftColor {

    WHITE(new Color(204,210,211)),
    LIGHT_GRAY(new Color(124,124,114)),
    GRAY(new Color(53,56,60)),
    BLACK(new Color(8,10,15)),
    BROWN(new Color(95,58,31)),
    RED(new Color(139,32,32)),
    ORANGE(new Color(222,97,1)),
    YELLOW(new Color(238,173,21)),
    LIME(new Color(94,168,25)),
    GREEN(new Color(72,89,36)),
    CYAN(new Color(21,117,133)),
    LIGHT_BLUE(new Color(35,135,197)),
    BLUE(new Color(43,45,141)),
    PURPLE(new Color(100,32,155)),
    MAGENTA(new Color(165,46,155)),
    PINK(new Color(211,100,141));

    public static final MinecraftColor[] TYPES = values();

    private final Color color;
    private final Material[] variations = new Material[BlockType.TYPES.length];

    MinecraftColor(@Nonnull Color color) {
        this.color = color;
    }

    @Nonnull
    public Color java() {
        return this.color;
    }

    @Nonnull
    public Material get(@Nonnull BlockType blockType) {
        Objects.requireNonNull(blockType, "blockType");
        int index = blockType.ordinal();
        Material variation = this.variations[index];
        if (variation == null) {
            this.variations[index] = variation = Material.valueOf(this.name() + "_" + blockType.name());
        }
        return variation;
    }

    public enum BlockType {

        WOOL,
        CARPET,
        TERRACOTTA,
        CONCRETE,
        CONCRETE_POWDER,
        GLAZED_TERRACOTTA,
        STAINED_GLASS,
        STAINED_GLASS_PANE,
        SHULKER_BOX,
        BED,
        CANDLE,
        BANNER,
        DYE;

        public static final BlockType[] TYPES = values();
    }
}
