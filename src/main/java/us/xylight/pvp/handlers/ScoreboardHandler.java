package us.xylight.pvp.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardHandler {
    public Scoreboard mainScoreboard;

    public void createMainScoreboard() {
        mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective obj;
        if (mainScoreboard.getObjective("Scoreboard") == null) {
            obj = mainScoreboard.registerNewObjective("Scoreboard", Criteria.DUMMY, ChatColor.translateAlternateColorCodes('&', "  ⥠  "));
        } else {
            obj = mainScoreboard.getObjective("Scoreboard");
        }
        assert obj != null;
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void setPlayerScoreboard(Player p, String title, String[] lines) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective("Scoreboard", Criteria.DUMMY, "  ⥠  ");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);


        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        int[] index = {lines.length - 1};
        for (String line : lines) {
            scoreboard.getScores(line).forEach((Score score) -> {
                score.setScore(index[0]);
                index[0]--;
            });
        }
        p.setScoreboard(scoreboard);
    }
}
