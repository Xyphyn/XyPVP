package us.xylight.pvp.util;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.UUID;

public class PlaceableBlock implements Workload {
    private final UUID worldID;
    private final int blockX;
    private final int blockY;
    private final int blockZ;
    private final Material material;

    public PlaceableBlock(UUID worldID, int blockX, int blockY, int blockZ, Material material) {
        this.worldID = worldID;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.material = material;
    }

    @Override
    public void compute() {
        World world = Bukkit.getWorld(this.worldID);
        Preconditions.checkState(world != null);
        world.getBlockAt(this.blockX, this.blockY, this.blockZ).setType(this.material);
    }
}

