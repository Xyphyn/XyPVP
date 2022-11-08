package us.xylight.pvp.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.xylight.pvp.XyPVP;
import us.xylight.pvp.enums.KitType;
import us.xylight.pvp.enums.MenuType;
import us.xylight.pvp.enums.QueueType;
import us.xylight.pvp.games.ffakits.abilities.*;
import us.xylight.pvp.menus.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuHandler implements Listener {
    public XyPVP pl;
    public static MenuItem[] ffaMenuItems;

    ItemStack arenaItem = getItem(new ItemStack(Material.DIAMOND_SWORD), "&bFFA", "&8Fight in the colosseum!");
    ItemStack multiplayerItem = getItem(new ItemStack(Material.GOLDEN_SWORD), "&bMultiplayer", "&8Duels, minigames, and more");
    ItemStack invisibleExit = getItem(new ItemStack(Material.PAPER), "&cExit");
    ItemStack invisAbility = getItem(new ItemStack(Material.PAPER), "&bAbilities");
    public static MenuItem[] gameMenuItems;
    public static MenuItem[] multiplayerMenuItems;
    public static MenuItem[] abilityMenuItems;
    public MenuHandler() {
        this.pl = XyPVP.getInstance();
        Bukkit.getPluginManager().registerEvents(this, this.pl);
        ItemMeta meta = invisibleExit.getItemMeta();
        meta.setCustomModelData(1);
        invisibleExit.setItemMeta(meta);
        ItemMeta meta2 = invisAbility.getItemMeta();
        meta2.setCustomModelData(2);
        invisAbility.setItemMeta(meta2);

        gameMenuItems = new MenuItem[]{
                new MenuItem(arenaItem, 1,
                        event -> openMenu(event.getWhoClicked(), MenuType.FFA)),
                new MenuItem(multiplayerItem, 4,
                        event -> openMenu(event.getWhoClicked(), MenuType.MULTIPLAYER)),
                new MenuItem(getItem(invisibleExit, "&cExit"),  22,
                        event -> event.getWhoClicked().closeInventory())
        };

        abilityMenuItems = new MenuItem[] {
                new MenuItem(getItem(new ItemStack(Material.BARRIER), "&bNone", "&8&l• &8No ability"),  0,
                        event -> {
                        pl.ffa.abilities.put((event.getWhoClicked()).getUniqueId(), new None());
                        ((Player) event.getWhoClicked()).playSound(event.getWhoClicked(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                        openMenu(event.getWhoClicked(), MenuType.ABILITIES);
                     }),
                new MenuItem(getItem(new ItemStack(Material.RABBIT_FOOT), "&bSpeed", "&b+ &r&bSpeed", "&c&l- &r&cNo leggings"), 2,
                        event -> {
                            pl.ffa.abilities.put((event.getWhoClicked()).getUniqueId(), new Speed());
                            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                            openMenu(event.getWhoClicked(), MenuType.ABILITIES);
                        }),
                new MenuItem(getItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE), "&bTank", "&b+ &r&bProtection 3", "&c&l- &r&cSlowness"),  4,
                        event -> {
                            pl.ffa.abilities.put(((Player) event.getWhoClicked()).getUniqueId(), new Tank());
                            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                            openMenu(event.getWhoClicked(), MenuType.ABILITIES);
                        }),
                new MenuItem(getItem(new ItemStack(Material.REDSTONE), "&bFrenzy", "&b+ &r&bStrength", "&c&l- &r&cNo chestplate", "&c&l- &r&cChainmail Leggings"),  6,
                        event -> {
                            pl.ffa.abilities.put(((Player) event.getWhoClicked()).getUniqueId(), new Frenzy());
                            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                            openMenu(event.getWhoClicked(), MenuType.ABILITIES);
                        }),
                new MenuItem(getItem(new ItemStack(Material.DIAMOND_BOOTS), "&bJump", "&b+ &r&bJump Boost 3", "&c&l- &r&cNo boots", "&c&l- &r&cWeakness"),  8,
                        event -> {
                            pl.ffa.abilities.put(((Player) event.getWhoClicked()).getUniqueId(), new Jump());
                            ((Player) event.getWhoClicked()).playSound(event.getWhoClicked(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                            openMenu(event.getWhoClicked(), MenuType.ABILITIES);
                        }),
                new MenuItem(getItem(invisibleExit, "&cBack"),  22,
                        event -> openMenu(event.getWhoClicked(), MenuType.FFA))
        };

        multiplayerMenuItems = new MenuItem[] {
            new MenuItem(getItem(new ItemStack(Material.IRON_CHESTPLATE), "&bSurvival", "&8Iron armor, iron weapons"),
                     0,
                    event -> pl.queueHandler.queue(event.getWhoClicked(), QueueType.SURVIVAL)),
            new MenuItem(getItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), "&bOP", "&8Enchanted diamond armor and weapons"), 2,
                    event -> pl.queueHandler.queue(event.getWhoClicked(), QueueType.OP)),
            new MenuItem(getItem(new ItemStack(Material.LEAD), "&bSumo", "&8Knock the other player off the map!"), 4,
                    event -> pl.queueHandler.queue(event.getWhoClicked(), QueueType.SUMO)),
            new MenuItem(getItem(new ItemStack(Material.COBBLESTONE_WALL), "&bWalls", "&8Build a fort and bow your opponent!"), 6,
                    event -> pl.queueHandler.queue(event.getWhoClicked(), QueueType.WALLS)),
            new MenuItem(getItem(new ItemStack(Material.IRON_AXE), "&bAxe", "&8Traditional axe PvP."), 8,
                    event -> pl.queueHandler.queue(event.getWhoClicked(), QueueType.AXE)),
            new MenuItem(getItem(invisibleExit, "&cExit"), 22, event -> openMenu(event.getWhoClicked(), MenuType.MAIN))
        };

        ffaMenuItems = new MenuItem[] {
                new MenuItem(
                        getItem(new ItemStack(Material.DIAMOND_AXE), "&bAxe", "&8Melee    | &6★★★★★", "&8Defense | &6★★★☆☆"),
                        1,
                        event -> pl.ffa.joinFFA(event.getWhoClicked(), KitType.AXE)),
                new MenuItem(
                        getItem(new ItemStack(Material.DIAMOND_SWORD), "&bKnight", "&8Melee    | &6★★★★☆", "&8Defense | &6★★★★☆"),
                        3,
                        event -> pl.ffa.joinFFA(event.getWhoClicked(), KitType.SWORD)),
                new MenuItem(getItem(new ItemStack(Material.BOW), "&bArcher", "&8Ranged  | &6★★★★★", "&8Melee    | &6★★☆☆☆",
                        "&8Defense | &6★★★☆☆"),
                        5,
                        event -> pl.ffa.joinFFA(event.getWhoClicked(), KitType.BOW)),
                new MenuItem(
                        getItem(new ItemStack(Material.TRIDENT), "&bTrident", "&8Ranged  | &6★★★☆☆", "&8Melee    | &6★★★★☆",
                                "&8Defense | &6★★★☆☆"),
                        7,
                event -> pl.ffa.joinFFA(event.getWhoClicked(), KitType.TRIDENT)),
                new MenuItem(
                        getItem(invisAbility, "&bAbilities", "&8Abilities can power you up- at a cost!"),
                        26,
                        event -> openMenu(event.getWhoClicked(), MenuType.ABILITIES)),
                new MenuItem(getItem(invisibleExit, "&cExit"), 22, event -> openMenu(event.getWhoClicked(), MenuType.MAIN))
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
        if (LobbyHandler.inLobby(p)) openMenu(p, MenuType.MAIN);
    }

    public void openMenu(HumanEntity pl, MenuType type) {
        if (!(pl instanceof Player p)) return;

        if (type.equals(MenuType.ABILITIES)) {
            for (MenuItem menuItem : abilityMenuItems) {
                menuItem.item.removeEnchantment(Enchantment.DIG_SPEED);
                if (XyPVP.getInstance().ffa.abilities.get(p.getUniqueId()) == null) {
                    if (menuItem.item.getType().equals(Material.BARRIER)) {
                        menuItem.item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                        ItemMeta meta = menuItem.item.getItemMeta();
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        menuItem.item.setItemMeta(meta);
                    }
                    continue;
                }
                if (menuItem.item.getType().equals(XyPVP.getInstance().ffa.abilities.get(p.getUniqueId()).item.getType())) {
                    menuItem.item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                    ItemMeta meta = menuItem.item.getItemMeta();
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    menuItem.item.setItemMeta(meta);
                } else {
                    menuItem.item.removeEnchantment(Enchantment.DIG_SPEED);
                }
            }
        }

        type.createMenu(p);
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!LobbyHandler.inLobby((Player) event.getWhoClicked())) return;
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
