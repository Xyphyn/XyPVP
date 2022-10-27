package us.xylight.pvp.games.ffakits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AxeKit extends FFAKit {
    public AxeKit(Player player) {
        super(player);
        this.name = "Axe";
    }

    @Override
    public void setContents(Player p) {
        contents = new ItemStack[] {
                enchant(new ItemStack(Material.DIAMOND_AXE), Enchantment.DAMAGE_ALL, 1)
        };

        armorContents = new ItemStack[] {
                enchant(new ItemStack(Material.DIAMOND_BOOTS), Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                enchant(new ItemStack(Material.DIAMOND_LEGGINGS), Enchantment.PROTECTION_PROJECTILE, 2),
                enchant(new ItemStack(Material.IRON_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 1),
                enchant(new ItemStack(Material.DIAMOND_HELMET), Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        };

        p.getInventory().setContents(contents);
        p.getInventory().setArmorContents(armorContents);
    }
}
