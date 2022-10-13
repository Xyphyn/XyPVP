package us.xylight.pvp.ranks;

public enum RankPermission {
    DEFAULT(0),
    MODERATOR(2),
    ADMIN(3),
    OWNER(4);

    private final int power;
    RankPermission(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }
}
