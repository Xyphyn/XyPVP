package us.xylight.pvp.enums;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.xylight.pvp.handlers.MenuHandler;
import us.xylight.pvp.listeners.MenuListener;
import us.xylight.pvp.menus.Menu;
import us.xylight.pvp.menus.MenuItem;

public enum MenuType {
    MAIN(MenuHandler.gameMenuItems, 3, ChatColor.WHITE + "\uF808⫝̸"),
    FFA(MenuHandler.ffaMenuItems, 3, ChatColor.WHITE + "\uF808₃"),
    ABILITIES(MenuHandler.abilityMenuItems, 3, ChatColor.WHITE + "\uF808₂"),
    MULTIPLAYER(MenuHandler.multiplayerMenuItems, 3, ChatColor.WHITE + "\uF808₄");

    // I'm sorry.
    public final MenuItem[] items;
    public final int rows;
    public final String title;

    MenuType(MenuItem[] items, int rows, String title) {
        this.items = items;
        this.rows = rows;
        this.title = title;
    }

    public void createMenu(Player p) {
        Menu menu = new Menu(items, rows, p, title);
        MenuListener.addMenu(menu);
        p.openInventory(menu.inventory);
    }
}
