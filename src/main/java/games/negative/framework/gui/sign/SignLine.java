package games.negative.framework.gui.sign;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.function.Function;

@Data
@AllArgsConstructor
public class SignLine {

    private Function<Player, String> text;


}
