package us.xylight.pvp.story;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.util.PlaceableBlock;
import us.xylight.pvp.util.PlayerUtils;

import java.util.Random;

import static us.xylight.pvp.util.Colorizer.colorize;

public class Bossfight implements Listener {
    private final int x, y, z;
    public Player player;
    private BoundingBox arenaArea;
    private ItemStack[] gameInventory;
    public Boss boss;
    private EntityType bossType;
    private boolean started;

    public Bossfight(Player p, ItemStack[] inv, ItemStack[] armorItems, EntityType bossType) {
        this.player = p;
        this.gameInventory = inv;
        this.bossType = bossType;

        Bukkit.getPluginManager().registerEvents(this, XyPVP.getInstance());
        Random random = new Random();

        x = random.nextInt(100000);
        y = 175;
        z = random.nextInt(100000);

        arenaArea = new BoundingBox(x, y, z, x + 50, y + 6, z + 25);
        buildArena(x, y, z);

        Location loc = new Location(player.getWorld(), x + 5, y + 2, z + 12.5, -90f, 0f);
        player.teleport(loc);
        player.getInventory().setContents(gameInventory);
        player.getInventory().setArmorContents(armorItems);

        p.playSound(loc, "xypvp.boss", 0.5f, 1.0f);

        this.boss = new Boss(bossType, new Location(player.getWorld(), x + 45, y + 2, z + 12.5, 90f, 0f));
        boss.setAiEnabled(false);
        p.sendTitle(colorize("&b&lReady..."), "", 0, 65, 0);

        Bukkit.getScheduler().runTaskLater(XyPVP.getInstance(), () -> {
            started = true;
            boss.setAiEnabled(true);
            p.sendTitle(colorize("&b&cFight!"), "", 0, 15, 5);
        }, 60);
    }

    public void unregisterEvents() {
        EntityDeathEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        EntityDamageEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);
        ProjectileHitEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p.equals(player) && !started) e.setCancelled(true);
    }

    public void resetOnDeath() {
        clearArena();
        if (!boss.entity.isDead()) boss.entity.remove();

        unregisterEvents();
        player.stopAllSounds();
        player.teleport(player.getWorld().getSpawnLocation());
        XyPVP.getInstance().fights.remove(this);
        PlayerUtils.removeRedVignette(player);
    }

    public void clearArena() {
        Vector max = arenaArea.getMax();
        Vector min = arenaArea.getMin();
        World world = player.getWorld();

        // Ground
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    new PlaceableBlock(world.getUID(), x, y, z, Material.AIR).compute();
                }
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        Entity deadEntity = event.getEntity();
        if (!(deadEntity.equals(player) || deadEntity.equals(boss.entity))) return;
        if (deadEntity.equals(player)) {
            player.playSound(event.getEntity().getLocation(), "xypvp.death", 1.0f, 1.0f);
            player.sendMessage("you died nice");
        }
        if (deadEntity.equals(boss.entity)) {
            player.sendMessage("you won nice");
        }
        resetOnDeath();
    }

    public void buildArena(int passedX, int passedY, int passedZ) {
        Vector max = arenaArea.getMax();
        Vector min = arenaArea.getMin();
        World world = player.getWorld();
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