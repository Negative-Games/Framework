/*
 *  MIT License
 *
 * Copyright (C) 2022 Negative Games & Developers
 * Copyright (C) 2022 NegativeDev (NegativeKB, Eric)
 * Copyright (C) 2022 Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package games.negative.framework.command;

import games.negative.framework.command.annotation.CommandInfo;
import games.negative.framework.command.base.CommandBase;
import games.negative.framework.command.event.CommandLogEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class Command extends org.bukkit.command.Command implements CommandBase {

    @Deprecated
    private final List<SubCommand> subCommands = new ArrayList<>();
    private final List<Command> commands = new ArrayList<>();

    public boolean consoleOnly = false;
    public boolean playerOnly = false;
    public boolean disabled = false;
    public String permissionNode = "";
    public List<String> shortCommands = new ArrayList<>();
    private String[] params;
    private TabCompleter completer;
    private Consumer<CommandLogEvent> logEvent;
    private CommandBase parent;

    public Command() {
        this("1");
    }

    public Command(@NotNull String name) {
        this(name, "", Collections.emptyList());
    }

    public Command(@NotNull String name, @NotNull String description) {
        this(name, description, Collections.emptyList());
    }

    public Command(@NotNull String name, @NotNull Collection<String> aliases) {
        this(name, "", aliases);
    }

    public Command(@NotNull String name, @NotNull String description, @NotNull Collection<String> aliases) {
        super(name, description, "/" + name, new ArrayList<>(aliases));

        boolean hasInfo = getClass().isAnnotationPresent(CommandInfo.class);
        if (hasInfo) {
            CommandInfo annotation = getClass().getAnnotation(CommandInfo.class);
            setName(annotation.name());

            if (annotation.consoleOnly())
                setConsoleOnly(true);

            if (annotation.playerOnly())
                setPlayerOnly(true);

            if (annotation.disabled())
                setDisabled(true);

            if (!annotation.description().isEmpty())
                setDescription(annotation.description());

            if (!annotation.permission().isEmpty())
                setPermissionNode(annotation.permission());

            String[] annoAliases = annotation.aliases();
            if (!annoAliases[0].isEmpty())
                setAliases(Arrays.asList(annoAliases));

            String[] annoShort = annotation.shortCommands();
            if (!annoShort[0].isEmpty())
                setShortCommands(Arrays.asList(annoShort));

            String[] annoArgs = annotation.args();
            if (!annoArgs[0].isEmpty())
                setParams(annoArgs);
        }

    }

    public abstract void onCommand(CommandSender sender, String[] args);

    public void onCommand(CommandSender sender, String label, String[] args) {
        onCommand(sender, args);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        execute(sender, args);
        return true;
    }

    /**
     * Run SubCommand Function
     *
     * @param subCommand SubCommand
     * @param sender     Player/Sender
     * @param args       Arguments
     */
    @Override
    public void runSubCommand(SubCommand subCommand, CommandSender sender, String[] args) {
        subCommand.execute(sender, args);
    }

    @Override
    public void ifHasPermission(@NotNull CommandSender sender, @NotNull String perm, @NotNull Consumer<CommandSender> consumer) {
        if (sender.hasPermission(perm))
            consumer.accept(sender);
    }

    @Override
    public void ifNotHasPermission(@NotNull CommandSender sender, @NotNull String perm, @NotNull Consumer<CommandSender> consumer) {
        if (!sender.hasPermission(perm))
            consumer.accept(sender);
    }

    @Override
    public void ifPlayer(@NotNull CommandSender sender, @NotNull Consumer<Player> consumer) {
        if (sender instanceof Player)
            consumer.accept((Player) sender);
    }

    public void ifConsole(@NotNull CommandSender sender, @NotNull Consumer<ConsoleCommandSender> consumer) {
        if (sender instanceof ConsoleCommandSender)
            consumer.accept((ConsoleCommandSender) sender);
    }

    /**
     * Add one or more SubCommands to
     * a command
     *
     * @param subCommands SubCommand(s)
     */
    @Override
    public void addSubCommands(SubCommand... subCommands) {
        Arrays.stream(subCommands).forEach(subCommand -> subCommand.setParent(this));
        this.subCommands.addAll(Arrays.asList(subCommands));
    }

    public void setTabComplete(BiFunction<CommandSender, String[], List<String>> function) {
        this.completer = (sender, command, alias, args) -> {
            if (alias.equalsIgnoreCase(getName()) || getAliases().contains(alias.toLowerCase())) {
                return function.apply(sender, args);
            }
            return null;
        };
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (completer == null || this.completer.onTabComplete(sender, this, alias, args) == null) {
            String lastWord = args[args.length - 1];
            Player senderPlayer = sender instanceof Player ? (Player) sender : null;
            ArrayList<String> matchedPlayers = new ArrayList<>();
            sender.getServer().getOnlinePlayers().stream()
                    .filter(player -> senderPlayer == null || senderPlayer.canSee(player) && StringUtil.startsWithIgnoreCase(player.getName(), lastWord))
                    .forEach(player -> matchedPlayers.add(player.getName()));

            matchedPlayers.sort(String.CASE_INSENSITIVE_ORDER);
            return matchedPlayers;
        }
        return this.completer.onTabComplete(sender, this, alias, args);
    }

    @Override
    public @NotNull String getName() {
        return super.getName();
    }

    @Override
    public @Nullable CommandBase getParent() {
        return parent;
    }

    @Override
    public void setParent(@NotNull CommandBase parent) {
        this.parent = parent;
    }

    @Override
    public boolean runLogEvent(CommandBase base, CommandSender sender, String[] args) {
        if (logEvent == null)
            return false;

        CommandLogEvent event = new CommandLogEvent(sender, args, this);
        Bukkit.getPluginManager().callEvent(event);

        return event.isCancelled();
    }

    @Override
    public String getPermission() {
        return getPermissionNode();
    }
}
