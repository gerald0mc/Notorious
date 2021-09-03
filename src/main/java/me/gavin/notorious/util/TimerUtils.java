package me.gavin.notorious.util;

public class TimerUtils {
    public long lastMilliseconds = System.currentTimeMillis();

    public boolean hasTimeElapsed(long time) {
        return System.currentTimeMillis() - lastMilliseconds > time;
    }

    public void reset() {
        this.lastMilliseconds = System.currentTimeMillis();
    }
}