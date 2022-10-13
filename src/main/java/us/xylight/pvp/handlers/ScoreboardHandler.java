package us.xylight.pvp.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Map;

public class ScoreboardHandler {
    public Scoreboard mainScoreboard;

    public Scoreboard createMainScoreboard() {
        mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective obj;
        if (mainScoreboard.getObjective("Scoreboard") == null) {
            obj = mainScoreboard.registerNewObjective("Scoreboard", Criteria.DUMMY, ChatColor.translateAlternateColorCodes('&', "  ⥠  "));
        } else {
            obj = mainScoreboard.getObjective("Scoreboard");
        }
        assert obj != null;
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        return mainScoreboard;
    }

    public void setMainScoreboard(String[] lines) {
        for (String entry : mainScoreboard.getEntries()) {
            mainScoreboard.resetScores(entry);
        }

        for (String line : lines) {
            mainScoreboard.getScores(line).forEach((Score score) -> {
                score.setScore(0);
            });
        }
    }

    public void setPlayerScoreboard(Player p, String title, String[] lines) {

    }
}
