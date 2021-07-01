package me.gavin.notorious.util;

import java.awt.*;

public class ColorUtil {

    public static int getRainbow(float time, float saturation) {
        float hue = (System.currentTimeMillis() % (int) (time * 1000)) / (time * 1000);
        return Color.HSBtoRGB(hue, saturation, 1f);
    }

    public static int getRGBWave(float seconds, float saturation, long index) {
        float hue = (float)((System.currentTimeMillis() + index) % (long)((int)(seconds * 1000.0F))) / (seconds * 1000.0F);
        return Color.HSBtoRGB(hue, saturation, 1f);
    }

    public static Color colorRainbow(float delay, float saturation) {
        float hue = (System.currentTimeMillis() % (delay * 1000)) / (delay * 1000);
        return Color.getHSBColor(hue, saturation, 1f);
    }

    public static Color normalizedFade(float value) {
        final float green = (1f - value);
        return new Color((value), green, 0f);
    }
}
