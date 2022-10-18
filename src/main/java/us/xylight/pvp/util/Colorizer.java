package us.xylight.pvp.util;

import org.bukkit.ChatColor;

public class Colorizer {
    public static String colorize(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }
}
