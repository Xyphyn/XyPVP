package us.xylight.pvp.handlers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.games.Game;

public class WorldProtect implements Listener {
    public WorldProtect(XyPVP plugin) {

        Bukkit.getPluginManager().registerEvents(this, XyPVP.getInstance());
    }

    // [x1, x2], [z1, z2]
    public static int[][][] protectedAreas = {{{108, 124}, {121, 139}}};
    public static boolean enabled = true;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!enabled) return;
        for (Game game : QueueHandler.getGames()) {
            if (game.players.contains(event.getPlayer())) {
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
