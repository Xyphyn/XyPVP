package us.xylight.pvp.games;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.games.ffakits.FFAKit;
import us.xylight.pvp.util.Clamp;

import java.util.*;

public class FFA implements Listener {

    public XyPVP plugin;
    ItemStack[] axeInventory;

    Random rand = new Random();

    public Map<Player, Integer> killMap = new HashMap<>();
    public ArrayList<Player> players = new ArrayList<>();
    public Map<UUID, Long> cooldowns = new HashMap<>();

    public FFA(Plugin p) {
        this.plugin = (XyPVP) p;
        Bukkit.getPluginManager().registerEvents(this, plugin);

        axeInventory = new ItemStack[]{
            new ItemStack(Material.IRON_SWORD),
                    new ItemStack(Material.BOW),
                    new ItemStack(Material.ARROW, 12),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.AIR),
                    plugin.menuHandler.getItem(new ItemStack(Material.BARRIER), "&cLeave", "Leave the game.")
        };
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        event.getEntity().remove();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            cooldowns.put(p.getUniqueId(), System.currentTimeMillis() + 10000);
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
                                event.getPlayer().sendMessage(String.format("%s[!] You are in combat! You can leave in %d seconds", ChatColor.RED, ( Math.round((cooldowns.get(event.getPlayer().getUniqueId()) - System.currentTimeMillis()) / 1000f))));
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
        if (p.getKiller() != null) {
//            p.getKiller().addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 10));
            p.getKiller().setHealth(Clamp.clamp((int) (p.getKiller().getHealth() + 10), 0, 20));
            p.getKiller().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "+1 Kill" + ChatColor.RESET + " | " + ChatColor.RED + "❤ +10"));
            p.getKiller().playSound(p.getKiller(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }
        p.teleport(p.getWorld().getSpawnLocation());
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (players.contains(event.getEntity())) {
            event.getDrops().clear();
            resetOnDeath(event.getEntity());
        }
    }

    public void joinFFA(Player p, FFAKit kit) {

        Location[] spawnLocations = new Location[] {
                new Location(p.getWorld(), 67, 4, 98),
                new Location(p.getWorld(), 115, 3, 100),
                new Location(p.getWorld(), 178, 4, 98)
        };

        kit.setContents(p);
        p.getInventory().setItem(8, plugin.menuHandler.getItem(new ItemStack(Material.BARRIER), ChatColor.translateAlternateColorCodes('&', "&cLeave"), "Leave the game."));

        p.teleport(spawnLocations[rand.nextInt(spawnLocations.length)]);

        killMap.put(p, 0);
        players.add(p);
    }
}
