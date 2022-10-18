package us.xylight.pvp.ranks;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.xylight.pvp.XyPVP;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class RankHandler {
    Map<Player, Rank> ranks = new HashMap<>();
    public void changeName(Player p, String nick) {
        ServerPlayer sp = ((CraftPlayer) p).getHandle();
        String name = nick;
        GameProfile playerProfile = sp.getGameProfile();
        try {
            Field ff = playerProfile.getClass().getDeclaredField("name");
            ff.setAccessible(true);
            ff.set(playerProfile, name);
        } catch (Exception ignored) {

        }

        Bukkit.getOnlinePlayers().forEach(pl -> {
            ServerPlayerConnection connection = ((CraftPlayer) pl).getHandle().connection;
            connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, sp));
            connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, sp));

        });
    }

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
