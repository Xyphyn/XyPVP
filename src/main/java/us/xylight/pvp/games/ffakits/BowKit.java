package us.xylight.pvp.games.ffakits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.xylight.pvp.games.FFA;

public class BowKit extends FFAKit {

    public BowKit(Player player) {
        super(player);
        this.name = "Archer";
    }

    @Override
    public void setContents(Player p) {
        contents = new ItemStack[] {
                enchant(enchant(new ItemStack(Material.BOW), Enchantment.ARROW_INFINITE, 1), Enchantment.ARROW_DAMAGE, 2),
                enchant(new ItemStack(Material.IRON_SWORD), Enchantment.DAMAGE_ALL, 1),
                new ItemStack(Material.ARROW)
        };

        armorContents = new ItemStack[] {
                enchant(new ItemStack(Material.DIAMOND_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                enchant(new ItemStack(Material.IRON_LEGGINGS), Enchantment.PROTECTION_PROJECTILE, 2),
                enchant(new ItemStack(Material.IRON_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                enchant(new ItemStack(Material.DIAMOND_HELMET), Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        };

        p.getInventory().setContents(contents);
        p.getInventory().setArmorContents(armorContents);
    }
}
