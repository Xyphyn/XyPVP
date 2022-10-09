package us.xylight.pvp.games.ffakits.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Speed extends Ability {
    public Speed() {
        effect = new PotionEffect(PotionEffectType.SPEED, 99999, 0);
        item = new ItemStack(Material.RABBIT_FOOT);
    }

    @Override
    public void setPenalty(Player p) {
        p.getInventory().setLeggings(new ItemStack(Material.AIR));
    }
}
