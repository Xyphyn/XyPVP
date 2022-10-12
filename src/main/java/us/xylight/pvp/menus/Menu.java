package us.xylight.pvp.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.xylight.pvp.XyPVP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu {
    public Inventory inventory;
    public String invName;
    public XyPVP plugin;
    public Menu(MenuItem[] items, int rows, Player player, String invName) {
        this.invName = invName;
        inventory = Bukkit.createInventory(player, 9 * rows, this.invName);

        for (MenuItem item : items) inventory.setItem(item.location, item.item);
    }

    private ItemStack getItem(ItemStack item, String name, String ... lore) {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> lores = new ArrayList<>();
        for (String s : lore) lores.add(ChatColor.translateAlternateColorCodes('&', s));
        meta.setLore(lores);

        item.setItemMeta(meta);
        return item;
    }
}
