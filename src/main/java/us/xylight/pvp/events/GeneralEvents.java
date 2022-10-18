package us.xylight.pvp.events;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.games.Game;
import us.xylight.pvp.handlers.QueueHandler;
import us.xylight.pvp.ranks.RankHandler;
import us.xylight.pvp.ranks.RankPermission;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralEvents implements Listener {
    private final Pattern hexRegex = Pattern.compile("#[a-fA-F0-9]{6}");
    public final Map<String, String> emoji = new HashMap<>();

    public GeneralEvents() {
        Bukkit.getPluginManager().registerEvents(this, XyPVP.getInstance());

        emoji.put(":)", "☺");
        emoji.put(":happy:", "☺");
        emoji.put(":(", "☹");
        emoji.put(":sad:", "☹");
        emoji.put(":skull:", "♬");
        emoji.put(":heart:", "❤");
        emoji.put("<3", "❤");
        emoji.put(":flushed:", "≪");
        emoji.put(":thinking:", "␑");
        emoji.put(":thumbsup:", "✉");
        emoji.put(":pensive:", "⤯");
        emoji.put(":sweat:", "Ⓡ");
        emoji.put(":joy:", "⥥");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        p.getWorld().setSpawnLocation(new Location(p.getWorld(), 115.5d, 7.0d, 138.7d, 180f, 0f));

        p.teleport(p.getWorld().getSpawnLocation());
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Welcome!"));
        p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        p.setRotation(180, 0);

        XyPVP.getInstance().rankHandler.loadRank(p);
        event.setJoinMessage(event.getPlayer().getDisplayName() + ChatColor.GRAY + " joined.");

        XyPVP.getInstance().npcs.forEach(npc -> npc.showNPC(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null)
            event.setDeathMessage(
                    String.format("%s %s‡ %s",
                            event.getEntity().getKiller().getDisplayName(),
                            ChatColor.RESET,
                            event.getEntity().getDisplayName())
            );
        else if (event.getDeathMessage().equalsIgnoreCase(event.getEntity().getName() + " died")) {
            event.setDeathMessage(String.format("%s %s‡ %s",
                    event.getEntity().getDisplayName(),
                    ChatColor.RESET,
                    event.getEntity().getDisplayName()));
        }
    }

    public String replace(String m, String key, String value) {
        return m.replace(key, value);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        RankHandler handler = XyPVP.getInstance().rankHandler;
        String message = event.getMessage();
        Matcher matcher = hexRegex.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            if (!color.equals("") && !(XyPVP.getInstance().rankHandler.getRank(event.getPlayer()).permission.getPower() >= RankPermission.VIP.getPower())) {
                event.getPlayer().sendMessage(ChatColor.GRAY + "Hex colors are only available to those with VIP permission or higher.");
                break;
            }
            message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            matcher = hexRegex.matcher(message);
        }

        for (Map.Entry<String, String> entry : emoji.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            message = message.replace(key, value);
        }

        String messageFormat = String.format("%s %s%s%s: %s",
                XyPVP.getInstance().rankHandler.getRank(event.getPlayer()).prefix,
                handler.getRank(event.getPlayer()).nameColor,
                event.getPlayer().getName(),
                handler.getRank(event.getPlayer()).chatColor,
                message);

        event.setFormat(messageFormat);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.getPlayer().sendTitle("₅", "", 0, 20, 20);
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
        for (Game game : QueueHandler.getGames()) {
            if (game.players.contains(event.getPlayer())) {
                game.checkPlayerDeath();
                return;
            }
        }

        QueueHandler.removeFromQueue(event.getPlayer());

        event.setQuitMessage(event.getPlayer().getDisplayName() + ChatColor.GRAY + " left.");

        XyPVP.getInstance().ffa.killMap.remove(event.getPlayer().getUniqueId());
        XyPVP.getInstance().ffa.players.remove(event.getPlayer());

        XyPVP.getInstance().rankHandler.saveRank(event.getPlayer());
    }
}
