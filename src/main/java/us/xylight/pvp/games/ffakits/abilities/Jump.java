package us.xylight.pvp.games.ffakits.abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Jump extends Ability {
    public Jump() {
        effect = new PotionEffect(PotionEffectType.JUMP, 99999, 2);
        item = new ItemStack(Material.DIAMOND_BOOTS);
    }

    @Override
    public void setPenalty(Player p) {
        p.getInventory().setBoots(new ItemStack(Material.AIR));
        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 99999, 0));
    }
}
