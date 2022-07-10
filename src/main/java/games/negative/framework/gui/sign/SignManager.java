package games.negative.framework.gui.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import games.negative.framework.BasePlugin;
import games.negative.framework.gui.sign.packets.BlockChangePacket;
import games.negative.framework.gui.sign.packets.SignEditorPacket;
import games.negative.framework.gui.sign.packets.TileEntityPacket;
import games.negative.framework.util.Utils;
import games.negative.framework.util.version.VersionChecker;
import lombok.Getter;
import lombok.Setter;
import one.util.streamex.StreamEx;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages the back-end logic for the sign input util
 */
public class SignManager {

    @Getter
    private static final HashMap<Player, SignGUI> input = new HashMap<>();

    @Getter
    private static final ProtocolManager protocol;

    @Getter
    @Setter
    private static BlockPosition location;

    static {
        protocol = ProtocolLibrary.getProtocolManager();
    }

    /**
     * Opens the sign GUI for the given player.
     *
     * @param player  The player to open the GUI for.
     * @param signGUI THe gui to open
     * @author Seailz & OrangeUtan
     */
    public static void open(@NotNull Player player, @NotNull SignGUI signGUI) {
        VersionChecker versionChecker = VersionChecker.getInstance();

        setLocation(new BlockPosition(player.getLocation().getBlockX(), 0, player.getLocation().getBlockZ()));

        SignEditorPacket editor = new SignEditorPacket(protocol.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR));
        editor.setLocation(location);

        NbtCompound signNbt = NbtFactory.ofCompound("SignNBT");

        if (signGUI.getLines().size() >= 1)
            signNbt.put("Text1", "{\"text\":\"" + signGUI.getLines().get(0).getText().apply(player) + "\"}");
        else
            signNbt.put("Text4", "{\"text\":\"\"}");
        if (signGUI.getLines().size() >= 2)
            signNbt.put("Text2", "{\"text\":\"" + signGUI.getLines().get(1).getText().apply(player) + "\"}");
        else
            signNbt.put("Text4", "{\"text\":\"\"}");
        if (signGUI.getLines().size() >= 3)
            signNbt.put("Text3", "{\"text\":\"" + signGUI.getLines().get(2).getText().apply(player) + "\"}");
        else
            signNbt.put("Text4", "{\"text\":\"\"}");
        if (signGUI.getLines().size() >= 4)
            signNbt.put("Text4", "{\"text\":\"" + signGUI.getLines().get(3).getText().apply(player) + "\"}");
        else
            signNbt.put("Text4", "{\"text\":\"\"}");

        signNbt.put("id", "minecraft:sign");
        signNbt.put("x", location.getX());
        signNbt.put("y", location.getY());
        signNbt.put("z", location.getZ());

        BlockChangePacket blockChange = new BlockChangePacket(protocol.createPacket(PacketType.Play.Server.BLOCK_CHANGE));
        blockChange.setLocation(location);

        player.sendBlockChange(new Location(player.getWorld(), 0, 0, 0), versionChecker.isLegacy() ? Material.SIGN : Material.getMaterial("OAK_SIGN"), (byte) 0);

        TileEntityPacket tileEntity = new TileEntityPacket(protocol.createPacket(PacketType.Play.Server.TILE_ENTITY_DATA));

        tileEntity.setNbtData(signNbt);
        tileEntity.setAction(9);
        tileEntity.setLocation(location);

        blockChange.sendPacket(player);
        editor.sendPacket(player);
        tileEntity.sendPacket(player);
        // sendPacket(player, signEditorPacket.getHandle());

        input.put(player, signGUI);

        if (input.get(player).getOnClose() != null)
            listen();
    }

    /**
     * Creates the packet listener
     */
    private static void listen() {
        protocol.addPacketListener(new PacketAdapter(BasePlugin.getInst(), PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player p = event.getPlayer();
                if (input.containsKey(p)) {
                    SignGUI signGUI = input.get(p);
                    StructureModifier<String> values = event.getPacket().getStrings();

                    // String[] lines = new String[]{values.read(1), values.read(2), values.read(3), values.read(4)};
                    WrappedChatComponent[] lines = event.getPacket().getChatComponentArrays().read(0);
                    String[] linesStrings = new String[]{lines[0].getJson(), lines[1].getJson(), lines[2].getJson(), lines[3].getJson()};
                    signGUI.getOnClose().accept(p, linesStrings);
                    input.remove(p);
                }
            }
        });
    }

    /**
     * Sends a packet with ProtocolLib
     *
     * @param player The player to send the packet to.
     * @param packet The packet to send.
     */
    private static void sendPacket(@NotNull Player player, @NotNull PacketContainer packet) {
        try {
            protocol.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
