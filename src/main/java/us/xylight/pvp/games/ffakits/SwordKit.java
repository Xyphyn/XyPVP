package us.xylight.pvp.games.ffakits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SwordKit extends FFAKit {
    public SwordKit(Player player) {
        super(player);
    }

    @Override
    public void setContents(Player p) {
        contents = new ItemStack[] {
                enchant(new ItemStack(Material.DIAMOND_SWORD), Enchantment.DAMAGE_ALL, 1)
        };

        armorContents = new ItemStack[] {
                enchant(new ItemStack(Material.DIAMOND_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                enchant(new ItemStack(Material.DIAMOND_LEGGINGS), Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                enchant(new ItemStack(Material.DIAMOND_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 2),
                enchant(new ItemStack(Material.DIAMOND_HELMET), Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        };

        p.getInventory().setContents(contents);
        p.getInventory().setArmorContents(armorContents);
    }
}
