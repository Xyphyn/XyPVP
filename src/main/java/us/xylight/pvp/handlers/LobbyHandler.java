package us.xylight.pvp.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.xylight.pvp.XyPVP;

public class LobbyHandler implements Listener {
    // [x1, x2], [z1, z2]
    public int[][][] lobbyCoords = {{{108, 124}, {121, 139}}};
    public ItemStack[] lobbyInv = new ItemStack[9];
    public boolean enabled = true;

    public LobbyHandler() {
        Bukkit.getScheduler().runTaskTimer(XyPVP.getInstance(), () -> {
            if (enabled) { hotbarChecker(); resetHealth(); }
        }, 0, 1);

        ItemStack gameMenu = new ItemStack(Material.NETHER_STAR);
        ItemMeta gameMenuMeta = gameMenu.getItemMeta();
        assert gameMenuMeta != null;
        gameMenuMeta.setDisplayName(String.format("%s%sGame Menu", ChatColor.BLUE, ChatColor.BOLD ));
        gameMenu.setItemMeta(gameMenuMeta);

        lobbyInv[0] = gameMenu;

        Bukkit.getPluginManager().registerEvents(this, XyPVP.getInstance());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (inLobby(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    public static boolean inLobby(Player player) {
        int playerX = player.getLocation().getBlockX();
        int playerZ = player.getLocation().getBlockZ();

        for (int[][] lobbyCoord : XyPVP.getInstance().lobbyHandler.lobbyCoords)
            if (playerX >= lobbyCoord[0][0] && playerX <= lobbyCoord[0][1])
                if (playerZ >= lobbyCoord[1][0] && playerZ <= lobbyCoord[1][1])
                    return true;

        return false;
    }

    public void resetHealth() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (inLobby(player)) {
                player.setHealth(20);
                player.setFoodLevel(20);
            }
        }
    }

    public void hotbarChecker() {
        if (!enabled) {
            return;
        }
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (inLobby(player)) {
                if (player.getInventory().getContents() != lobbyInv) {
                    player.getInventory().setContents(lobbyInv);
                }
            } else {
                if (player.getInventory().getContents() == lobbyInv) {
                    player.getInventory().clear();
                }
            }
        }
    }
}
