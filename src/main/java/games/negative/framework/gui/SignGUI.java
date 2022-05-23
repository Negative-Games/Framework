package games.negative.framework.gui;

import games.negative.framework.BasePlugin;
import games.negative.framework.gui.holder.SignGUIHolder;
import games.negative.framework.gui.internal.SignMenuFactory;
import lombok.Getter;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
public class SignGUI {

    private final HashMap<Player, Inventory> activeInventories;
    private BiConsumer<Player, String[]> onSubmit;

    public SignGUI() {
        activeInventories = new HashMap<>();
    }

    public void open(@NotNull Player player) {
        SignMenuFactory.Menu menu = BasePlugin.sign.newMenu(Arrays.asList("", "Enter", "your input"))
                .reopenIfFail(true)
                .response((p, strings) -> {
                    setOnSubmit(p, strings);
                    return true;
                });

        menu.open(player);
    }

    public void onSubmit(BiConsumer<Player, String[]> function) {
        onSubmit = function;
    }

    public void setOnSubmit(@NotNull Player player, String[] lines) {
        BiConsumer<Player, String[]> onSubmit = this.getOnSubmit();
        if (onSubmit != null) onSubmit.accept(player, lines);
    }
}