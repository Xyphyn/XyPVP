package us.xylight.pvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import us.xylight.pvp.handlers.WorldProtect;

public class WorldProtectToggle implements CommandExecutor {
    public static String cmdName = "worldprotect";
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        WorldProtect.enabled = !WorldProtect.enabled;
        sender.sendMessage(WorldProtect.enabled ? ChatColor.GREEN + "World protect has been enabled." : ChatColor.RED + "World protect has been disabled." );

        return true;
    }
}
