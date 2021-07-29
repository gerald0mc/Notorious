package me.gavin.notorious.setting;

import me.gavin.notorious.util.NColor;

import java.awt.*;

public class ColorSetting extends Setting {

    private final NumSetting hue;
    private final NumSetting saturation;
    private final NumSetting brightness;
    private final NumSetting alpha;

    public ColorSetting(String name, NColor color) {
        super(name);
        final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hue = new NumSetting("Hue", hsb[0], 0f, 1f, 0.01f);
        saturation = new NumSetting("Saturation", hsb[1], 0f, 1f, 0.01f);
        brightness = new NumSetting("Brightness", hsb[2], 0f, 1f, 0.01f);
        alpha = new NumSetting("A", color.getAlpha(), 0, 255, 1);
    }

    public ColorSetting(String name, int red, int green, int blue, int alpha) {
        this(name, new NColor(red, green, blue, alpha));
    }

    public ColorSetting(String name, Color color) {
        this(name, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public NumSetting getHue() {
        return hue;
    }

    public NumSetting getSaturation() {
        return saturation;
    }

    public NumSetting getBrightness() {
        return brightness;
    }

    public NumSetting getAlpha() {
        return alpha;
    }

    public Color getAsColor() {
        return new Color(Color.HSBtoRGB(hue.getValue(), saturation.getValue(), brightness.getValue()));
    }
}
