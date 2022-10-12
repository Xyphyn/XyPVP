package us.xylight.pvp.ranks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.xylight.pvp.XyPVP;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum Rank {
    NONE("⮭", ChatColor.GRAY, ChatColor.GRAY),
    BETA("∈", ChatColor.GREEN, ChatColor.WHITE),
    SPOOKY("♜", ChatColor.GOLD, ChatColor.GOLD),
    OWNER("⚿", ChatColor.LIGHT_PURPLE, ChatColor.WHITE);

    public final String prefix;
    public final ChatColor nameColor;
    public final ChatColor chatColor;

    Rank(String prefix, ChatColor color, ChatColor chatColor) {
        this.prefix = prefix;
        this.nameColor = color;
        this.chatColor = chatColor;
    }
}

