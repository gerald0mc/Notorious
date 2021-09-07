package dev.notorious.client.value.impl;

import dev.notorious.client.value.Value;

public class ValueBind extends Value {
    private int value;

    public ValueBind(final String name, final int value){
        super(name);
        this.value = value;
    }

    public ValueBind(final String name, final String displayName, final int value){
        super(name, displayName);
        this.value = value;
    }

    public ValueBind(final String name, final String displayName, final String description, final int value){
        super (name, displayName, description);
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }
}