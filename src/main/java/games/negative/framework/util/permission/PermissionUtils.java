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
    public static boolean hasPermission(@NonNull Player player, @NonNull String permission) {
        return player.hasPermission(permission);
    }

    /**
     * Applies a permission to a player
     * @param player The player you want to apply the permission to
     * @param permission The permission you want to apply
     * @author Seailz
     */
    public static void applyPermission(@NotNull Player player, @NotNull String permission) {
        player.addAttachment(BasePlugin.getInst(), permission, true);
    }

    /**
     * Removes a permission from a player
     * @param player The player you want to remove the permission from
     * @param permission The permission you want to remove
     * @author Seailz
     */
    public static void removePermission(@NotNull Player player, @NotNull String permission) {
        for (PermissionAttachmentInfo node : player.getEffectivePermissions()) {
            if (node.getPermission().equals(permission)) {
                player.removeAttachment(node.getAttachment());
                break;
            }
        }
    }
}
