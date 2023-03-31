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
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import me.atilt.buddy.event.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 * Buddy's implementation of {@link JavaPlugin} with the extended functionality provided
 * by {@link BuddyPlugin}
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Atilt
 */
public abstract class BuddyJavaPlugin extends JavaPlugin implements BuddyPlugin {

    private CommandManager<CommandSender> commandManager;
    private EventManager eventManager;

    private boolean closed;

    /**
     * Called when the plugin enables post buddy-enabled functionality.
     *
     * @since 1.0.0
     */
    protected abstract void enable();

    /**
     * Called when the plugin disables post buddy-disabled functionality.
     *
     * @since 1.0.0
     */
    protected abstract void disable();

    @Override
    public void onEnable() {
        super.onEnable();

        try {
            this.commandManager = BukkitCommandManager.createNative(this, CommandExecutionCoordinator.simpleCoordinator());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        this.eventManager = new EventManager(this);

        enable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        close();

        disable();
    }

    @Nonnull
    @Override
    public CommandManager<CommandSender> commandManager() {
        return this.commandManager;
    }

    @Nonnull
    @Override
    public EventManager eventManager() {
        return this.eventManager;
    }

    @Override
    public boolean reload() {
        return true;
    }

    @Override
    public void close() {
        this.closed = true;
    }

    @Override
    public boolean closed() {
        return this.closed || Bukkit.getPluginManager().isPluginEnabled(this);
    }
}