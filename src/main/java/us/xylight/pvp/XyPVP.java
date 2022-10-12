package us.xylight.pvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.xylight.pvp.commands.LobbyInvToggle;
import us.xylight.pvp.commands.SetRank;
import us.xylight.pvp.commands.TestCommand;
import us.xylight.pvp.commands.WorldProtectToggle;
import us.xylight.pvp.events.GeneralEvents;
import us.xylight.pvp.games.FFA;
import us.xylight.pvp.handlers.*;
import us.xylight.pvp.menus.MenuItem;
import us.xylight.pvp.ranks.Rank;
import us.xylight.pvp.ranks.RankHandler;
import us.xylight.pvp.util.ConfigUtil;

public final class XyPVP extends JavaPlugin {
//    CommandExecutor[] commands = { new WorldProtectToggle() };
    public QueueHandler queueHandler;
    public LobbyHandler lobbyHandler;
    public MenuHandler menuHandler;
    public FFA ffa;
    public ScoreboardHandler scoreboardHandler;

    private static XyPVP instance;
    public Rank rank = Rank.NONE;
    public RankHandler rankHandler = new RankHandler();

    public ConfigUtil config;

    public static XyPVP getInstance() {
        return XyPVP.instance;
    }

    @Override
    public void onEnable() {
        XyPVP.instance = this;
        // Plugin startup logic
        Bukkit.getLogger().info(ChatColor.GREEN + "[XyPVP] Plugin has enabled successfully");

        config = new ConfigUtil("test.yml");

        WorldProtect worldProtect = new WorldProtect(this);
//        for (CommandExecutor cmd : commands) {

//        }
        lobbyHandler = new LobbyHandler();
        getCommand("worldprotect").setExecutor(new WorldProtectToggle());
        getCommand("test").setExecutor(new TestCommand(this));
        getCommand("lobbyinvtoggle").setExecutor(new LobbyInvToggle(this));
        getCommand("setrank").setExecutor(new SetRank());

        menuHandler = new MenuHandler();
        queueHandler = new QueueHandler();
        new GeneralEvents();
        ffa = new FFA();
        scoreboardHandler = new ScoreboardHandler();
        scoreboardHandler.createMainScoreboard();

        Bukkit.getScheduler().runTaskTimer(this, task -> {
            scoreboardHandler.setMainScoreboard(new String[]{String.format(ChatColor.translateAlternateColorCodes('&',  "&b&lPlayers&r: %d"), Bukkit.getServer().getOnlinePlayers().size())});
        }, 0, 20 * 2);

        Bukkit.getOnlinePlayers().forEach(rankHandler::loadRank);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info(ChatColor.LIGHT_PURPLE + "[XyPVP] Plugin shutting down");
    }
}
