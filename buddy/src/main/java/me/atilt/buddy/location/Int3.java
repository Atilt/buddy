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

public class Int3 {

    private static final int X_MASK = 0x3FFFFFF;
    private static final long X_SHIFT = 38;
    private static final int Y_MASK = 0xFFF;
    private static final long Y_SHIFT = 26;
    private static final int Y_SHIFT_UNPACK = 20;
    private static final long Z_MASK = 0x3FFFFFF;
    private static final int Z_SHIFT_UNPACK = 6;

    public static long pack(int x, int y, int z) {
        return ((long) (x & X_MASK) << X_SHIFT) | ((long) (y & Y_MASK) << Y_SHIFT) | (z & Z_MASK);
    }

    public static int[] unpack(long combined) {
        int x = (int) (combined >> X_SHIFT);
        int y = (int) ((combined >> Y_SHIFT) & Y_MASK) << Y_SHIFT_UNPACK >> Y_SHIFT_UNPACK;
        int z = (int) (combined & Z_MASK) << Z_SHIFT_UNPACK >> Z_SHIFT_UNPACK;
        y = (y << Y_SHIFT_UNPACK) >> Y_SHIFT_UNPACK;
        z = (z << Z_SHIFT_UNPACK) >> Z_SHIFT_UNPACK;
        return new int[]{x, y, z};
    }
    
    private Int3() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}