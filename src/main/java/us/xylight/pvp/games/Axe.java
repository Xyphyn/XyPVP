package us.xylight.pvp.games;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Axe extends Game {
    public Axe(ArrayList<Player> plyers) {
        super(plyers);

        this.gameInventory = new ItemStack[] {
                new ItemStack(Material.IRON_AXE),
                new ItemStack(Material.IRON_PICKAXE),
                new ItemStack(Material.BOW),
                new ItemStack(Material.ARROW, 64),
                new ItemStack(Material.COBBLESTONE, 64),
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
