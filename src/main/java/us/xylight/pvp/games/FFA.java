package us.xylight.pvp.games;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.games.ffakits.abilities.Ability;
import us.xylight.pvp.games.ffakits.FFAKit;
import us.xylight.pvp.handlers.KitType;
import us.xylight.pvp.util.Clamp;

import java.util.*;

public class FFA implements Listener {
    ItemStack[] axeInventory;

    Random rand = new Random();

    public Map<UUID, Integer> killMap = new HashMap<>();
    public ArrayList<Player> players = new ArrayList<>();
    public Map<UUID, Long> cooldowns = new HashMap<>();
    public Map<UUID, Long> bucketCooldowns = new HashMap<>();
    public Map<UUID, Ability> abilities = new HashMap<>();
    public ArrayList<Block> trackedBlocks = new ArrayList<>();
    public Map<UUID, BossBar> bossBars = new HashMap<>();

    public FFA() {
        Bukkit.getPluginManager().registerEvents(this, XyPVP.getInstance());

        axeInventory = new ItemStack[]{
            new ItemStack(Material.IRON_SWORD),
                    new ItemStack(Material.BOW),
                    new ItemStack(Material.ARROW, 12),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                XyPVP.getInstance().menuHandler.getItem(new ItemStack(Material.BARRIER), "&cLeave", "Leave the game.")
        };
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getType().equals(EntityType.TRIDENT)) return;
        event.getEntity().remove();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && players.contains(event.getEntity())) {
            Player p = (Player) event.getEntity();
            cooldowns.put(p.getUniqueId(), System.currentTimeMillis() + 10000);
        }
    }

    @EventHandler
    public void onWaterFlow(BlockFromToEvent event) {
        if (trackedBlocks.contains(event.getBlock())) event.setCancelled(true);
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!players.contains(event.getPlayer())) return;
        if (event.getBucket().equals(Material.WATER_BUCKET)) {
            if (bucketCooldowns.get(event.getPlayer().getUniqueId()) != null) {
                Long cooldown = bucketCooldowns.get(event.getPlayer().getUniqueId());
                if (!(cooldown <= System.currentTimeMillis())) {
                    Player p = event.getPlayer();
                    p.playNote(event.getPlayer().getLocation(), Instrument.BASS_GUITAR, Note.natural(0, Note.Tone.A));
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.format("⛃%s Your bucket is on cooldown! You can use it in %d seconds", ChatColor.RED, ( Math.round((cooldown - System.currentTimeMillis()) / 1000f)))));
                    event.setCancelled(true);
                    return;
                }
            }

            trackedBlocks.add(event.getBlock());
            bucketCooldowns.put(event.getPlayer().getUniqueId(), System.currentTimeMillis() + 10000);

            Bukkit.getScheduler().runTaskLater(XyPVP.getInstance(), task -> {
                if (event.getBlock().getBlockData() instanceof Waterlogged wl) {
                    wl.setWaterlogged(false);
                    trackedBlocks.remove(event.getBlock());

                    event.getBlock().setType(event.getBlock().getType());
                    return;
                }

                event.getBlock().setType(Material.AIR);
                trackedBlocks.remove(event.getBlock());
            }, 20 * 3);

            event.setItemStack(new ItemStack(Material.WATER_BUCKET));


        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (players.contains(event.getPlayer())) {
            if (event.getBlock().getType().equals(Material.BARRIER)) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        try {
            //  || LobbyHandler.inLobby(event.getPlayer())
            if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.hasItem()) {
                if (event.getItem().hasItemMeta()) {
                    if (event.getItem().getItemMeta().hasDisplayName()) {
                        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cLeave")) {
                            if (cooldowns.get(event.getPlayer().getUniqueId()) == null) {
                                event.setCancelled(true);
                                resetOnDeath(event.getPlayer());
                                return;
                            }
                            if (cooldowns.containsKey(event.getPlayer().getUniqueId()) && (cooldowns.get(event.getPlayer().getUniqueId()) <= System.currentTimeMillis())) {
                                event.setCancelled(true);
                                resetOnDeath(event.getPlayer());
                            }
                            else {
                                event.getPlayer().playNote(event.getPlayer().getLocation(), Instrument.BASS_GUITAR, Note.natural(0, Note.Tone.A));
                                event.getPlayer().sendMessage(String.format("⛃%s You are in combat! You can leave in %d seconds", ChatColor.RED, ( Math.round((cooldowns.get(event.getPlayer().getUniqueId()) - System.currentTimeMillis()) / 1000f))));
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) { e.printStackTrace(); }
    }

    public void resetOnDeath(Player p) {
        players.remove(p);
        killMap.remove(p);
        cooldowns.remove(p.getUniqueId());
        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
        if (p.getKiller() != null) {
//            p.getKiller().addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 10));
            p.getKiller().setHealth(Clamp.clamp((int) (p.getKiller().getHealth() + 10), 0, 20));
            p.getKiller().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("‡ " + ChatColor.GREEN + "+1 Kill" + ChatColor.RESET + "   µ" + ChatColor.RED + " +10" + ChatColor.RESET + "   ≮ " + ChatColor.BLUE + killMap.get(p.getKiller().getUniqueId()) + (killMap.get(p.getKiller().getUniqueId()).equals(1) ? " kill" : " kills")));
            p.getKiller().playSound(p.getKiller(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }
        p.teleport(p.getWorld().getSpawnLocation());

        bossBars.get(p.getUniqueId()).removePlayer(p);
        bossBars.remove(p.getUniqueId());
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (players.contains(event.getEntity())) {
            if (event.getEntity().getKiller() != null) {
                killMap.put(event.getEntity().getKiller().getUniqueId(), killMap.get(event.getEntity().getKiller().getUniqueId()) + 1);
                event.getEntity().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(
                                String.format("%s%s You were killed by %s%s with %s%d♥%s remaining!",
                                        "‡", ChatColor.GRAY, event.getEntity().getKiller().getDisplayName(), ChatColor.GRAY, ChatColor.RED, Math.round(event.getEntity().getKiller().getHealth()), ChatColor.GRAY)
                        ));
            }
            event.getDrops().clear();
            resetOnDeath(event.getEntity());
        }
    }

    @EventHandler
    public void onFoodDeplete(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    public void joinFFA(HumanEntity p, KitType kit) {
        if (!(p instanceof Player pl)) return;
        pl.closeInventory();
        pl.sendTitle("⇗", "", 10, 0, 10);
        Bukkit.getScheduler().runTaskLater(XyPVP.getInstance(), () -> addPlayerToFFA(pl, kit), 10);
    }

    public void addPlayerToFFA(Player p, KitType kit) {

        FFAKit ffaKit = kit.newKit(p);

        if (players.contains(p)) {
            resetOnDeath(p);
        }

        Location[] spawnLocations = new Location[] {
                new Location(p.getWorld(), 68, 4, 98, -90, 0),
                new Location(p.getWorld(), 115, 3, 100, 90, 0),
                new Location(p.getWorld(), 177, 4, 98, 90, 0)
        };

       ffaKit.setContents(p);
        p.getInventory().setItem(8, XyPVP.getInstance().menuHandler.getItem(new ItemStack(Material.BARRIER), ChatColor.translateAlternateColorCodes('&', "&cLeave"), "Leave the game."));

        p.teleport(spawnLocations[rand.nextInt(spawnLocations.length)]);

        Ability ability = abilities.get(p.getUniqueId());

        if (abilities.get(p.getUniqueId()) != null) {
            ability = abilities.get(p.getUniqueId());
            p.getActivePotionEffects().removeAll(p.getActivePotionEffects());
            p.addPotionEffect(ability.effect);
            ability.setPenalty(p);
        }

        BossBar bar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', String.format("◦ &bFFA &r ☈ &9%s &r ∯ &d%s",
                ffaKit.name,
                abilities.get(p.getUniqueId()) != null ? ability.getClass().getSimpleName() : "None")
        ), BarColor.YELLOW, BarStyle.SOLID);

        bossBars.put(p.getUniqueId(), bar);

        bar.addPlayer(p);

        killMap.put(p.getUniqueId(), 0);
        players.add(p);
    }
}
