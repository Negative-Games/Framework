package games.negative.framework.gui.holder;

import games.negative.framework.gui.SignGUI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

@Getter
@Setter
@RequiredArgsConstructor
public class SignGUIHolder {
    private Sign sign;
    private SignGUI gui;

    public Sign getSign() {
        return sign;
    }
}