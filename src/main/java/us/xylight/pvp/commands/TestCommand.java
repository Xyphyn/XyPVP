package us.xylight.pvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.xylight.pvp.XyPVP;

public class TestCommand implements CommandExecutor {
    Plugin pl;

    public TestCommand(XyPVP plugin) {
        this.pl = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        new BukkitRunnable() {
            @Override
            public void run() {
                sender.sendMessage("hey it worked");
            }
        }.runTaskLater(pl, 20);

        return true;
    }
}
