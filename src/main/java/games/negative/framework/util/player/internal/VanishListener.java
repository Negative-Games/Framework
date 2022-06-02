package games.negative.framework.util.player.internal;

import games.negative.framework.util.player.VanishUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Hides vanished players when a new player joins
 * @author Seailz
 */
public class VanishListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        VanishUtils.getPlugin().getVanished().forEach(player -> {
            if (!Bukkit.getOfflinePlayer(player).isOnline()) return;
            e.getPlayer().hidePlayer(Bukkit.getPlayer(player));
        });
    }

}
