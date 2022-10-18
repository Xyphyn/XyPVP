package us.xylight.pvp.handlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.xylight.pvp.games.*;

import java.util.*;

public class QueueHandler {
    private static ArrayList<Game> games = new ArrayList<>();
    private static Map<QueueTypes, Deque<Player>> queue = new HashMap<>();

    public QueueHandler() {

    }
    public void queue(Player p, QueueTypes type) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&aQueueing %s", type.toString().toLowerCase())));

        p.closeInventory();

        if (queue.get(type) == null) {
            queue.put(type, new ArrayDeque<>());
        }
        removeFromQueue(p);
        queue.get(type).add(p);

        checkQueue(type, p);
    }

    public static ArrayList<Game> getGames() {
        return games;
    }

    public static void setGames(ArrayList<Game> passedGames) {
        games = passedGames;
    }

    public static void removeGame(Game game) {
        games.remove(game);
    }

    public static Map<QueueTypes, Deque<Player>> getQueue() {
        return queue;
    }
    public boolean inQueue(Player p) {
        for (Deque<Player> deque : queue.values()) {
            if (deque.contains(p)) return true;
        }
        return false;
    }

    public static void removeFromQueue(Player p) {
        for (Deque<Player> deque : queue.values()) {
            deque.remove(p);
        }
    }

    public void checkQueue(QueueTypes type, Player queuer) {
        Deque<Player> deque = queue.get(type);
        if (deque.size() >= type.maxPlayers) {
            type.newGame(new ArrayList<>(deque));
            deque.clear();
        }
        queue.put(type, deque);
    }
}

