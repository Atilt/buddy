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

package me.atilt.buddy.state;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public class CursoredStateFlow extends CursoredState {

    public CursoredStateFlow(@Nonnull Collection<State> states) {
        super(states);
    }

    @Nonnull
    @Override
    public State cursor(int cursor) {
        validate(Status.STARTED, true);
        validate(Status.CLOSED, false);

        State cursored = super.cursor(cursor);

        for (int remaining = cursor + 1; remaining < size(); remaining++) {
            State state = get(remaining);
            state.reset();
        }
        return cursored;
    }

    @Override
    public void update() {
        super.update();
        if (completed()) {
            return;
        }
        State current = state();
        if (current.completed()) {
            State focus = cursor(cursor() + 1);
            focus.start();
        }
    }
}