package us.xylight.pvp.story;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class Boss {
    public Entity entity;
    public boolean aiEnabled = true;

    public Boss(EntityType entityType, Location spawn) {
        this.entity = spawn.getWorld().spawnEntity(spawn, entityType);
    }

    public void setAiEnabled(boolean enabled) {
        aiEnabled = enabled;
        ((LivingEntity) entity).setAI(enabled);
    }
}
