package us.xylight.pvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.ranks.Rank;
import us.xylight.pvp.ranks.RankHandler;
import us.xylight.pvp.ranks.RankPermission;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SetRank implements CommandExecutor {
    boolean checkRank(CommandSender sender) {
        if (XyPVP.getInstance().rankHandler.getRank((Player) sender).permission.getPower() < RankPermission.ADMIN.getPower()) {
            sender.sendMessage("⛃ " + ChatColor.RED + "You do not have permission to execute that command.");
            return true;
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.getName().equalsIgnoreCase("Xylight")) {
            if (!checkRank(sender)) {
                return false;
            }
        }

        if (!(args.length >= 2)) return false;

        Rank rank = RankHandler.stringToRank(args[1]);
        if (rank == null) return false;

        XyPVP.getInstance().rankHandler.setRank(Objects.requireNonNull(Bukkit.getPlayer(args[0])), rank);


        return true;
    }
}
