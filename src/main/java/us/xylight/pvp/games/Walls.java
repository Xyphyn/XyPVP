package us.xylight.pvp.games;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import us.xylight.pvp.util.PlaceableBlock;

import java.awt.*;
import java.util.UUID;

public class Walls extends Game {
    ItemStack enchant(ItemStack item, Enchantment enchantment, int level) {
        item.addEnchantment(enchantment, level);
        return item;
    }

    boolean started = false;

    @Override
    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (started) {
            if (event.getHitBlock() != null && event.getHitBlock().getType().equals(Material.WHITE_WOOL)) {
                event.getHitBlock().setType(Material.AIR);
            }
            event.getEntity().remove();
        }
    }

    public Walls(Player[] plyers, Plugin pl, UUID[] playerUUIDs) {
        super(plyers, pl, playerUUIDs);

        World w = plyers[0].getWorld();

        Vector min = arenaArea.getMin();
        Vector max = arenaArea.getMax();

        for (int x2 = (int) Math.floor(min.getBlockX() + ((max.getBlockX() - min.getBlockX()) / 2f)); x2 <= (int) Math.floor(min.getBlockX() + ((max.getBlockX() - min.getBlockX()) / 2f)); x2++) {
            for (int y2 = min.getBlockY() + 1; y2 < max.getBlockY(); y2++) {
                for (int z2 = min.getBlockZ() + 1; z2 < max.getBlockZ(); z2++) {
                    plyers[0].getWorld().getBlockAt(x2, y2, z2).setType(Material.BEDROCK);
                }
            }
        }

        this.gameInventory = new ItemStack[] {
                enchant(new ItemStack(Material.BOW), Enchantment.ARROW_INFINITE, 1),
                new ItemStack(Material.ARROW),
                new ItemStack(Material.WHITE_WOOL, 64),
                new ItemStack(Material.WHITE_WOOL, 64),
                new ItemStack(Material.WHITE_WOOL, 64),
                new ItemStack(Material.WHITE_WOOL, 64),
                new ItemStack(Material.SHEARS)
        };

        for (Player player : players) {
            player.getInventory().setContents(gameInventory);

            player.getInventory().setArmorContents(new ItemStack[]{
                    new ItemStack(Material.IRON_BOOTS),
                    new ItemStack(Material.IRON_LEGGINGS),
                    new ItemStack(Material.IRON_CHESTPLATE),
                    new ItemStack(Material.IRON_HELMET)
            });

            player.updateInventory();
        }

        new BukkitRunnable() {
            int timer = 30;

            @Override
            public void run() {
                for (Player player : players) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.format((ChatColor.YELLOW + "You have %d seconds to build a fort!"), timer)));
                }
                timer -= 1;
                if (timer == 0) {
                    cancel();
                }
            }
        }.runTaskTimer(pl, 0, 20);

        new BukkitRunnable() {
            @Override
            public void run() {
                clearWalls(w);

                started = true;
            }
        }.runTaskLater(pl, 600);
    }

    public void clearWalls(World w) {
        Vector min = arenaArea.getMin();
        Vector max = arenaArea.getMax();

        for (int x2 = (int) Math.floor(min.getBlockX() + ((max.getBlockX() - min.getBlockX()) / 2f)); x2 <= (int) Math.floor(min.getBlockX() + ((max.getBlockX() - min.getBlockX()) / 2f)); x2++) {
            for (int y2 = min.getBlockY() + 1; y2 < max.getBlockY(); y2++) {
                for (int z2 = min.getBlockZ() + 1; z2 < max.getBlockZ(); z2++) {
                    w.getBlockAt(x2, y2, z2).setType(Material.AIR);
                }
            }
        }
    }
}
