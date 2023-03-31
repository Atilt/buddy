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

package me.atilt.buddy.math;

/**
 * Improved version of Riven's sine & cosine algorithm.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Riven, Nate, Atilt, & others
 */
public final class FastMath {

    private static final int BIG_ENOUGH_INT   = 16 * 1024;
    private static final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;
    private static final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5;
    private static final int SIN_BITS, SIN_MASK, SIN_COUNT;
    private static final float radFull, radToIndex;
    private static final float degFull, degToIndex;
    private static final float[] sin, cos;

    static {
        SIN_BITS = 12;
        SIN_MASK = ~(-1 << SIN_BITS);
        SIN_COUNT = SIN_MASK + 1;

        radFull = (float) (Math.PI * 2.0);
        degFull = (float) (360.0);
        radToIndex = SIN_COUNT / radFull;
        degToIndex = SIN_COUNT / degFull;

        sin = new float[SIN_COUNT];
        cos = new float[SIN_COUNT];

        for (int i = 0; i < SIN_COUNT; i++) {
            sin[i] = (float) Math.sin((i + 0.5f) / SIN_COUNT * radFull);
            cos[i] = (float) Math.cos((i + 0.5f) / SIN_COUNT * radFull);
        }

        // Four cardinal directions (credits: Nate)
        for (int i = 0; i < 360; i += 90) {
            sin[(int) (i * degToIndex) & SIN_MASK] = (float) Math.sin(i * Math.PI / 180.0);
            cos[(int) (i * degToIndex) & SIN_MASK] = (float) Math.cos(i * Math.PI / 180.0);
        }
    }

    public static float sin(float rad) {
        return sin[(int) (rad * radToIndex) & SIN_MASK];
    }

    public static float cos(float rad) {
        return cos[(int) (rad * radToIndex) & SIN_MASK];
    }
    public static float sinDeg(float deg) {
        return sin[((int) (deg * degToIndex)) & SIN_MASK];
    }

    public static float cosDeg(float deg) {
        return cos[((int) (deg * degToIndex)) & SIN_MASK];
    }

    public static int floor(float x) {
        return (int) (x + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT;
    }

    public static int round(float x) {
        return (int) (x + BIG_ENOUGH_ROUND) - BIG_ENOUGH_INT;
    }

    public static int ceil(float x) {
        return BIG_ENOUGH_INT - (int) (BIG_ENOUGH_FLOOR - x);
    }

    public static int floor(double x) {
        return (int) (x + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT;
    }

    public static int round(double x) {
        return (int) (x + BIG_ENOUGH_ROUND) - BIG_ENOUGH_INT;
    }

    public static int ceil(double x) {
        return BIG_ENOUGH_INT - (int) (BIG_ENOUGH_FLOOR - x);
    }

    private FastMath() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}