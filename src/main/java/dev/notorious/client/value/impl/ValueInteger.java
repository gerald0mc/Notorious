package dev.notorious.client.value.impl;

import dev.notorious.client.value.Value;

public class ValueInteger extends Value {
    private int value;

    private final int min;
    private final int max;

    public ValueInteger(final String name, final int value, final int min, final int max){
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public ValueInteger(final String name, final String displayName, final int value, final int min, final int max){
        super(name, displayName);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public ValueInteger(final String name, final String displayName, final String description, final int value, final int min, final int max){
        super (name, displayName, description);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }

    public int getMax(){
        return max;
    }

    public int getMin(){
        return min;
    }
}