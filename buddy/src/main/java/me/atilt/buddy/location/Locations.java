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

package me.atilt.buddy.location;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import com.google.common.base.Preconditions;

/**
 * Utility for Bukkit's {@link org.bukkit.Location} and {@link World} 3D coordinates.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 */
public final class Locations {

    /**
     * Packs {@link World} coordinates into a single long key.
     *
     * @since 1.0.0
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return the packed long key
     */
    public static long key(int x, int y, int z) {
        return ((long) (x & 0x3FFFFFF) << 38) | ((long) (y & 0xFFF) << 26) | (z & 0x3FFFFFF);
    }

    /**
     * Packs a {@link Location} into a single long key.
     *
     * @since 1.0.0
     *
     * @param location the {@link World} location
     * @return the packed long key
     */
    public static long key(@Nonnull Location location) {
        Preconditions.checkNotNull(location, "location");
        return key(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Unpacks a long key into a {@link Location}.
     *
     * @since 1.0.0
     *
     * @param world the world
     * @param combined the long key
     * @return the long key's location
     */
    public static Location keyLocation(@Nonnull World world, @Nonnegative long combined) {
        Preconditions.checkArgument(combined >= 0, "invalid range: %s", combined);
        int x = keyX(combined);
        int y = keyY(combined);
        int z = keyZ(combined);
        return new Location(world, x, y, z);
    }

    /**
     * Unpacks a long key into its associated x coordinate.
     *
     * @since 1.0.0
     *
     * @param combined the long key
     * @return the x coordinate
     */
    public static int keyX(@Nonnegative long combined) {
        Preconditions.checkArgument(combined >= 0, "invalid range: %s", combined);
        return (int) (combined >> 38);
    }

    /**
     * Unpacks a long key into its associated y coordinate.
     *
     * @since 1.0.0
     *
     * @param combined the long key
     * @return the y coordinate
     */
    public static int keyY(@Nonnegative long combined) {
        Preconditions.checkArgument(combined >= 0, "invalid range: %s", combined);
        int y = (int) ((combined >> 26) & 0xFFF) << 20 >> 20;
        return (y << 20) >> 20;
    }

    /**
     * Unpacks a long key into its associated z coordinate.
     *
     * @since 1.0.0
     *
     * @param combined the long key
     * @return the z coordinate
     */
    public static int keyZ(@Nonnegative long combined) {
        Preconditions.checkArgument(combined >= 0, "invalid range: %s", combined);
        int z = (int) (combined & 0x3FFFFFF) << 6 >> 6;
        return (z << 6) >> 6;
    }

    private Locations() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}