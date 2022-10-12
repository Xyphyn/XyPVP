package us.xylight.pvp.games;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class OP extends Game {

    ItemStack enchant(ItemStack item, Enchantment enchantment, int level) {
        item.addEnchantment(enchantment, level);
        return item;
    }

    public OP(Player[] plyers, UUID[] playerUUIDs) {
        super(plyers, playerUUIDs);

        this.gameInventory = new ItemStack[] {
                enchant(new ItemStack(Material.DIAMOND_SWORD), Enchantment.DAMAGE_ALL, 2),
                enchant(new ItemStack(Material.BOW), Enchantment.ARROW_DAMAGE, 3),
                new ItemStack(Material.ARROW, 64),
                new ItemStack(Material.WATER_BUCKET),
                new ItemStack(Material.BIRCH_PLANKS, 64),
                enchant(new ItemStack(Material.DIAMOND_PICKAXE), Enchantment.DIG_SPEED, 5),
                new ItemStack(Material.GOLDEN_APPLE, 4)
        };

        for (Player player : players) {
            player.getInventory().setContents(gameInventory);

            player.getInventory().setArmorContents(new ItemStack[]{
                    enchant(new ItemStack(Material.DIAMOND_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 3),
                    enchant(new ItemStack(Material.DIAMOND_LEGGINGS), Enchantment.PROTECTION_ENVIRONMENTAL, 3),
                    enchant(new ItemStack(Material.DIAMOND_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 3),
                    enchant(new ItemStack(Material.IRON_HELMET), Enchantment.PROTECTION_ENVIRONMENTAL, 3)
            });
        }
    }
}
