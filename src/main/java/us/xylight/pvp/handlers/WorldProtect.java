package us.xylight.pvp.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.games.Game;

import java.util.Arrays;
import java.util.UUID;

public class WorldProtect implements Listener {
    XyPVP plugin;
    public WorldProtect(XyPVP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // [x1, x2], [z1, z2]
    public static int[][][] protectedAreas = {{{108, 124}, {121, 139}}};
    public static boolean enabled = true;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!enabled) return;
        for (Game game : plugin.queueHandler.games) {
            if (Arrays.asList(game.players).contains(event.getPlayer())) {
                event.setCancelled(false);
                return;
            }
        }
        event.setCancelled(true);
        for (int[][] protectedArea : protectedAreas)
        {
            int blockX = event.getBlock().getLocation().getBlockX();
            int blockZ = event.getBlock().getLocation().getBlockZ();

            if (blockX >= protectedArea[0][0] && blockX <= protectedArea[0][1])
                if (blockZ >= protectedArea[1][0] && blockZ <= protectedArea[1][1])
                    event.setCancelled(true);
        }
    }
}