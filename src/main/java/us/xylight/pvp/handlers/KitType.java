package us.xylight.pvp.handlers;

import org.bukkit.entity.Player;
import us.xylight.pvp.games.ffakits.*;

import java.util.function.Function;

public enum KitType {
    AXE(AxeKit::new),
    BOW(BowKit::new),
    SWORD(SwordKit::new),
    TRIDENT(TridentKit::new);

    public final Function<Player, FFAKit> consumer;
    KitType(Function<Player, FFAKit> passedConsumer) {
        this.consumer = passedConsumer;
    }

    public FFAKit newKit(Player p) { return consumer.apply(p); }
}
