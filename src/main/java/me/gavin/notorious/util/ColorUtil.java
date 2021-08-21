package me.gavin.notorious.util;

import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class ColorUtil {

    public static int getRainbow(float time, float saturation) {
        float hue = (System.currentTimeMillis() % (int) (time * 1000)) / (time * 1000);
        return Color.HSBtoRGB(hue, saturation, 1f);
    }

    public static int getRGBWave(float seconds, float saturation, long index) {
        float hue = (float) ((System.currentTimeMillis() + index) % (long) ((int) (seconds * 1000.0F))) / (seconds * 1000.0F);
        return Color.HSBtoRGB(hue, saturation, 1f);
    }

    public static Color colorRainbow(int delay, float saturation, float brightness) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
        return Color.getHSBColor((float) ((rainbowState %= 360.0) / 360.0), saturation, brightness);
    }

    public static Color normalizedFade(float value) {
        final float green = (1f - value);
        return new Color((value), green, 0f);
    }

    public static Color normalizedFade(float value, Color startColor, Color endColor) {
        final float sr = startColor.getRed() / 255f;
        final float sg = startColor.getGreen() / 255f;
        final float sb = startColor.getBlue() / 255f;

        final float er = endColor.getRed() / 255f;
        final float eg = endColor.getGreen() / 255f;
        final float eb = endColor.getBlue() / 255f;

        final float r = MathHelper.clamp(sr * value + er * (1f - value), 0.0f, 1.0f);
        final float g = MathHelper.clamp(sg * value + eg * (1f - value), 0.0f, 1.0f);
        final float b = MathHelper.clamp(sb * value + eb * (1f - value), 0.0f, 1.0f);

        return new Color(r, g, b);
    }

    public static Color getColorFlow(double time, double speed, Color startColor, Color endColor) {
        final float sin = (float) (Math.sin(((System.currentTimeMillis() / speed) + time)) * 0.5f) + 0.5f;
        return normalizedFade(sin, startColor, endColor);
    }
}
