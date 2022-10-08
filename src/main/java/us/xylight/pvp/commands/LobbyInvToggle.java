package us.xylight.pvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.handlers.LobbyHandler;

public class LobbyInvToggle implements CommandExecutor {
    XyPVP pl;
    public LobbyInvToggle(XyPVP plugin) {
        this.pl = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        pl.lobbyHandler.enabled = !pl.lobbyHandler.enabled;
        sender.sendMessage(pl.lobbyHandler.enabled ? ChatColor.GREEN + "Lobby inventory has been enabled." : ChatColor.RED + "Lobby inventory has been disabled." );

        return true;
    }
}
