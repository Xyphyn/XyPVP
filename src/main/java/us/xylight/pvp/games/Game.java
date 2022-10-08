package us.xylight.pvp.games;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class Game implements Listener {
    Random rand = new Random();
    BoundingBox arenaArea;
    ItemStack[] gameInventory = new ItemStack[] {};
    public Player[] players;
    public UUID[] playerUUIDs;
    public ArrayList<Block> trackedBlocks = new ArrayList<>();
    XyPVP plugin;
    int x;
    int y;
    int z;
    boolean gameStarted;

    public Game(Player[] plyers, Plugin pl, UUID[] playerUUIDs) {
        Bukkit.getPluginManager().registerEvents(this, pl);
        this.plugin = (XyPVP) pl;
        this.playerUUIDs = playerUUIDs;

        x = rand.nextInt(100000);
        y = 175;
        z = rand.nextInt(100000);

        arenaArea = new BoundingBox(x, y, z, x + 50, y + 10, z + 25);
        this.players = plyers;
        World world = players[0].getWorld();
        buildArena(x, y, z);
        teleportPlayers(world, x, y, z);
        new BukkitRunnable() {
            int timer = 4;

            @Override
            public void run() {
                timer -= 1;
                for (Player player : players) {
                    player.sendTitle(timer == 0 ? ChatColor.GREEN + "Go!" : String.format((ChatColor.YELLOW + "%d"), timer), "", 0, 15, 5);
                    player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(timer == 0 ? 2 : 1, Note.Tone.F));
                }
                if (timer == 0) {
                    gameStarted = true;
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(pl, 0, 20);
    }

    public void teleportPlayers(World world, int x, int y, int z) {

        int index = 0;
        for (Player player : players) {
            Location loc;
            loc = new Location(world, x + (index % 2 == 0 ? 5 : 45), y + 2, z + 12.5, (index % 2 == 0 ? -90f : 90f), 0f);
            player.teleport(loc);
            player.getInventory().setContents(gameInventory);
            index++;
        }

    }

    public void resetOnDeath(Player winner, Player loser) {
        winner.sendMessage("You won!");
        loser.sendMessage("You lost!");
        winner.teleport(winner.getWorld().getSpawnLocation());
        loser.teleport(winner.getWorld().getSpawnLocation());
        clearArena();

        plugin.queueHandler.games.removeIf(game -> game.equals(this));

        PlayerDeathEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        EntityDamageEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);
        ProjectileHitEvent.getHandlerList().unregister(this);
        trackedBlocks.clear();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (players.length <= 2) {
            if (event.getEntity().equals(players[0]))
                resetOnDeath(players[1], players[0]);
            else
                resetOnDeath(players[0], players[1]);

        } else {

        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {}

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(!trackedBlocks.contains(event.getBlock()));
    }

    public boolean containsCurrentPlayers(UUID[] pUUIDs) {
        for (Game game : plugin.queueHandler.games) {
            if (Arrays.equals(game.playerUUIDs, pUUIDs)) {
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!gameStarted) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (Arrays.asList(playerUUIDs).contains(event.getPlayer().getUniqueId()) && containsCurrentPlayers(playerUUIDs)) {
            trackedBlocks.add(event.getBlock());
        }
    }

    public void clearArena() {
        Vector max = arenaArea.getMax();
        Vector min = arenaArea.getMin();
        World world = players[0].getWorld();

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
        World world = players[0].getWorld();

        // Ground
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= min.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    new PlaceableBlock(world.getUID(), x, y, z, Material.GRASS_BLOCK).compute();
                }
            }
        }

        // Back
        for (int x = min.getBlockX(); x <= min.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    new PlaceableBlock(world.getUID(), x, y, z, Material.GLASS).compute();
                }
            }
        }

        // Front
        for (int x = max.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    new PlaceableBlock(world.getUID(), x, y, z, Material.GLASS).compute();
                }
            }
        }

        // Left
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= min.getBlockZ(); z++) {
                    new PlaceableBlock(world.getUID(), x, y, z, Material.GLASS).compute();
                }
            }
        }

        // Right
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = max.getBlockZ(); z <= max.getBlockZ(); z++) {
                    new PlaceableBlock(world.getUID(), x, y, z, Material.GLASS).compute();
                }
            }
        }

        // Roof
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = max.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    new PlaceableBlock(world.getUID(), x, y, z, Material.GLASS).compute();
                }
            }
        }
    }
}
