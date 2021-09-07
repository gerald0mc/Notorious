package dev.notorious.client.value.impl;

import dev.notorious.client.value.Value;

public class ValueBoolean extends Value {
    private boolean value;

    public ValueBoolean(final String name, final boolean value){
        super(name);
        this.value = value;
    }

    public ValueBoolean(final String name, final String displayName, final boolean value){
        super(name, displayName);
        this.value = value;
    }

    public ValueBoolean(final String name, final String displayName, final String description, final boolean value){
        super (name, displayName, description);
        this.value = value;
    }

    public boolean getValue(){
        return value;
    }

    public void setValue(boolean value){
        this.value = value;
    }
}