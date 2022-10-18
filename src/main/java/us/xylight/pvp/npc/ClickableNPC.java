package us.xylight.pvp.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Material;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import us.xylight.pvp.XyPVP;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class ClickableNPC implements Listener {
    public Location location;
    public String name;
    public XyPVP pl;
    public ServerPlayer npc;

    private Consumer<PacketEvent> onClick;

    public ClickableNPC(Location loc, String name, Consumer<PacketEvent> click, Plugin plugin, NPCSkin skin) {
        this.onClick = click;
        this.location = loc;
        this.name = name;
        this.pl = (XyPVP) plugin;


        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel level = ((CraftWorld) loc.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);

        gameProfile.getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));

        npc = new ServerPlayer(server, level, gameProfile, null);
        teleport(npc, loc);

        Bukkit.getOnlinePlayers().forEach(this::showNPC);
        Bukkit.getPluginManager().registerEvents(this, plugin);

        XyPVP.getProtocol().addPacketListener(
        new PacketAdapter(
            XyPVP.getInstance(),
            ListenerPriority.NORMAL,
            PacketType.Play.Client.USE_ENTITY
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                int entityID = packet.getIntegers().read(0);
                EnumWrappers.EntityUseAction action = packet.getEnumEntityUseActions().read(0).getAction();
                EnumWrappers.Hand hand = null;
                try {
                    hand = packet.getEnumEntityUseActions().read(0).getHand();
                } catch (Exception ignored) {
                    return;
                }
                if (entityID == npc.getId() &&
                        hand.equals(EnumWrappers.Hand.MAIN_HAND) &&
                        action.equals(EnumWrappers.EntityUseAction.INTERACT)) {
                    onClick.accept(event);
                    System.out.println("h");
                }
            }
        });
    }

    public void teleport(ServerPlayer npc, Location to) {
        npc.setPos(to.getX(), to.getY(), to.getZ());
    }

    public void showNPC(Player p) {
        ServerGamePacketListenerImpl ps = ((CraftPlayer) p).getHandle().connection;

        ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
        ps.send(new ClientboundAddPlayerPacket(npc));
    }

    public void hideNPC(Player p) {
        ServerGamePacketListenerImpl ps = ((CraftPlayer) p).getHandle().connection;

        ps.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc));
        ps.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
    }
}
