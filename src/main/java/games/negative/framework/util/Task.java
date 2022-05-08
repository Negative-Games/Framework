/*
 * MIT License
 *
 * Copyright (c) 2022 Negative
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
 *
 */

package games.negative.framework.util;

import games.negative.framework.BasePlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class Task {

    /**
     * Run a task asynchronously
     *
     * @param plugin   {@link JavaPlugin} instance
     * @param runnable {@link Runnable} instance
     * @return {@link BukkitTask} instance
     */
    public BukkitTask async(@NotNull JavaPlugin plugin, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    /**
     * Run a task asynchronously
     * @param runnable {@link Runnable} instance
     * @return {@link BukkitTask} instance
     */
    public BukkitTask async(@NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(BasePlugin.getInst(), runnable);
    }

    /**
     * Run a delayed task asynchronously
     *
     * @param plugin   {@link JavaPlugin} instance
     * @param delay    Delay in ticks
     * @param runnable {@link Runnable} instance
     * @return {@link BukkitTask} instance
     */
    public BukkitTask asyncDelayed(@NotNull JavaPlugin plugin, final long delay, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    /**
     * Run a delayed task asynchronously
     * @param delay   Delay in ticks
     * @param runnable {@link Runnable} instance
     * @return {@link BukkitTask} instance
     */
    public BukkitTask asyncDelayed(final long delay, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(BasePlugin.getInst(), runnable, delay);
    }

    /**
     * Run a repeating task asynchronously
     *
     * @param plugin   {@link JavaPlugin} instance
     * @param delay    Delay in ticks
     * @param interval Interval the task gets repeated in ticks
     * @param runnable {@link Runnable} instance
     * @return {@link BukkitTask} instance
     */
    public BukkitTask asyncRepeating(@NotNull JavaPlugin plugin, final long delay, final long interval, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, interval);
    }

    /**
     * Run a repeating task asynchronously
     * @param delay   Delay in ticks
     * @param interval Interval the task gets repeated in ticks
     * @param runnable {@link Runnable} instance
     * @return {@link BukkitTask} instance
     */
    public BukkitTask asyncRepeating(final long delay, final long interval, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(BasePlugin.getInst(), runnable, delay, interval);
    }

    /**
     * Run a task
     *
     * @param plugin   {@link JavaPlugin} instance
     * @param runnable {@link Runnable} instance
     * @return {@link BukkitTask} instance
     */
    public BukkitTask task(@NotNull JavaPlugin plugin, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTask(plugin, runnable);
    }

    /**
     * Run a task
     * @param runnable {@link Runnable} instance
     * @return {@link BukkitTask} instance
     */
    public BukkitTask task(@NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTask(BasePlugin.getInst(), runnable);
    }

    /**
     * Run a delayed task
     *
     * @param plugin   {@link JavaPlugin} instance
     * @param delay    Delay in ticks
     * @param runnable {@link Runnable} instance
     * @return {@link BukkitTask} instance
     */
    public BukkitTask taskDelayed(@NotNull JavaPlugin plugin, final long delay, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }

    /**
     * Run a delayed task
     * @param delay   Delay in ticks
     * @param runnable {@link Runnable} instance
     * @return {@link BukkitTask} instance
     */
    public BukkitTask taskDelayed(final long delay, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskLater(BasePlugin.getInst(), runnable, delay);
    }

    /**
     * Run a repeating task
     *
     * @param plugin   {@link JavaPlugin} instance
     * @param delay    Delay in ticks
     * @param interval Interval the task gets repeated in ticks
     * @param runnable {@link Runnable} instance
     * @return {@link BukkitTask} instance
     */
    public BukkitTask taskRepeating(@NotNull JavaPlugin plugin, final long delay, final long interval, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, interval);
    }

    /**
     * Run a repeating task
     * @param delay   Delay in ticks
     * @param interval Interval the task gets repeated in ticks
     * @param runnable {@link Runnable} instance
     * @return {@link BukkitTask} instance
     */
    public BukkitTask taskRepeating(final long delay, final long interval, @NotNull Runnable runnable) {
        return Bukkit.getScheduler().runTaskTimer(BasePlugin.getInst(), runnable, delay, interval);
    }

}
