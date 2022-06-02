package games.negative.framework.util.player;

import games.negative.framework.BasePlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * A Utils class to make vanishing players easier.
 * @author Seailz - https://www.seailz.com
 */
public class VanishUtils {

    @Getter
    @Setter
    private static BasePlugin plugin;

    /**
     * Creates a {@link VanishUtils} instance
     * @param plugin A {@link BasePlugin} instance which will be used to keep an array of vanished players
     * @author Seailz
     */
    public VanishUtils(BasePlugin plugin) { setPlugin(plugin); }

    /**
     * Vanishes a player
     * @param player the player you want to hide.
     * @author Seailz
     */
    public void vanish(Player player) {
        // Adds players to the vanished arraylist
        getPlugin().getVanished().add(player.getUniqueId());

        // Hides the player from all online players
        Bukkit.getOnlinePlayers().forEach(o -> {
            o.hidePlayer(player);
        });
    }

    /**
     * Un-vanishes a player
     * @param player The player you want to un-vanish
     * @author Seailz
     */
    public void unVanish(Player player) {
        // Removes the player from the vanished arraylist
        getPlugin().getVanished().remove(player.getUniqueId());

        // Shows the player to all online players
        Bukkit.getOnlinePlayers().forEach(o -> {
            o.showPlayer(player);
        });
    }

}
