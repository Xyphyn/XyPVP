package us.xylight.pvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FallbackFont implements CommandExecutor {
    public static HashMap<Player, Boolean> enabled = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        enabled.putIfAbsent(p, false);
        enabled.put(p, !enabled.get(p));
        Boolean en = enabled.get(p);
        p.sendMessage(en ? ChatColor.GREEN + "[i] Enabling fallback font" : ChatColor.RED + "[i] Disabling fallback font");

        return true;
    }
}
