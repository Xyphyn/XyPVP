package us.xylight.pvp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import us.xylight.pvp.XyPVP;

public class GameListener implements Listener {
    public GameListener() {
        Bukkit.getPluginManager().registerEvents(this, XyPVP.getInstance());
    }
}
