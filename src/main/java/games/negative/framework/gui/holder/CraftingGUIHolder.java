package games.negative.framework.gui.holder;

import games.negative.framework.gui.CraftingGUI;
import games.negative.framework.gui.DropperGUI;
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
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Crafting GUI Holder
 * @author Negative
 */
@Getter
@Setter
@RequiredArgsConstructor
public class CraftingGUIHolder implements MenuHolder<CraftingGUI> {
        private final CraftingGUI gui;
        private Inventory inventory;

        @Override
        public void onOpen(@NotNull Player player, @NotNull InventoryOpenEvent event) {
            BiConsumer<Player, InventoryOpenEvent> onOpen = gui.getOnOpen();
            if (onOpen != null) onOpen.accept(player, event);
        }

        @Override
        public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent event) {
            BiConsumer<Player, InventoryCloseEvent> onClose = gui.getOnClose();
            if (onClose != null) onClose.accept(player, event);

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
        public @NotNull CraftingGUI getMenu() {
            return gui;
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }
