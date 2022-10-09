package us.xylight.pvp.games.ffakits.abilities;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.xylight.pvp.util.Enchant;

public class Tank extends Ability {
    public Tank() {
        effect = new PotionEffect(PotionEffectType.SLOW, 99999, 0);
        item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
    }

    @Override
    public void setPenalty(Player p) {
        p.getInventory().setChestplate(Enchant.enchant(p.getInventory().getChestplate(), Enchantment.PROTECTION_ENVIRONMENTAL, 3));
    }
}
