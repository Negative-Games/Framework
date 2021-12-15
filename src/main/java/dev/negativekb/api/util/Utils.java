package dev.negativekb.api.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Utils {

    private final DecimalFormat df;

    static {
        df = new DecimalFormat("###,###,###,###,###,###,###,###.##");
    }

    /**
     * Takes an Integer and turns it into a fancy string
     *
     * @param i Input
     * @return Output (Fancy String!)
     */
    public String decimalFormat(int i) {
        return df.format(i);
    }

    /**
     * Takes a Double and turns it into a fancy string
     *
     * @param i Input
     * @return Output (Fancy String!)
     */
    public String decimalFormat(double i) {
        return df.format(i);
    }

    /**
     * Takes a Float and turns it into a fancy string
     *
     * @param i Input
     * @return Output (Fancy String!)
     */
    public String decimalFormat(float i) {
        return df.format(i);
    }

    /**
     * Takes an Object and turns it into a fancy string
     *
     * @param i Input
     * @return Output (Fancy String!)
     */
    public String decimalFormat(@NotNull Object i) {
        return df.format(i);
    }

    /**
     * Get all online players in a List format
     *
     * @return Returns all online players
     */
    public List<Player> getOnlinePlayers() {
        List<? extends Player> collect = new ArrayList<Player>(Bukkit.getOnlinePlayers());
        return new ArrayList<>(collect);
    }

    /**
     * Executes a command as the Console
     *
     * @param command Command input
     */
    public void executeConsoleCommand(@NotNull String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    /**
     * Broadcast a message to the server
     *
     * @param message Message input
     */
    public void broadcast(@NotNull String message) {
        Bukkit.broadcastMessage(color(message));
    }

    /**
     * Colorize a String
     *
     * @param input Input String
     * @return Chat Color formatted String
     */
    @NotNull
    public String color(@NotNull String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    /**
     * Colorize a List of Strings
     *
     * @param input Input StringList
     * @return Chat Color formatted List of Strings
     */
    public List<String> color(@NotNull List<String> input) {
        List<String> returnValue = new ArrayList<>();
        input.forEach(s -> returnValue.add(color(s)));
        return returnValue;
    }

    /**
     * Checks if a provided plugin name is enabled
     *
     * @param name Plugin Name
     * @return Returns true if the plugin is enabled
     */
    public boolean hasPlugin(@NotNull String name) {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }

}
