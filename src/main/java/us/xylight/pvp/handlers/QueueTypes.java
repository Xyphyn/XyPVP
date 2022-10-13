package us.xylight.pvp.handlers;

import org.bukkit.entity.Player;
import us.xylight.pvp.games.*;

import java.util.ArrayList;
import java.util.function.Consumer;

public enum QueueTypes {
    SURVIVAL(2, Survival::new),
    OP(2, us.xylight.pvp.games.OP::new),
    SUMO(2, Sumo::new),
    WALLS(2, Walls::new),
    AXE(2, Axe::new);

    public final int maxPlayers;
    public final Consumer<ArrayList<Player>> consumer;
    QueueTypes(int maximumPlayers, Consumer<ArrayList<Player>> passedConsumer) {
        maxPlayers = maximumPlayers;
        this.consumer = passedConsumer;
    }

    public void newGame(ArrayList<Player> players) {
        consumer.accept(players);
    }
}
