package dev.notorious.client.value.impl;

import dev.notorious.client.value.Value;

import java.util.Arrays;
import java.util.List;

public class ValueMode extends Value {
    private final List<String> modes;
    private String value;

    public ValueMode(final String name, final String[] modes, final String value){
        super(name);
        this.modes = Arrays.asList(modes);
        this.value = value;
    }

    public ValueMode(final String name, final String displayName, final String[] modes, final String value){
        super(name, displayName);
        this.modes = Arrays.asList(modes);
        this.value = value;
    }

    public ValueMode(final String name, final String displayName, final String description, final String[] modes, final String value){
        super (name, displayName, description);
        this.modes = Arrays.asList(modes);
        this.value = value;
    }

    public void increment(){
        for (int i = 0; i < modes.size(); i++){
            if (modes.get(i).equals(getValue())) {
                i++;
                if (i > modes.size() - 1) i = 0;
                setValue(modes.get(i));
            }
        }
    }

    public void decrement(){
        for (int i = 0; i < modes.size(); i++){
            if (modes.get(i).equals(getValue())) {
                i--;
                if (i < 0) i = modes.size() - 1;
                setValue(modes.get(i));
            }
        }
    }

    public String getValue(){
        return value;
    }

    public boolean equals(final String value){
        return this.value.equalsIgnoreCase(value);
    }

    public void setValue(String value){
        this.value = value;
    }

    public List<String> getModes(){
        return modes;
    }
}