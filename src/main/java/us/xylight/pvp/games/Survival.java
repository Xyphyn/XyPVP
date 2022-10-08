package us.xylight.pvp.games;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class Survival extends Game {

    public Survival(Player[] plyers, Plugin pl, UUID[] playerUUIDs) {
        super(plyers, pl, playerUUIDs);

        this.gameInventory = new ItemStack[] {
                new ItemStack(Material.IRON_SWORD),
                new ItemStack(Material.BOW),
                new ItemStack(Material.ARROW, 64),
                new ItemStack(Material.WATER_BUCKET),
                new ItemStack(Material.COBBLESTONE, 64),
                new ItemStack(Material.IRON_PICKAXE)
        };

        for (Player player : players) {
            player.getInventory().setContents(gameInventory);

            player.getInventory().setArmorContents(new ItemStack[]{
                    new ItemStack(Material.IRON_BOOTS),
                    new ItemStack(Material.IRON_LEGGINGS),
                    new ItemStack(Material.IRON_CHESTPLATE),
                    new ItemStack(Material.IRON_HELMET)
            });

            player.getInventory().setItemInOffHand(new ItemStack(Material.SHIELD));
        }
    }
}
