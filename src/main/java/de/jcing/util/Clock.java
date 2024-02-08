package de.jcing.util;

public class Clock {

    private static final long START_MILIS = System.currentTimeMillis();

    public static int millis() {
        return (int) (System.currentTimeMillis() - START_MILIS);
    }

}
