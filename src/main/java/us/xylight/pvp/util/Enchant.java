package us.xylight.pvp.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Enchant {
    public static ItemStack enchant(ItemStack item, Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return item;
    }
}
