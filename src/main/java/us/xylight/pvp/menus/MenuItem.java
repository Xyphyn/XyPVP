package us.xylight.pvp.menus;

import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class MenuItem implements Listener {
    public ItemStack item;
    public int location;
    private Consumer<InventoryClickEvent> consumer;
    public MenuItem(ItemStack item, int location, Consumer<InventoryClickEvent> consumer) {
        this.item = item;
        this.location = location;
        this.consumer = consumer;
//        Bukkit.getPluginManager().registerEvents(this, XyPVP.getInstance());
    }

    public void onClick(InventoryClickEvent event) {
        consumer.accept(event);
    }

//    @EventHandler
//    public void clickEvent(InventoryClickEvent event) {
//
//        if (event.getCurrentItem() == null) return;
//        if (!event.getCurrentItem().hasItemMeta()) return;
//        if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
//        if (event.getCurrentItem().equals(this.item)) {
//            onClick(event);
//            event.setCancelled(true);
//        }
//    }

    public void closed() {

    }
}
