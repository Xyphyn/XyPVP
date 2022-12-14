package us.xylight.pvp.games;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import us.xylight.pvp.XyPVP;

import java.util.ArrayList;

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

    public Walls(ArrayList<Player> plyers) {
        super(plyers);

        World w = plyers.get(0).getWorld();

        Vector min = arenaArea.getMin();
        Vector max = arenaArea.getMax();

        for (int x2 = (int) Math.floor(min.getBlockX() + ((max.getBlockX() - min.getBlockX()) / 2f)); x2 <= (int) Math.floor(min.getBlockX() + ((max.getBlockX() - min.getBlockX()) / 2f)); x2++) {
            for (int y2 = min.getBlockY() + 1; y2 < max.getBlockY(); y2++) {
                for (int z2 = min.getBlockZ() + 1; z2 < max.getBlockZ(); z2++) {
                    plyers.get(0).getWorld().getBlockAt(x2, y2, z2).setType(Material.BEDROCK);
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

        int[] timer = {30};

        BukkitScheduler scheduler = Bukkit.getScheduler();

        scheduler.runTaskTimer(XyPVP.getInstance(), task -> {
            players.forEach(p -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.format((ChatColor.YELLOW + "You have %d seconds to build a fort!"), timer[0]))));

            timer[0] -= 1;
            if (timer[0] == 0) {
                task.cancel();
            }
        }, 0, 20);

        scheduler.runTaskLater(XyPVP.getInstance(), () -> { clearWalls(w); started = true; }, 20 * 30);
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
