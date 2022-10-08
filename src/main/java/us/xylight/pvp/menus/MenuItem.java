package us.xylight.pvp.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import us.xylight.pvp.XyPVP;

import java.util.Objects;
import java.util.function.Consumer;

public class MenuItem implements Listener {
    public ItemStack item;
    public String name;
    public int location;
    private Consumer<InventoryClickEvent> consumer;
    public MenuItem(ItemStack item, String name, int location, XyPVP pl, Consumer<InventoryClickEvent> consumer) {
        this.item = item;
        this.name = name;
        this.location = location;
        this.consumer = consumer;
        Bukkit.getPluginManager().registerEvents(this, pl);
    }

    public void onClick(InventoryClickEvent event) {
        consumer.accept(event);
    }

    @EventHandler
    public void clickEvent(InventoryClickEvent event) {
        if (Objects.equals(event.getCurrentItem(), this.item)) {
            onClick(event);
            event.setCancelled(true);
        }
    }
}
