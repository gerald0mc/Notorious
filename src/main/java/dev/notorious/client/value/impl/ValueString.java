package dev.notorious.client.value.impl;

import dev.notorious.client.value.Value;

public class ValueString extends Value {
    private String value;

    public ValueString(final String name, final String value){
        super(name);
        this.value = value;
    }

    public ValueString(final String name, final String displayName, final String value){
        super(name, displayName);
        this.value = value;
    }

    public ValueString(final String name, final String displayName, final String description, final String value){
        super (name, displayName, description);
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }
}