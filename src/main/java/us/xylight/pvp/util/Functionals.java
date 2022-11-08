package us.xylight.pvp.util;

public class Functionals {
    @FunctionalInterface
    public
    interface QuadFunction<T, U, V, W, R> {
        R apply(T t, U u, V v, W w);
    }
}
