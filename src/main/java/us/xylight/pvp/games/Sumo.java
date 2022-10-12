package us.xylight.pvp.games;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.util.PlaceableBlock;

import java.util.Arrays;
import java.util.UUID;

public class Sumo extends Game {
    public BoundingBox copyArea;

    public Sumo(Player[] plyers, UUID[] playerUUIDs) {
        super(plyers, playerUUIDs);

    }
    @Override
    public void teleportPlayers(World world, int x, int y, int z) {
        int index = 0;
        for (Player player : players) {
            Location loc;
            loc = new Location(world, x + (index % 2 == 0 ? 2 : 10), 5, z + 6, (index % 2 == 0 ? -90f : 90f), 0f);
            player.teleport(loc);
            player.getInventory().setContents(gameInventory);
            index++;
        }

    }

    @Override
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (Arrays.asList(playerUUIDs).contains(e.getUniqueId()) && (!(e.getLocation().getBlockY() < 0))) {
            event.setDamage(0);
        }
    }

    @Override
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (Arrays.asList(playerUUIDs).contains(event.getPlayer().getUniqueId()) && !gameStarted) {
            event.setCancelled(true);
        }
        if (Arrays.asList(playerUUIDs).contains(event.getPlayer().getUniqueId()) && (event.getPlayer().getLocation().getBlockY() < -5)) {
            event.getPlayer().setHealth(0);
        }
    }

    @Override
    public void buildArena(int passedX, int passedY, int passedZ) {
        int y = 3;

        copyArea = new BoundingBox(-14, 3, -26, -26, 3, -14);
        arenaArea = new BoundingBox(passedX, y, passedZ, passedX + 12, y, passedZ + 12);

        Vector max1 = arenaArea.getMax();
        Vector min1 = arenaArea.getMin();
        Vector max2 = copyArea.getMax();
        Vector min2 = copyArea.getMin();
        World world = players[0].getWorld();

        for (int x2 = min2.getBlockX(); x2 <= max2.getBlockX(); x2++) {
            for (int y2 = min2.getBlockY(); y2 <= max2.getBlockY(); y2++) {
                for (int z2 = min2.getBlockZ(); z2 <= max2.getBlockZ(); z2++) {
                    new PlaceableBlock(world.getUID(), passedX + (x2 - min2.getBlockX()), y + (y2 - min2.getBlockY()), passedZ + (z2 - min2.getBlockZ()), world.getBlockAt(x2, y2, z2).getType()).compute();
                }
            }
        }
    }
}
