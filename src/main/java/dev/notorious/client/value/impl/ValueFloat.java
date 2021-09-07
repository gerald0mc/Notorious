package dev.notorious.client.value.impl;

import dev.notorious.client.value.Value;

public class ValueFloat extends Value {
    private float value;

    private final float min;
    private final float max;

    public ValueFloat(final String name, final float value, final float min, final float max){
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public ValueFloat(final String name, final String displayName, final float value, final float min, final float max){
        super(name, displayName);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public ValueFloat(final String name, final String displayName, final String description, final float value, final float min, final float max){
        super (name, displayName, description);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public float getValue(){
        return value;
    }

    public void setValue(float value){
        this.value = value;
    }

    public float getMax(){
        return max;
    }

    public float getMin(){
        return min;
    }
}