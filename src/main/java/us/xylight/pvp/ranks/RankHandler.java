package us.xylight.pvp.ranks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.util.WrappedScoreboardTeam;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RankHandler {
    Map<Player, Rank> ranks = new HashMap<>();

    public Optional<?> getTeam(PacketContainer packet) {
        Optional<?> team = (Optional<?>) packet.getModifier().withType(Optional.class).read(0);
        if (team.isPresent()) {
            return Optional.of(WrappedScoreboardTeam.fromHandle(team.get()));
        }
        return Optional.empty();
    }


    public void changePrefix(Player p, String name) {
//        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
//        Optional<?> optional = getTeam(packet);
//        WrappedScoreboardTeam scoreboardTeam = WrappedScoreboardTeam.fromHandle(packet.getOptionalStructures().read(0).get());
//        scoreboardTeam.setTeamColor(ChatColor.RED);
//        scoreboardTeam.setDisplayName(WrappedChatComponent.fromText("tester"));
//        scoreboardTeam.setCollisionRule(Team.OptionStatus.ALWAYS);
//        scoreboardTeam.setNameTagVisibility(Team.OptionStatus.ALWAYS);
//
//        scoreboardTeam.setPrefix(WrappedChatComponent.fromText("test"));
//        packet.getOptionalStructures().write(0,  Optional.of((InternalStructure) scoreboardTeam.getHandle()));
//
//        Bukkit.getOnlinePlayers().forEach(pl -> {
//            try {
//                XyPVP.getProtocol().sendServerPacket(pl, packet);
//            } catch (InvocationTargetException e) {
//                throw new RuntimeException(e);
//            }
//        });


//        final StructureModifier<WrappedChatComponent> chats = packet.getChatComponents();
//        System.out.println(chats.size());
//        System.out.println(chats);
//        System.out.println(packet.getStrings());
//        System.out.println(packet.getStructures());
//        // Team Name
//        packet.getStrings().write(0, "heysothisisatest");
//        // Mode
//        packet.getIntegers().write(0, 0);
//        // Team Display Name
//        packet.getChatComponents().write(0, WrappedChatComponent.fromText("hey"));
//        // Flags
//        packet.getIntegers().write(1, 1);
//        // Name Tag Visibility
//        packet.getChatVisibilities().write(0, EnumWrappers.ChatVisibility.FULL);
//        // Collision Rule
//        packet.getChatVisibilities().write(1, EnumWrappers.ChatVisibility.FULL);
//        // Team Color
//        packet.getIntegers().write(2, 15);
//        // Team Prefix
//        packet.getChatComponents().write(1, WrappedChatComponent.fromLegacyText("e - "));
//        // Team Suffix
//        packet.getChatComponents().write(2, WrappedChatComponent.fromLegacyText(""));
//        // Entity Count
//        packet.getIntegers().write(3, 1);
//        // Entities
//        packet.getStringArrays().write(0, new String[] { p.getName() });
    }

    public void setRank(Player player, Rank rank) {
        changePrefix(player, rank.prefix + player.getName());
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
