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

package games.negative.framework.message;

import games.negative.framework.util.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
public class Message implements Serializable {

    private final String initial;
    private String message;

    public Message(@NotNull String... msg) {
        String actual = String.join("\n", msg);
        setMessage(actual);
        this.initial = actual;
    }

    public Message(@NotNull String msg) {
        setMessage(msg);
        this.initial = msg;
    }

    public Message(@NotNull Collection<String> msg) {
        String actual = String.join("\n", msg);
        setMessage(actual);
        this.initial = actual;
    }

    /**
     * Replacer
     * <p>
     * Simply replaces object1 with object2.
     * Could be a string, int, double, whatever
     *
     * @param placeholder - Placeholder
     *                    (ex. %player% or %amount%)
     *                    <p>
     * @param replacement - Replacement
     *                    (ex. "Negative" or 100)
     *                    <p>
     * @return - Message instance with the replacement
     */
    public Message replace(String placeholder, String replacement) {
        String newMSG = this.message;

        newMSG = newMSG.replaceAll(placeholder, replacement);

        setMessage(newMSG);
        return this;
    }

    /**
     * Send Message
     * <p>
     * Sends the message to a CommandSender.
     * Usually a player or console
     *
     * @param sender - Sender
     */
    public void send(@NotNull CommandSender sender) {
        if (this.message.contains("\n")) {
            String[] msg = getMessage().split("\n");
            for (String s : msg) {
                sender.sendMessage(Utils.color(s));
            }
            this.message = initial;
            return;
        }
        sender.sendMessage(Utils.color(getMessage()));
        setMessage(getInitial());
    }

    /**
     * Send Message to a list of players
     * <p>
     * Simply sends the message to a list of players
     *
     * @param players - List of players
     */
    public void send(@NotNull Iterable<CommandSender> players) {
        players.forEach(this::send);
    }

    /**
     * Broadcast message
     * <p>
     * Simply broadcasts the message for
     * all to see!
     */
    public void broadcast() {
        if (getMessage().contains("\n")) {
            String[] msg = getMessage().split("\n");
            for (String s : msg) {
                Utils.broadcast(s);
            }
            setMessage(getInitial());
            return;
        }
        Utils.broadcast(getMessage());
        setMessage(getInitial());
    }

    /**
     * Returns the string
     * @return the defined message
     */
    @Override
    public String toString() {
        return getMessage();
    }

}
