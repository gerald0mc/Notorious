package me.gavin.notorious.util;

public class Timer {
    public long time = -1L;

    public boolean passed(double ms) {
        return ((System.currentTimeMillis() - this.time) >= ms);
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }

    public void resetTime(long ms) {
        this.time = System.currentTimeMillis() + ms;
    }
}
