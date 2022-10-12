package us.xylight.pvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.ranks.Rank;
import us.xylight.pvp.ranks.RankHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SetRank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        System.out.println(Arrays.toString(args));
        if (!(args.length >= 2)) return false;

        Rank rank = RankHandler.stringToRank(args[1]);
        if (rank == null) return false;

        XyPVP.getInstance().rankHandler.setRank(Objects.requireNonNull(Bukkit.getPlayer(args[0])), rank);


        return true;
    }
}
