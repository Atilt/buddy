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

package me.atilt.buddy;

import cloud.commandframework.CommandManager;
import me.atilt.buddy.closeable.Closeable;
import me.atilt.buddy.event.EventManager;
import me.atilt.buddy.reloadable.Reloadable;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;

/**
 * Represents a {@link Plugin} with extended functionality related to
 * Buddy.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 */
public interface BuddyPlugin extends Plugin, Reloadable, Closeable {

    /**
     * Provides access to Cloud's {@link CommandManager} for
     * manging Bukkit's {@link org.bukkit.command.CommandExecutor} and {@link org.bukkit.command.Command}
     *
     * @since 1.0.0
     *
     * @return the command manager
     */
    @Nonnull
    CommandManager<CommandSender> commandManager();

    /**
     * Provides access to Buddy's {@link EventManager} for
     * managing Bukkit's {@link org.bukkit.event.Event} and {@link org.bukkit.event.Listener}
     *
     * @since 1.0.0
     *
     * @return the event manager
     */
    @Nonnull
    EventManager eventManager();
}