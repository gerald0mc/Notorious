package me.gavin.notorious.setting;

import me.gavin.notorious.util.NColor;

import java.awt.*;

public class ColorSetting extends Setting {

    private final NumSetting red;
    private final NumSetting green;
    private final NumSetting blue;
    private final NumSetting alpha;

    public ColorSetting(String name, NColor color) {
        super(name);
        red = new NumSetting("R", color.getRed(), 0, 255, 1);
        green = new NumSetting("G", color.getGreen(), 0, 255, 1);
        blue = new NumSetting("B", color.getBlue(), 0, 255, 1);
        alpha = new NumSetting("A", color.getAlpha(), 0, 255, 1);
    }

    public ColorSetting(String name, int red, int green, int blue, int alpha) {
        this(name, new NColor(red, green, blue, alpha));
    }

    public ColorSetting(String name, int red, int green , int blue) {
        this(name, new NColor(red, green, blue));
    }

    public ColorSetting(String name, Color color) {
        this(name, new NColor(color));
    }

    public NumSetting getRed() {
        return red;
    }

    public NumSetting getGreen() {
        return green;
    }

    public NumSetting getBlue() {
        return blue;
    }

    public NumSetting getAlpha() {
        return alpha;
    }

    public Color getAsColor() {
        return new Color((int)red.getValue(), (int)green.getValue(), (int)blue.getValue(), (int)alpha.getValue());
    }

    public float[] getHSB() {
        return Color.RGBtoHSB((int)red.getValue(), (int)green.getValue(), (int)blue.getValue(), null);
    }
}
