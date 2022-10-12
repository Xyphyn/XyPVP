package us.xylight.pvp.games.ffakits;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FFAKit {
    public ItemStack[] contents;
    public ItemStack[] armorContents;
    public String name = "";

    ItemStack enchant(ItemStack item, Enchantment enchantment, int level) {
        item.addEnchantment(enchantment, level);
        return item;
    }

    public FFAKit(Player player) {
        setContents(player);
    }

    public void setContents(Player player) {
        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(armorContents);
    }
}
