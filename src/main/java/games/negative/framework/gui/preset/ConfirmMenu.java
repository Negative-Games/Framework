package games.negative.framework.gui.preset;

import games.negative.framework.gui.GUI;
import games.negative.framework.util.ItemBuilder;
import games.negative.framework.util.version.VersionChecker;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class ConfirmMenu extends GUI {
    public ConfirmMenu(@NotNull String title, BiConsumer<Player, InventoryClickEvent> onConfirm, BiConsumer<Player, InventoryClickEvent> onCancel) {
        super(title, 3);
        ItemStack confirm = VersionChecker.getInstance().isModern() ?
                new ItemBuilder(Material.valueOf("LIME_STAINED_GLASS_PANE"))
                        .setName("&a&lConfirm").build() : new ItemBuilder(Material.STAINED_GLASS_PANE).setDyeColor(DyeColor.LIME).setName("&a&lConfirm").build();

        ItemStack cancel = VersionChecker.getInstance().isModern() ?
                new ItemBuilder(Material.valueOf("RED_STAINED_GLASS_PANE"))
                        .setName("&c&lCancel").build() : new ItemBuilder(Material.STAINED_GLASS_PANE).setDyeColor(DyeColor.RED).setName("&c&lCancel").build();

        setItemClickEvent(12, player -> confirm, onConfirm);

        setItemClickEvent(14, player -> cancel, onCancel);

    }

    public ConfirmMenu(@NotNull String title, BiConsumer<Player, InventoryClickEvent> onConfirm) {
        this(title, onConfirm, (player, event) -> {
            player.closeInventory();
        });
    }
}
