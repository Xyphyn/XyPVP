package us.xylight.pvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.xylight.pvp.commands.LobbyInvToggle;
import us.xylight.pvp.commands.TestCommand;
import us.xylight.pvp.commands.WorldProtectToggle;
import us.xylight.pvp.events.GeneralEvents;
import us.xylight.pvp.games.FFA;
import us.xylight.pvp.handlers.*;
import us.xylight.pvp.menus.MenuItem;

public final class XyPVP extends JavaPlugin {
//    CommandExecutor[] commands = { new WorldProtectToggle() };
    public QueueHandler queueHandler;
    public XyPVP INSTANCE = this;
    public LobbyHandler lobbyHandler;
    public MenuHandler menuHandler;
    public FFA ffa;
    public ScoreboardHandler scoreboardHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info(ChatColor.GREEN + "[XyPVP] Plugin has enabled successfully");

        WorldProtect worldProtect = new WorldProtect(this);
//        for (CommandExecutor cmd : commands) {

//        }
        lobbyHandler = new LobbyHandler(this);
        getCommand("worldprotect").setExecutor(new WorldProtectToggle());
        getCommand("test").setExecutor(new TestCommand(this));
        getCommand("lobbyinvtoggle").setExecutor(new LobbyInvToggle(this));

        menuHandler = new MenuHandler(this);
        queueHandler = new QueueHandler(this);
        new GeneralEvents(this);
        ffa = new FFA(this);
        scoreboardHandler = new ScoreboardHandler();
        scoreboardHandler.createMainScoreboard();

        new BukkitRunnable() {
            @Override
            public void run() {
                scoreboardHandler.setMainScoreboard(new String[]{String.format(ChatColor.translateAlternateColorCodes('&',  "§b§lPlayers§r: %d"), Bukkit.getServer().getOnlinePlayers().size())});
            }
        }.runTaskTimer(this, 0, 40);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info(ChatColor.LIGHT_PURPLE + "[XyPVP] Plugin shutting down");
    }
}
