package us.xylight.pvp.npc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import us.xylight.pvp.XyPVP;

import java.util.function.Consumer;

public class ClickableNPC implements Listener {
    public Location location;
    public String name;
    public XyPVP pl;

    private Consumer<PlayerInteractEntityEvent> onClick;

    public ClickableNPC(Location loc, String name, Consumer<PlayerInteractEntityEvent> click, Plugin plugin) {
        this.onClick = click;
        this.location = loc;
        this.name = name;
        this.pl = (XyPVP) plugin;



        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEntityEvent event) {

    }
}
