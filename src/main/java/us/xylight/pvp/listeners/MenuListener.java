package us.xylight.pvp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.menus.Menu;
import us.xylight.pvp.menus.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class MenuListener implements Listener {
    private static Map<Inventory, Menu> menus = new HashMap<>();
    public MenuListener() {
        Bukkit.getPluginManager().registerEvents(this, XyPVP.getInstance());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;
        Menu menu = menus.get(clickedInventory);
        if (menu == null) return;
        if (menu.inventory != clickedInventory) return;
        MenuItem menuItem = menu.getItemAt(event.getSlot());
        if (menuItem == null) return;
        menuItem.onClick(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        menus.remove(e.getInventory());
    }

    public static void addMenu(Menu menu) {
        menus.put(menu.inventory, menu);
    }
}
