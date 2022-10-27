package us.xylight.pvp.util;

import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerUtils {
    public static void addRedVignette(Player p) {
        CraftPlayer cp = (CraftPlayer) p;

        WorldBorder w = new WorldBorder();
        w.world = ((CraftWorld) p.getWorld()).getHandle();
        w.setSize(1);
        w.setCenter(p.getLocation().getX() + 10_000, p.getLocation().getZ() + 10_000);
        cp.getHandle().connection.send(new ClientboundSetBorderSizePacket(w));
    }

    public static void removeRedVignette(Player p) {
        CraftPlayer cp = (CraftPlayer) p;
        WorldBorder w = new WorldBorder();
        w.world = ((CraftWorld) p.getWorld()).getHandle();
        w.setSize(30_000_000);
        w.setCenter(p.getLocation().getX(), p.getLocation().getZ());
        cp.getHandle().connection.send(new ClientboundSetBorderSizePacket(w));
    }
}
