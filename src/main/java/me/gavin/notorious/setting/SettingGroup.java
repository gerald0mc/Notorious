package me.gavin.notorious.setting;

import me.gavin.notorious.hack.Hack;

import java.util.ArrayList;

public class SettingGroup {

    private final String name;
    private final ArrayList<Setting> values;

    public SettingGroup(String name) {
        this.name = name;
        this.values = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Setting> getValues() {
        return values;
    }
}
