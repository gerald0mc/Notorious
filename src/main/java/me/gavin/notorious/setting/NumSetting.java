package me.gavin.notorious.setting;

public class NumSetting extends Setting {

    private float value;

    private final float min;
    private final float max;
    private final float increment;

    public NumSetting(String name, float value, float min, float max, float increment) {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public void setValue(float value) {
        this.value = Math.max(min, Math.min(value, max));
    }

    public float getValue() {
        return value;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getIncrement() {
        return increment;
    }
}
