package us.xylight.pvp.games.ffakits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.xylight.pvp.util.Enchant;

public class TridentKit extends FFAKit {
    public TridentKit(Player player) {
        super(player);
        this.name = "Trident";
    }

    @Override
    public void setContents(Player p) {
        contents = new ItemStack[] {
                Enchant.enchant(Enchant.enchant(new ItemStack(Material.TRIDENT), Enchantment.LOYALTY, 3), Enchantment.RIPTIDE, 2),
                new ItemStack(Material.WATER_BUCKET)
        };

        armorContents = new ItemStack[] {
                Enchant.enchant(new ItemStack(Material.IRON_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                Enchant.enchant(new ItemStack(Material.IRON_LEGGINGS), Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                Enchant.enchant(new ItemStack(Material.DIAMOND_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                Enchant.enchant(new ItemStack(Material.IRON_HELMET), Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        };

        p.getInventory().setContents(contents);
        p.getInventory().setArmorContents(armorContents);
    }
}
