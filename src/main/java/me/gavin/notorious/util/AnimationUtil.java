package me.gavin.notorious.util;

public class AnimationUtil {
    public static float getSmooth2Animation(int duration, int time) {
        double x1 = (float) time / duration; //Used to force input to range from 0 - 1
        return (float) (6 * Math.pow(x1, 5) - (15 * Math.pow(x1, 4)) + (10 * Math.pow(x1, 3)));
    }

    public static float getSmooth2Animation(float duration, float time) {
        double x1 = time / duration; //Used to force input to range from 0 - 1
        return (float) (6 * Math.pow(x1, 5) - (15 * Math.pow(x1, 4)) + (10 * Math.pow(x1, 3)));
    }
}
