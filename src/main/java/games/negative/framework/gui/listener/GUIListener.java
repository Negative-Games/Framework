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

package games.negative.framework.gui.listener;

import games.negative.framework.gui.holder.CraftingGUIHolder;
import games.negative.framework.gui.holder.DropperGUIHolder;
import games.negative.framework.gui.holder.GUIHolder;
import games.negative.framework.gui.holder.HopperGUIHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public class GUIListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof GUIHolder) {
            GUIHolder base = (GUIHolder) holder;
            base.onClick(event);
        }

        if (holder instanceof HopperGUIHolder) {
            HopperGUIHolder base = (HopperGUIHolder) holder;
            base.onClick(event);
        }

        if (holder instanceof DropperGUIHolder) {
            DropperGUIHolder base = (DropperGUIHolder) holder;
            base.onClick(event);
        }

        if (holder instanceof CraftingGUIHolder) {
            CraftingGUIHolder base = (CraftingGUIHolder) holder;
            base.onClick(event);
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof GUIHolder) {
            GUIHolder base = (GUIHolder) holder;
            base.onClose((Player) event.getPlayer(), event);
        }

        if (holder instanceof HopperGUIHolder) {
            HopperGUIHolder base = (HopperGUIHolder) holder;
            base.onClose((Player) event.getPlayer(), event);
        }

        if (holder instanceof CraftingGUIHolder) {
            CraftingGUIHolder base = (CraftingGUIHolder) holder;
            base.onClose((Player) event.getPlayer(), event);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof GUIHolder) {
            GUIHolder base = (GUIHolder) holder;
            base.onOpen((Player) event.getPlayer(), event);
        }

        if (holder instanceof HopperGUIHolder) {
            HopperGUIHolder base = (HopperGUIHolder) holder;
            base.onOpen((Player) event.getPlayer(), event);
        }

        if (holder instanceof CraftingGUIHolder) {
            CraftingGUIHolder base = (CraftingGUIHolder) holder;
            base.onOpen((Player) event.getPlayer(), event);
        }
    }
}
