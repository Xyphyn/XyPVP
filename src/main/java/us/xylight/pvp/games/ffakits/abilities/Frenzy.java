package us.xylight.pvp.games.ffakits.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Frenzy extends Ability {
    public Frenzy() {
        effect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 0);
        item = new ItemStack(Material.REDSTONE);
    }

    @Override
    public void setPenalty(Player p) {
        p.getInventory().setChestplate(new ItemStack(Material.AIR));
        p.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
    }
}
