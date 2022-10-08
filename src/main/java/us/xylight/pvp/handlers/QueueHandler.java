package us.xylight.pvp.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import us.xylight.pvp.games.*;

import java.util.*;

public class QueueHandler {
    public ArrayList<Game> games = new ArrayList<>();
    public Map<UUID, QueueTypes> queue = new HashMap<>();
    Plugin plugin;
    public QueueHandler(Plugin pl) {
        this.plugin = pl;
    }
    public void queue(Player p, QueueTypes type) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&aQueueing %s", type.toString())));

        p.closeInventory();
        checkQueue(type, p);
    }

    public void checkQueue(QueueTypes type, Player queuer) {
        if (queue.containsValue(type)) {
            for (UUID p : queue.keySet()) {
                Player player = Bukkit.getPlayer(p);
                if (player == null) {
                    queue.remove(p);
                    return;
                }
                if (queue.get(p).equals(type)) {
                    if (player.equals(queuer)) return;
                    queue.remove(p);
                    queue.remove(queuer.getUniqueId());

                    if (type == QueueTypes.SURVIVAL) {
                        games.add(new Survival(new Player[]{player, queuer}, plugin, new UUID[]{p, queuer.getUniqueId()}));
                    }
                    if (type == QueueTypes.OP) {
                        games.add(new OP(new Player[]{player, queuer}, plugin, new UUID[]{p, queuer.getUniqueId()}));
                    }
                    if (type == QueueTypes.SUMO) {
                        games.add(new Sumo(new Player[]{player, queuer}, plugin, new UUID[]{p, queuer.getUniqueId()}));
                    }
                    if (type == QueueTypes.WALLS) {
                        games.add(new Walls(new Player[]{player, queuer}, plugin, new UUID[]{p, queuer.getUniqueId()}));
                    }
                    if (type == QueueTypes.AXE) {
                        games.add(new Axe(new Player[]{player, queuer}, plugin, new UUID[]{p, queuer.getUniqueId()}));
                    }
                }
            }
        } else {
            queue.put(queuer.getUniqueId(), type);
        }
    }
}

