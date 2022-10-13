package us.xylight.pvp.ranks;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.xylight.pvp.XyPVP;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum Rank {
    // de2e39
    NONE("⮭", ChatColor.of("#AAAAAA"), ChatColor.of("#AAAAAA"), RankPermission.DEFAULT),
    BETA("∈", ChatColor.of("#44cc4d"), ChatColor.WHITE, RankPermission.DEFAULT),
    MODERATOR("➻", ChatColor.of("#4974eb"), ChatColor.WHITE, RankPermission.MODERATOR),
    SPOOKY("♜", ChatColor.of("#db5c2d"), ChatColor.of("#db5c2d"), RankPermission.MODERATOR),
    ADMIN("⬬", ChatColor.of("#de2e39"), ChatColor.WHITE, RankPermission.ADMIN),
    OWNER("⚿", ChatColor.of("#ce3dea"), ChatColor.WHITE, RankPermission.OWNER);

    public final String prefix;
    public final ChatColor nameColor;
    public final ChatColor chatColor;
    public final RankPermission permission;

    Rank(String prefix, ChatColor color, ChatColor chatColor, RankPermission power) {
        this.prefix = prefix;
        this.nameColor = color;
        this.chatColor = chatColor;
        this.permission = power;
    }
}

