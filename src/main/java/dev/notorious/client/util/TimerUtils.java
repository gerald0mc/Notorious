package dev.notorious.client.util;

public class TimerUtils {
    public long last = System.currentTimeMillis();

    public boolean hasTimeElapsed(long time){
        return System.currentTimeMillis() - last > time;
    }

    public void reset(){
        last = System.currentTimeMillis();
    }
}
