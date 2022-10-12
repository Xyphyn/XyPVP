package us.xylight.pvp.ranks;

import org.bukkit.entity.Player;
import us.xylight.pvp.XyPVP;

import java.util.HashMap;
import java.util.Map;

public class RankHandler {
    Map<Player, Rank> ranks = new HashMap<>();

    public void setRank(Player player, Rank rank) {


        player.setDisplayName(rank.prefix + " " + rank.nameColor + player.getName());
        player.setPlayerListName(rank.prefix + " " + rank.nameColor + player.getName());
        ranks.put(player, rank);
    }

    public Rank getRank(Player player) {
        return ranks.get(player);
    }

    public static Rank stringToRank(String rankString) {
        for (Rank r : Rank.values()) {
            if (r.toString().equalsIgnoreCase(rankString)) {
                return r;
            }
        }
        return null;
    }

    public void loadRank(Player player) {
        Object rankString = XyPVP.getInstance().config.getConfig().get(player.getUniqueId().toString());
        Rank rank = (rankString == null) ? Rank.NONE : stringToRank(rankString.toString());
        assert rank != null;
        player.setDisplayName(rank.prefix + " " + rank.nameColor + player.getName());
        player.setPlayerListName(rank.prefix + " " + rank.nameColor + player.getName());
        ranks.put(player, rank);
    }

    public void saveRank(Player player) {
        try {
            XyPVP.getInstance().config.getConfig().set(player.getUniqueId().toString(), ranks.get(player).toString());
            ranks.remove(player);
            XyPVP.getInstance().config.save();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
