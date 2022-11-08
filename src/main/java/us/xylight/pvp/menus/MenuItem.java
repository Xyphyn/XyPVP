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
    }

    public void onClick(InventoryClickEvent event) {
        consumer.accept(event);
    }

    public void closed() {

    }
}
