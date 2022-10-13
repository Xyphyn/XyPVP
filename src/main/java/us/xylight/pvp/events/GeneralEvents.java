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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.games.Game;
import us.xylight.pvp.ranks.Rank;
import us.xylight.pvp.ranks.RankHandler;

import java.util.Arrays;
import java.util.UUID;

public class GeneralEvents implements Listener {

    public GeneralEvents() {
        Bukkit.getPluginManager().registerEvents(this, XyPVP.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        p.teleport(p.getWorld().getSpawnLocation());
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Welcome!"));
        p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        p.setRotation(180, 0);

        XyPVP.getInstance().rankHandler.loadRank(p);
        event.setJoinMessage(event.getPlayer().getDisplayName() + ChatColor.GRAY + " joined.");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        RankHandler handler = XyPVP.getInstance().rankHandler;
        String messageFormat = String.format("%s %s%s%s: %s",
                XyPVP.getInstance().rankHandler.getRank(event.getPlayer()).prefix,
                handler.getRank(event.getPlayer()).nameColor,
                event.getPlayer().getName(),
                handler.getRank(event.getPlayer()).chatColor,
                event.getMessage());

        event.setFormat(messageFormat);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(event.getPlayer().getWorld().getSpawnLocation());
        event.getPlayer().setRotation(180, 0);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity().getScoreboardTags().contains("axe")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        for (Game game : XyPVP.getInstance().queueHandler.games) {
            if (Arrays.asList(game.playerUUIDs).contains(event.getPlayer().getUniqueId())) {
                if (game.players[0].equals(event.getPlayer())) {
                    game.resetOnDeath(game.players[1], game.players[0]);
                } else {
                    game.resetOnDeath(game.players[0], game.players[1]);
                }
                return;
            }
        }

        for (UUID pUUID : XyPVP.getInstance().queueHandler.queue.keySet()) {
            if (pUUID.equals(event.getPlayer().getUniqueId())) {
                XyPVP.getInstance().queueHandler.queue.remove(pUUID);
                return;
            }
        }

        event.setQuitMessage(event.getPlayer().getDisplayName() + ChatColor.GRAY + " left.");

        XyPVP.getInstance().ffa.killMap.remove(event.getPlayer().getUniqueId());
        XyPVP.getInstance().ffa.players.remove(event.getPlayer());

        XyPVP.getInstance().rankHandler.saveRank(event.getPlayer());
    }
}
