package us.xylight.pvp.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.games.FFA;
import us.xylight.pvp.games.ffakits.AxeKit;
import us.xylight.pvp.games.ffakits.BowKit;
import us.xylight.pvp.games.ffakits.SwordKit;
import us.xylight.pvp.games.ffakits.TridentKit;
import us.xylight.pvp.games.ffakits.abilities.*;
import us.xylight.pvp.menus.Menu;
import us.xylight.pvp.menus.MenuItem;

import java.util.*;
import java.util.concurrent.Callable;

public class MenuHandler implements Listener {
    private final String[] titles = {"Game Menu", "Multiplayer"};
    public XyPVP pl;

    public HashMap<String, Menu> associations;
    MenuItem[] ffaMenuItems;

    ItemStack arenaItem = getItem(new ItemStack(Material.DIAMOND_SWORD), "&bFFA", "&8Fight in the colosseum!");
    ItemStack multiplayerItem = getItem(new ItemStack(Material.GOLDEN_SWORD), "&bMultiplayer", "&8Duels, minigames, and more");
    MenuItem[] gameMenuItems;
    MenuItem[] multiplayerMenuItems;
    MenuItem[] abilityMenuItems;
    public MenuHandler() {
        this.pl = XyPVP.getInstance();
        Bukkit.getPluginManager().registerEvents(this, this.pl);

        gameMenuItems = new MenuItem[]{
                new MenuItem(arenaItem, "&bFFA", 11,
                        event -> openFFAMenu((Player) event.getWhoClicked()) ),
                new MenuItem(multiplayerItem, "&9Multiplayer", 13,
                        event -> openMultiplayerMenu((Player) event.getWhoClicked()))
        };

        abilityMenuItems = new MenuItem[] {
                new MenuItem(getItem(new ItemStack(Material.BARRIER), "&bNone", "&8&l• &8No ability"), "&bNone", 11,
                        event -> {
                        pl.ffa.abilities.put(((Player) event.getWhoClicked()).getUniqueId(), new None());
                        ((Player) event.getWhoClicked()).playSound(event.getWhoClicked(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                        openAbilitiesMenu((Player) event.getWhoClicked());
                     }),
                new MenuItem(getItem(new ItemStack(Material.RABBIT_FOOT), "&bSpeed", "&b&l+ &r&bSpeed", "&c&l- &r&cNo leggings"), "&bSpeed", 12,
                        event -> {
                            pl.ffa.abilities.put(((Player) event.getWhoClicked()).getUniqueId(), new Speed());
                            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                            openAbilitiesMenu((Player) event.getWhoClicked());
                        }),
                new MenuItem(getItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE), "&bTank", "&b&l+ &r&bProtection 3", "&c&l- &r&cSlowness"), "&bTank", 13,
                        event -> {
                            pl.ffa.abilities.put(((Player) event.getWhoClicked()).getUniqueId(), new Tank());
                            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                            openAbilitiesMenu((Player) event.getWhoClicked());
                        }),
                new MenuItem(getItem(new ItemStack(Material.REDSTONE), "&bFrenzy", "&b&l+ &r&bStrength", "&c&l- &r&cNo chestplate", "&c&l- &r&cChainmail Leggings"), "&bFrenzy", 14,
                        event -> {
                            pl.ffa.abilities.put(((Player) event.getWhoClicked()).getUniqueId(), new Frenzy());
                            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                            openAbilitiesMenu((Player) event.getWhoClicked());
                        }),
                new MenuItem(getItem(new ItemStack(Material.DIAMOND_BOOTS), "&bJump", "&b&l+ &r&bJump Boost 3", "&c&l- &r&cNo boots", "&c&l- &r&cWeakness"), "&bJump", 15,
                        event -> {
                            pl.ffa.abilities.put(((Player) event.getWhoClicked()).getUniqueId(), new Jump());
                            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                            openAbilitiesMenu((Player) event.getWhoClicked());
                        }),
                new MenuItem(getItem(new ItemStack(Material.DARK_OAK_DOOR), "&cBack", "Return to the FFA menu."), "&cBack", 26,
                        event -> {
                            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                            openFFAMenu((Player) event.getWhoClicked());
                        })
        };

        multiplayerMenuItems = new MenuItem[] {
            new MenuItem(getItem(new ItemStack(Material.IRON_CHESTPLATE), "&bSurvival", "&8Iron armor, iron weapons"),
                    "&bSurvival", 11,
                    event -> pl.queueHandler.queue((Player) event.getWhoClicked(), QueueTypes.SURVIVAL)),
            new MenuItem(getItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), "&bOP", "&8Enchanted diamond armor and weapons"), "&bOP", 12,
                    event -> pl.queueHandler.queue((Player) event.getWhoClicked(), QueueTypes.OP)),
            new MenuItem(getItem(new ItemStack(Material.LEAD), "&bSumo", "&8Knock the other player off the map!"), "&bSumo", 13,
                    event -> pl.queueHandler.queue((Player) event.getWhoClicked(), QueueTypes.SUMO)),
            new MenuItem(getItem(new ItemStack(Material.COBBLESTONE_WALL), "&bWalls", "&8Build a fort and bow your opponent!"), "&bWalls", 14,
                    event -> pl.queueHandler.queue((Player) event.getWhoClicked(), QueueTypes.WALLS)),
                new MenuItem(getItem(new ItemStack(Material.IRON_AXE), "&bAxe", "&8Traditional axe PvP."), "&bAxe", 15,
                        event -> pl.queueHandler.queue((Player) event.getWhoClicked(), QueueTypes.AXE))
        };

        ffaMenuItems = new MenuItem[] {
                new MenuItem(getItem(new ItemStack(Material.DIAMOND_AXE), "&bAxe", "&8Melee    | &6★★★★★", "&8Defense | &6★★★☆☆"), "&bAxe", 10,
                        event -> pl.ffa.joinFFA((Player) event.getWhoClicked(), new AxeKit((Player) event.getWhoClicked()))),
                new MenuItem(getItem(new ItemStack(Material.DIAMOND_SWORD), "&bKnight", "&8Melee    | &6★★★★☆", "&8Defense | &6★★★★☆"), "&bSword", 12,
                        event -> pl.ffa.joinFFA((Player) event.getWhoClicked(), new SwordKit((Player) event.getWhoClicked()))),
                new MenuItem(getItem(new ItemStack(Material.BOW), "&bArcher", "&8Ranged  | &6★★★★★", "&8Melee    | &6★★☆☆☆", "&8Defense | &6★★★☆☆"), "&bBow", 14,
                        event -> pl.ffa.joinFFA((Player) event.getWhoClicked(), new BowKit((Player) event.getWhoClicked()))),
                new MenuItem(getItem(new ItemStack(Material.TRIDENT), "&bTrident", "&8Ranged  | &6★★★☆☆", "&8Melee    | &6★★★★☆", "&8Defense | &6★★★☆☆"), "&bTrident", 16,
                event -> pl.ffa.joinFFA((Player) event.getWhoClicked(), new TridentKit((Player) event.getWhoClicked()))),
                new MenuItem(getItem(new ItemStack(Material.BEACON), "&bAbilities", "&8Abilities can power you up- at a cost!"), "&bAbilities", 26, event -> openAbilitiesMenu((Player) event.getWhoClicked()))
        };

    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!event.hasItem()) return;
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (!event.getItem().hasItemMeta()) return;
        if (!event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(
                ChatColor.translateAlternateColorCodes('&', "&9&lGame Menu")
        )) return;

        Player p = event.getPlayer();
        if (LobbyHandler.inLobby(p)) openGameMenu(p);
    }

    public void openGameMenu(Player p) {
        Menu gameMenu = new Menu(gameMenuItems, 3, p, "Game Menu");
        p.openInventory(gameMenu.inventory);
    }

    public void openMultiplayerMenu(Player p) {
        Menu multiplayerMenu = new Menu(multiplayerMenuItems, 3, p, "Multiplayer");
        p.openInventory(multiplayerMenu.inventory);
    }

    public void openFFAMenu(Player p) {

        Menu ffaMenu = new Menu(ffaMenuItems, 3, p, "FFA");
        p.openInventory(ffaMenu.inventory);
    }

    public void openAbilitiesMenu(Player p) {
        for (MenuItem menuItem : abilityMenuItems) {
            menuItem.item.removeEnchantment(Enchantment.DIG_SPEED);
            if (pl.ffa.abilities.get(p.getUniqueId()) == null) {
                if (menuItem.item.getType().equals(Material.BARRIER)) {
                    menuItem.item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                    ItemMeta meta = menuItem.item.getItemMeta();
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    menuItem.item.setItemMeta(meta);
                }
                continue;
            }
            if (menuItem.item.getType().equals(pl.ffa.abilities.get(p.getUniqueId()).item.getType())) {
                menuItem.item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                ItemMeta meta = menuItem.item.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                menuItem.item.setItemMeta(meta);
            } else {
                menuItem.item.removeEnchantment(Enchantment.DIG_SPEED);
            }
        }

        Menu abilityMenu = new Menu(abilityMenuItems, 3, p, "Abilities");
        p.openInventory(abilityMenu.inventory);
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
