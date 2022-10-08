package us.xylight.pvp.events;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.games.Game;

import java.util.Arrays;
import java.util.UUID;

public class GeneralEvents implements Listener {
    XyPVP pl;

    public GeneralEvents(Plugin plugin) {
        this.pl = (XyPVP) plugin;

        Bukkit.getPluginManager().registerEvents(this, pl);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
        event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Welcome!"));
        event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        event.getPlayer().setRotation(180, 0);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(event.getPlayer().getWorld().getSpawnLocation());
        event.getPlayer().setRotation(180, 0);
        event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "You died..."));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity().getScoreboardTags().contains("axe")) {
            event.setCancelled(true);
            System.out.println("hmmmmm");
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        for (Game game : pl.queueHandler.games) {
            if (Arrays.asList(game.playerUUIDs).contains(event.getPlayer().getUniqueId())) {
                if (game.players[0].equals(event.getPlayer())) {
                    game.resetOnDeath(game.players[1], game.players[0]);
                } else {
                    game.resetOnDeath(game.players[0], game.players[1]);
                }
                return;
            }
        }

        for (UUID pUUID : pl.queueHandler.queue.keySet()) {
            if (pUUID.equals(event.getPlayer().getUniqueId())) {
                pl.queueHandler.queue.remove(pUUID);
                return;
            }
        }

        pl.ffa.killMap.remove(event.getPlayer());
        pl.ffa.players.remove(event.getPlayer());
    }
}
