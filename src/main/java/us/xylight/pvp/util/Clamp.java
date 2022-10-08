package us.xylight.pvp.util;

public class Clamp {
    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}
