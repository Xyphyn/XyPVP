package us.xylight.pvp;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import us.xylight.pvp.commands.*;
import us.xylight.pvp.enums.MenuType;
import us.xylight.pvp.events.GeneralEvents;
import us.xylight.pvp.games.FFA;
import us.xylight.pvp.handlers.*;
import us.xylight.pvp.listeners.MenuListener;
import us.xylight.pvp.npc.ClickableNPC;
import us.xylight.pvp.npc.NPCSkin;
import us.xylight.pvp.ranks.Rank;
import us.xylight.pvp.ranks.RankHandler;
import us.xylight.pvp.ranks.RankPermission;
import us.xylight.pvp.story.Bossfight;
import us.xylight.pvp.util.ConfigUtil;
import us.xylight.pvp.util.WorkloadRunnable;

import java.util.ArrayList;

import static us.xylight.pvp.util.Colorizer.colorize;

public final class XyPVP extends JavaPlugin {
    public QueueHandler queueHandler;
    public LobbyHandler lobbyHandler;
    public MenuHandler menuHandler;
    public FFA ffa;
    public ScoreboardHandler scoreboardHandler;
    private static XyPVP instance;
    public Rank rank = Rank.NONE;
    public RankHandler rankHandler = new RankHandler();
    public ConfigUtil config;
    public WorkloadRunnable workloadRunnable;
    public ArrayList<ClickableNPC> npcs = new ArrayList<>();
    public ArrayList<Bossfight> fights = new ArrayList<>();
    private static ProtocolManager protocolManager;

    public static XyPVP getInstance() {
        return XyPVP.instance;
    }

    @Override
    public void onEnable() {
        workloadRunnable = new WorkloadRunnable();

        Bukkit.getScheduler().runTaskTimer(this, () -> workloadRunnable.run(), 0, 1);

        XyPVP.instance = this;

        // Plugin startup logic
        Bukkit.getLogger().info(ChatColor.GREEN + "[XyPVP] Plugin has enabled successfully");

        config = new ConfigUtil("test.yml");

        new WorldProtect(this);
        lobbyHandler = new LobbyHandler();

        getCommand("worldprotect").setExecutor(new WorldProtectToggle());
        getCommand("test").setExecutor(new TestCommand(this));
        getCommand("lobbyinvtoggle").setExecutor(new LobbyInvToggle(this));
        getCommand("setrank").setExecutor(new SetRank());
        getCommand("crash").setExecutor(new Crash());
        getCommand("fallbackfont").setExecutor(new FallbackFont());

        menuHandler = new MenuHandler();
        queueHandler = new QueueHandler();
        new GeneralEvents();
        ffa = new FFA();
        scoreboardHandler = new ScoreboardHandler();
        scoreboardHandler.createMainScoreboard();

        Bukkit.getScheduler().runTaskTimer(this, task -> {
            int players = Bukkit.getServer().getOnlinePlayers().size();
            Bukkit.getServer().getOnlinePlayers().forEach(p -> scoreboardHandler.setPlayerScoreboard(p, "", new String[]{
                    "  ",
                    String.format(" %s%sProfile ", ChatColor.LIGHT_PURPLE, ChatColor.BOLD),
                    String.format("   |%s   Name%s: %s ", ChatColor.LIGHT_PURPLE, ChatColor.WHITE, p.getName()),
                    " ",
                    String.format(" %s%sStats ", ChatColor.LIGHT_PURPLE, ChatColor.BOLD),
                    String.format("   |%s   Players%s: %d ", ChatColor.LIGHT_PURPLE, ChatColor.WHITE, players),
                    String.format("   |%s   Ping%s: %d ", ChatColor.LIGHT_PURPLE, ChatColor.WHITE, p.getPing()),
                    "    ",
                    "     "
            }));
        }, 0, 20 * 2);

        Bukkit.getOnlinePlayers().forEach(rankHandler::loadRank);

        protocolManager = ProtocolLibrary.getProtocolManager();

        npcs.add(new ClickableNPC(new Location(Bukkit.getWorld("world"), 115.5, 10, 125),
                colorize("&b&lDuels"),
                event -> Bukkit.getScheduler().runTask(this, () -> menuHandler.openMenu(event.getPlayer(), MenuType.MULTIPLAYER)), this, NPCSkin.ASTRONAUT));

        npcs.add(new ClickableNPC(new Location(Bukkit.getWorld("world"), 112.5, 10, 125),
                colorize("&b&lFFA"),
                event -> Bukkit.getScheduler().runTask(this, () -> menuHandler.openMenu(event.getPlayer(), MenuType.FFA)), this, NPCSkin.ARMORED_STEVE));

        ItemStack[] items = {
                new ItemStack(Material.DIAMOND_SWORD),
                new ItemStack(Material.GOLDEN_APPLE, 3),
                new ItemStack(Material.BOW),
                new ItemStack(Material.COBBLESTONE, 64),
                new ItemStack(Material.ARROW, 64)
        };

        ItemStack[] armorItems = {
                new ItemStack(Material.NETHERITE_BOOTS),
                new ItemStack(Material.NETHERITE_LEGGINGS),
                new ItemStack(Material.NETHERITE_CHESTPLATE),
                new ItemStack(Material.NETHERITE_HELMET)
        };

        npcs.add(new ClickableNPC(new Location(Bukkit.getWorld("world"), 118.5, 10, 125),
                colorize(" "),
                event -> Bukkit.getScheduler().runTask(this, () -> {
                    if (rankHandler.getRank(event.getPlayer()).permission.getPower() < RankPermission.VIP.getPower()) return;
                    fights.add(new Bossfight(event.getPlayer(), items, armorItems, EntityType.WITHER));
                }), this, NPCSkin.QUESTION));

        new MenuListener();

//        Scoreboard board = scoreboardHandler.mainScoreboard;
//        Team team = board.getTeam("test");
//        if (team == null)
//            team = board.registerNewTeam("test");
//        team.setPrefix("the - ");
//        team.addPlayer(Objects.requireNonNull(Bukkit.getPlayer("Xylight")));
//        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);

        if (config.getConfig().get("lobbyCoords") == null)
            config.getConfig().set("lobbyCoords", lobbyHandler.lobbyCoords);
            //            lobbyHandler.lobbyCoords = config.getConfig().getObject("lobbyCoords", lobbyHandler.lobbyCoords.getClass());
            //            System.out.println(lobbyHandler.lobbyCoords);
            //            config.save();


    }

    public static ProtocolManager getProtocol() {
        return protocolManager;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info(ChatColor.LIGHT_PURPLE + "[XyPVP] Plugin shutting down");
        Bukkit.getOnlinePlayers().forEach(p -> npcs.forEach(npc -> npc.hideNPC(p)));
        protocolManager.removePacketListeners(this);
        config.save();
    }
}
