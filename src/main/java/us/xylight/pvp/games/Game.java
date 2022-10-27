package us.xylight.pvp.games;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.handlers.QueueHandler;
import us.xylight.pvp.util.PlaceableBlock;
import us.xylight.pvp.util.PlayerUtils;
import us.xylight.pvp.util.WorkloadRunnable;

import java.util.*;

public class Game implements Listener {
    Random rand = new Random();
    BoundingBox arenaArea;
    ItemStack[] gameInventory = new ItemStack[] { };
    public ArrayList<Player> players;
    public ArrayList<Block> trackedBlocks = new ArrayList<>();
    int x, y, z;
    boolean gameStarted;
    boolean finished;
    public Map<Integer, ArrayList<Player>> teams = new HashMap<>();

    public Game(ArrayList<Player> plyers) {
        Bukkit.getPluginManager().registerEvents(this, XyPVP.getInstance());
        this.players = plyers;

        x = rand.nextInt(100000);
        y = 175;
        z = rand.nextInt(100000);

        arenaArea = new BoundingBox(x, y, z, x + 50, y + 10, z + 25);
        World world = players.get(0).getWorld();
        buildArena(x, y, z);
        teleportPlayers(world, x, y, z);

        teams.put(0, new ArrayList<>());
        teams.put(1, new ArrayList<>());

        final int[] index = {0};
        plyers.forEach(p -> {
            teams.get(index[0] % 2).add(p);
            index[0]++;
        });

        int[] timer = {4};

        Bukkit.getScheduler().runTaskTimer(XyPVP.getInstance(), task -> {
            timer[0] -= 1;
            for (Player player : players) {
                player.sendTitle(timer[0] == 0 ? ChatColor.GREEN + "Go!" : String.format((ChatColor.YELLOW + "%d"), timer[0]), "", 0, 15, 5);
                player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(timer[0] == 0 ? 2 : 1, Note.Tone.F));
            }
            if (timer[0] == 0) {
                gameStarted = true;
                task.cancel();
            }
        }, 20, 20);
    }

    public void unregisterEvents() {

        PlayerDeathEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        EntityDamageEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);
        ProjectileHitEvent.getHandlerList().unregister(this);

    }

    public void teleportPlayers(World world, int x, int y, int z) {
        int index = 0;
        for (Player player : players) {
            Location loc;
            loc = new Location(world, x + (index % 2 == 0 ? 5 : 45), y + 2, z + 12.5, (index % 2 == 0 ? -90f : 90f), 0f);

            player.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 2, loc.getBlockZ()).setType(Material.BARRIER);

            player.teleport(loc);
            player.getInventory().setContents(gameInventory);
            index++;
        }

    }

    public void resetOnDeath(ArrayList<Player> winners, ArrayList<Player> losers) {
        winners.forEach(winner -> {
            winner.teleport(winner.getWorld().getSpawnLocation());
        });
        losers.forEach(loser -> {
            loser.teleport(loser.getWorld().getSpawnLocation());
        });
        clearArena();

        QueueHandler.removeGame(this);
        trackedBlocks.clear();
        unregisterEvents();
    }

    public void showVictory(Player p) {
        p.sendTitle(String.format("⟿ %sVictory!", ChatColor.GOLD), "", 3, 35, 5);
        p.sendMessage(ChatColor.GREEN + "You won!");
        p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

        Bukkit.getScheduler().runTaskLater(XyPVP.getInstance(), () -> {
            p.sendTitle("⇗", "", 20, 0, 15);
        }, 43);
    }

    public void checkPlayerDeath() {
        if (finished) return;
        ArrayList<Player> team1 = teams.get(0);
        ArrayList<Player> team2 = teams.get(1);

        if (team1.stream().allMatch(Entity::isDead)) {
            team2.forEach(this::showVictory);
            finished = true;

            Bukkit.getScheduler().runTaskLater(XyPVP.getInstance(), () -> {
                resetOnDeath(team2, team1);
            }, 20 * 3);
            team1.forEach(PlayerUtils::removeRedVignette);
            team2.forEach(PlayerUtils::removeRedVignette);


        } else if (team2.stream().allMatch(Entity::isDead)) {
            team1.forEach(this::showVictory);
            finished = true;

            Bukkit.getScheduler().runTaskLater(XyPVP.getInstance(), () -> {
                resetOnDeath(team1, team2);
            }, 20 * 3);
            team1.forEach(PlayerUtils::removeRedVignette);
            team2.forEach(PlayerUtils::removeRedVignette);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        checkPlayerDeath();



        //        if (!died) died = true;
//        else return;

//        if (players.size() <= 2) {
//            Player winner;
//            Player loser;
//            if (event.getEntity().equals(players.get(0))) {
//                winner = players.get(1);
//                loser = players.get(0);
//            } else {
//                winner = players.get(0);
//                loser = players.get(1);
//            }
//            showVictory(winner);
//
//            Bukkit.getScheduler().runTaskLater(XyPVP.getInstance(), () -> {
//                resetOnDeath(winner, loser);
//            }, 20 * 3);
//        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {}

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(!trackedBlocks.contains(event.getBlock()));
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!gameStarted) event.setCancelled(true);
        if (finished) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (players.contains(event.getPlayer())) {
            trackedBlocks.add(event.getBlock());
        }
    }

    public void clearArena() {
        Vector max = arenaArea.getMax();
        Vector min = arenaArea.getMin();
        World world = players.get(0).getWorld();

        // Ground
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    new PlaceableBlock(world.getUID(), x, y, z, Material.AIR).compute();
                }
            }
        }
    }

    public void buildArena(int passedX, int passedY, int passedZ) {


        Vector max = arenaArea.getMax();
        Vector min = arenaArea.getMin();
        World world = players.get(0).getWorld();
        WorkloadRunnable r = XyPVP.getInstance().workloadRunnable;
        // Ground
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= min.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    r.addWorkload(new PlaceableBlock(world.getUID(), x, y, z, Material.GRASS_BLOCK));
                }
            }
        }

        // Back
        for (int x = min.getBlockX(); x <= min.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    r.addWorkload(new PlaceableBlock(world.getUID(), x, y, z, Material.GLASS));
                }
            }
        }

        // Front
        for (int x = max.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    r.addWorkload(new PlaceableBlock(world.getUID(), x, y, z, Material.GLASS));
                }
            }
        }

        // Left
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= min.getBlockZ(); z++) {
                    r.addWorkload(new PlaceableBlock(world.getUID(), x, y, z, Material.GLASS));
                }
            }
        }

        // Right
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = max.getBlockZ(); z <= max.getBlockZ(); z++) {
                    r.addWorkload(new PlaceableBlock(world.getUID(), x, y, z, Material.GLASS));
                }
            }
        }

        // Roof
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = max.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    r.addWorkload(new PlaceableBlock(world.getUID(), x, y, z, Material.GLASS));
                }
            }
        }
    }
}
