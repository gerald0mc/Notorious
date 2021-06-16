package me.gavin.notorious.setting;

import me.gavin.notorious.hack.Hack;

import java.util.ArrayList;

public class ValueGroup {

    private final String name;
    private final ArrayList<Value<?>> values;

    public ValueGroup(String name) {
        this.name = name;
        this.values = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Value<?>> getValues() {
        return values;
    }
}
