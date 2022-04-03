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

package games.negative.framework.gui;

import games.negative.framework.gui.base.MenuHolder;
import games.negative.framework.gui.internal.MenuItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
@Getter @Setter
public class BaseGUI implements MenuHolder<GUI> {

    private final GUI gui;
    private Inventory inventory;

    public void onOpen(@NotNull Player player, @NotNull InventoryOpenEvent event) {
        Optional.ofNullable(gui.getOnOpen()).ifPresent(function ->
                function.accept(player, event));
    }

    public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent event) {
        Optional.ofNullable(gui.getOnClose()).ifPresent(closeFunction ->
                closeFunction.accept(player, event));

        gui.getActiveInventories().remove(player);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (!gui.isAllowTakeItems())
            event.setCancelled(true);

        if (event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.PLAYER) {
            BiConsumer<Player, InventoryClickEvent> playerClick = gui.getPlayerInventoryClickEvent();
            if (playerClick != null)
                playerClick.accept((Player) event.getWhoClicked(), event);
            return;
        }

        int slot = event.getSlot();

        MenuItem item = gui.getItems().stream().filter(menuItem -> menuItem.getSlot() == slot).findFirst().orElse(null);
        if (item == null)
            return;

        BiConsumer<Player, InventoryClickEvent> click = item.getClickEvent();
        if (click == null)
            return;

        click.accept((Player) event.getWhoClicked(), event);
    }

    @Override
    public @NotNull GUI getMenu() {
        return gui;
    }

}
