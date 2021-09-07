package dev.notorious.client.value.impl;

import dev.notorious.client.value.Value;

import java.awt.*;

public class ValueColor extends Value {
    private float hue;
    private float saturation;
    private float brightness;
    private int alpha;

    public ValueColor(final String name, final Color value){
        super(name);

        final float[] hsb = Color.RGBtoHSB(value.getRed(), value.getGreen(), value.getBlue(), null);
        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
        alpha = value.getAlpha();
    }

    public ValueColor(final String name, final String displayName, final Color value){
        super(name, displayName);

        final float[] hsb = Color.RGBtoHSB(value.getRed(), value.getGreen(), value.getBlue(), null);
        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
        alpha = value.getAlpha();
    }

    public ValueColor(final String name, final String displayName, final String description, final Color value){
        super (name, displayName, description);

        final float[] hsb = Color.RGBtoHSB(value.getRed(), value.getGreen(), value.getBlue(), null);
        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
        alpha = value.getAlpha();
    }

    public Color getColor(){
        return new Color(Color.HSBtoRGB(hue, saturation, brightness));
    }

    public void setColor(Color color){
        final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
        this.alpha = color.getAlpha();
    }

    public int getRed(){
        return getColor().getRed();
    }

    public int getGreen(){
        return getColor().getGreen();
    }

    public int getBlue(){
        return getColor().getBlue();
    }

    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}