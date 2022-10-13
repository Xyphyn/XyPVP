package us.xylight.pvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.handlers.WorldProtect;
import us.xylight.pvp.ranks.RankPermission;

public class WorldProtectToggle implements CommandExecutor {
    public static String cmdName = "worldprotect";
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (XyPVP.getInstance().rankHandler.getRank((Player) sender).permission.getPower() < RankPermission.ADMIN.getPower()) {
            sender.sendMessage("⛃ " + ChatColor.RED + "You do not have permission to execute that command.");
            return true;
        }

        WorldProtect.enabled = !WorldProtect.enabled;
        sender.sendMessage(WorldProtect.enabled ? ChatColor.GREEN + "World protect has been enabled." : ChatColor.RED + "World protect has been disabled." );

        return true;
    }
}
