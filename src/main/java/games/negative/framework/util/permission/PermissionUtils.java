package games.negative.framework.util.permission;

import games.negative.framework.BasePlugin;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

/**
 * A way to interact with Bukkit's permission API with ease.
 * @author Seailz
 */
public class PermissionUtils {

    /**
     * Checks if a player has a permission
     * @param player The player you want to check
     * @param permission The permission you want to check
     * @return true or false
     * @author Seailz
     */
    public boolean hasPermission(@NonNull Player player, @NonNull String permission) {
        return player.hasPermission(permission);
    }

    /**
     * Applies a permission to a player
     * @param player The player you want to apply the permission to
     * @param permission The permission you want to apply
     * @author Seailz
     */
    public void applyPermission(@NotNull Player player, @NotNull String permission) {
        player.addAttachment(BasePlugin.getInst(), permission, true);
    }

    /**
     * Un-sets a permission from a player
     * @param player The player you want to unset the permission from
     * @param permission The permission you want to unset
     * @author Seailz
     */
    public void unsetPermission(@NotNull Player player, @NotNull String permission) {
        for (PermissionAttachmentInfo o : player.getEffectivePermissions()) {
            if (o.getPermission().equals(permission)) {
                player.removeAttachment(o.getAttachment());
                break;
            }
        }
    }
}
