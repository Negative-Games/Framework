/*
 *  MIT License
 *
 * Copyright (C) 2022 Negative Games & Developers
 * Copyright (C) 2022 NegativeDev (NegativeKB, Eric)
 * Copyright (C) 2022 Contributors
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

package games.negative.framework.gui;

import games.negative.framework.gui.base.MenuBase;
import games.negative.framework.gui.holder.DropperGUIHolder;
import games.negative.framework.gui.internal.MenuItem;
import games.negative.framework.util.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
@Setter
public class DropperGUI implements MenuBase {
    private final ArrayList<MenuItem> items;
    private final HashMap<Player, Inventory> activeInventories;
    // Are people allowed to take items from the GUI?
    private final boolean allowTakeItems;
    private String title;
    private BiConsumer<Player, InventoryCloseEvent> onClose;
    private BiConsumer<Player, InventoryOpenEvent> onOpen;
    private BiConsumer<Player, InventoryClickEvent> playerInventoryClickEvent;

    /**
     * Constructor for GUI
     *
     * @param title Title
     * @apiNote The title supports color codes automatically!
     * @apiNote By default, allowTakeItems is false.
     */
    public DropperGUI(@NotNull String title) {
        this(title, false);
    }

    /**
     * Main constructor for the GUI
     *
     * @param title          Title
     * @param allowTakeItems Allowed taking items from the menu?
     * @apiNote The title supports color codes automatically!
     */
    public DropperGUI(@NotNull String title, boolean allowTakeItems) {
        this.title = title;
        this.allowTakeItems = allowTakeItems;

        items = new ArrayList<>();
        activeInventories = new HashMap<>();
    }

    /**
     * Open the GUI for the provided player
     *
     * @param player Player
     */
    public void open(@NotNull Player player) {
        DropperGUIHolder holder = new DropperGUIHolder(this);
        Inventory inv = Bukkit.createInventory(holder, InventoryType.DROPPER, Utils.color(title));

        player.openInventory(inv);
        activeInventories.put(player, inv);

        // Will simply put the items in the corresponding slots
        refresh(player);
    }

    @Override
    public void onOpen(BiConsumer<Player, InventoryOpenEvent> function) {
        onOpen = function;
    }

    @Override
    public void onClose(BiConsumer<Player, InventoryCloseEvent> function) {
        onClose = function;
    }

    @Override
    public void onInventoryClick(BiConsumer<Player, InventoryClickEvent> function) {
        playerInventoryClickEvent = function;
    }

    /**
     * Set Item to a certain index in the GUI
     *
     * @param index        Index/Placement of the Item in the GUI
     * @param itemFunction ItemStack Function
     * @apiNote There is no click event linked to this item
     * @apiNote First slot of GUIs are 0
     */
    public void setItem(int index, @NotNull Function<Player, ItemStack> itemFunction) {
        setItemClickEvent(index, itemFunction, null);
    }

    /**
     * Set Item Click Event to a certain index in the GUI
     *
     * @param index        Index/Placement of the Item in the GUI
     * @param itemFunction ItemStack
     * @param function     Click Event of the Item
     */
    public void setItemClickEvent(int index, @NotNull Function<Player, ItemStack> itemFunction, @Nullable BiConsumer<Player, InventoryClickEvent> function) {
        MenuItem menuItem = new MenuItem(index, itemFunction, function);

        Optional<MenuItem> firstItem = items.stream().filter(item -> item.getSlot() == index).findFirst();
        boolean indexExists = false;
        int itemIndex = 0;
        if (firstItem.isPresent()) {
            indexExists = true;
            MenuItem mi = firstItem.get();
            itemIndex = items.indexOf(mi);
        }

        if (indexExists)
            items.set(itemIndex, menuItem);
        else
            items.add(menuItem);
    }

    /**
     * Add Item Click Event to the GUI
     *
     * @param itemFunction ItemStack
     * @param function     Click Event of the Item
     * @apiNote This adds the Item to the next available slot
     */
    public void addItemClickEvent(@NotNull Function<Player, ItemStack> itemFunction, @Nullable BiConsumer<Player, InventoryClickEvent> function) {
        int i;
        for (i = 0; i < 5; i++) {
            int indexSlot = i;
            boolean inSlot = items.stream().anyMatch(menuItem -> menuItem.getSlot() == indexSlot);
            if (!inSlot)
                break;

        }

        setItemClickEvent(i, itemFunction, function);
    }

    /**
     * Add Item to the GUI
     *
     * @param itemFunction ItemStack
     * @apiNote This adds the Item to the next available slot
     */
    public void addItem(@NotNull Function<Player, ItemStack> itemFunction) {
        addItemClickEvent(itemFunction, null);
    }

    /**
     * Clears the {@link ItemStack} and item click functionality in the {@param slot} slot
     *
     * @param slot Slot of the item function you would like to remove
     */
    public void clearSlot(int slot) {
        items.stream().filter(menuItem -> menuItem.getSlot() == slot).findFirst().ifPresent(items::remove);
    }

    /**
     * Refreshes the menu for the provided player
     *
     * @param player Player
     */
    public void refresh(@NotNull Player player) {
        Inventory inv = activeInventories.get(player);
        if (inv == null)
            return;

        items.forEach(menuItem -> {
            try {
                inv.setItem(menuItem.getSlot(), menuItem.getItem().apply(player));
            } catch (Exception ignored) {
            }
        });
    }
}
