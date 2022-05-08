/*
 * MIT License
 *
 * Copyright (c) 2022 Negative
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

package games.negative.framework;

import games.negative.framework.command.logging.CommandLogListener;
import games.negative.framework.command.repository.CommandRepository;
import games.negative.framework.command.repository.FrameworkCommandRepository;
import games.negative.framework.command.shortcommand.provider.ShortCommandsListener;
import games.negative.framework.cooldown.Cooldowns;
import games.negative.framework.gui.listener.GUIListener;
import games.negative.framework.inputlistener.InputListener;
import games.negative.framework.message.FrameworkMessage;
import games.negative.framework.util.FileLoader;
import games.negative.framework.util.version.VersionChecker;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

@Getter
public abstract class BasePlugin extends JavaPlugin {

    private static BasePlugin inst;
    private CommandRepository commandRepository;

    @Override
    public void onEnable() {
        inst = this;
        FrameworkMessage.init();
        new VersionChecker();

        registerListeners(
                new GUIListener(),
                new ShortCommandsListener(),
                new InputListener(),
                new CommandLogListener()
        );

        Cooldowns.startInternalCooldowns(this);

        commandRepository = new FrameworkCommandRepository();
    }

    /**
     * Load custom files with pre-written data.
     *
     * @param plugin Plugin instance
     * @param names  File names
     */
    public void loadFiles(@NotNull JavaPlugin plugin, @NotNull String... names) {
        File dataFolder = plugin.getDataFolder();
        Arrays.stream(names).forEach(name -> {
            File file = new File(dataFolder, name);
            FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);

            if (!file.exists())
                FileLoader.loadFile(plugin, name);

            try {
                fileConfig.load(file);
            } catch (Exception e3) {
                System.out.println("Could not load " + name + " due to the following:");
                e3.printStackTrace();
            }

            fileConfig.getKeys(false).forEach(priceString ->
                    fileConfig.set(priceString, fileConfig.getString(priceString)));
        });

    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void registerCommands(@NotNull Command... commands) {
        VersionChecker versionChecker = VersionChecker.getInstance();

        Server server = Bukkit.getServer();
        Field field = server.getClass().getDeclaredField("commandMap");
        field.setAccessible(true);

        Arrays.stream(commands).forEach(iCommand -> {
            try {
                CommandMap commandMap = (CommandMap) field.get(server);

                String name = iCommand.getName();

                org.bukkit.command.Command command = commandMap.getCommand(name);
                if (command != null) {
                    Map<String, Command> map;
                    if (versionChecker.isLegacy()) {
                        Field commandField = commandMap.getClass().getDeclaredField("knownCommands");
                        commandField.setAccessible(true);
                        map = (Map<String, Command>) commandField.get(commandMap);
                    } else {
                        map = (Map<String, Command>) commandMap.getClass().getDeclaredMethod("getKnownCommands").invoke(commandMap);
                    }
                    command.unregister(commandMap);
                    map.remove(name);
                    iCommand.getAliases().forEach(map::remove);
                }

                commandRepository.add(iCommand);
                commandMap.register(name, iCommand);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Registers Listener classes
     *
     * @param listeners Listeners
     */
    public void registerListeners(@NotNull Listener... listeners) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Arrays.stream(listeners).forEach(listener -> pluginManager.registerEvents(listener, this));
    }

    public static BasePlugin getInst() {
        return inst;
    }
}
