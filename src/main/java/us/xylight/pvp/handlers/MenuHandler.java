package us.xylight.pvp.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.games.FFA;
import us.xylight.pvp.menus.Menu;
import us.xylight.pvp.menus.MenuItem;

import java.util.*;
import java.util.concurrent.Callable;

public class MenuHandler implements Listener {
    private final String[] titles = {"Game Menu", "Multiplayer"};
    XyPVP pl;

    public HashMap<String, Menu> associations;

    ItemStack arenaItem = getItem(new ItemStack(Material.DIAMOND_SWORD), "&cDon't click please", "&8Click to queue");
    ItemStack multiplayerItem = getItem(new ItemStack(Material.GOLDEN_SWORD), "&9Multiplayer", "&8Click to queue");
    MenuItem[] gameMenuItems;
    MenuItem[] multiplayerMenuItems;
    public MenuHandler(Plugin plugin) {
        this.pl = (XyPVP) plugin;
        Bukkit.getPluginManager().registerEvents(this, this.pl);

        gameMenuItems = new MenuItem[]{
                new MenuItem(arenaItem, "&bFFA", 11, pl,
                        event -> pl.ffa.joinFFA((Player) event.getWhoClicked()) ),
                new MenuItem(multiplayerItem, "&9Multiplayer", 13, pl,
                        event -> openMultiplayerMenu((Player) event.getWhoClicked()))
        };

        multiplayerMenuItems = new MenuItem[] {
            new MenuItem(getItem(new ItemStack(Material.IRON_CHESTPLATE), "&bSurvival", "&7Click to queue"), "&bSurvival", 11, pl,
                    event -> pl.queueHandler.queue((Player) event.getWhoClicked(), QueueTypes.SURVIVAL)),
            new MenuItem(getItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), "&bOP", "&7Click to queue"), "&bOP", 12, pl,
                    event -> pl.queueHandler.queue((Player) event.getWhoClicked(), QueueTypes.OP)),
            new MenuItem(getItem(new ItemStack(Material.LEAD), "&bSumo", "&7Click to queue"), "&bSumo", 13, pl,
                    event -> pl.queueHandler.queue((Player) event.getWhoClicked(), QueueTypes.SUMO)),
            new MenuItem(getItem(new ItemStack(Material.COBBLESTONE_WALL), "&bWalls", "&7Click to queue"), "&bWalls", 14, pl,
                    event -> pl.queueHandler.queue((Player) event.getWhoClicked(), QueueTypes.WALLS)),
                new MenuItem(getItem(new ItemStack(Material.IRON_AXE), "&bAxe", "&7Click to queue"), "&bAxe", 15, pl,
                        event -> pl.queueHandler.queue((Player) event.getWhoClicked(), QueueTypes.AXE))
        };

    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        try {
            //  || LobbyHandler.inLobby(event.getPlayer())
                if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.hasItem()) {
                    if (event.getItem().hasItemMeta()) {
                        if (event.getItem().getItemMeta().hasDisplayName()) {
                            if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§9§lGame Menu")) {
                                Player p = event.getPlayer();
                                if (LobbyHandler.inLobby(p)) {
                                    openGameMenu(p);
                                }
                            }
                        }
                    }
                }
        } catch (NullPointerException e) { e.printStackTrace(); }
    }

    public void openGameMenu(Player p) {
        Menu gameMenu = new Menu(gameMenuItems, 3, p, "Game Menu");
        p.openInventory(gameMenu.inventory);
    }

    public void openMultiplayerMenu(Player p) {
        Menu multiplayerMenu = new Menu(multiplayerMenuItems, 3, p, "Multiplayer");
        p.openInventory(multiplayerMenu.inventory);
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!LobbyHandler.inLobby((Player) event.getWhoClicked()) || !Arrays.asList(titles).contains(event.getView().getTitle())) return;
        event.setCancelled(true);
    }

    public ItemStack getItem(ItemStack item, String name, String ... lore) {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        List<String> lores = new ArrayList<>();
        for (String s : lore) {
            lores.add(ChatColor.translateAlternateColorCodes('&', s));

        }
        meta.setLore(lores);

        item.setItemMeta(meta);
        return item;
    }
}
