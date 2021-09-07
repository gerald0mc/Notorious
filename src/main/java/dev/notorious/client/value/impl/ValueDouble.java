package dev.notorious.client.value.impl;

import dev.notorious.client.value.Value;

public class ValueDouble extends Value {
    private double value;

    private final double min;
    private final double max;

    public ValueDouble(final String name, final double value, final double min, final double max){
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public ValueDouble(final String name, final String displayName, final double value, final double min, final double max){
        super(name, displayName);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public ValueDouble(final String name, final String displayName, final String description, final double value, final double min, final double max){
        super (name, displayName, description);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public double getValue(){
        return value;
    }

    public void setValue(double value){
        this.value = value;
    }

    public double getMax(){
        return max;
    }

    public double getMin(){
        return min;
    }
}