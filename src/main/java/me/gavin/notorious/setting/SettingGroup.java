package me.gavin.notorious.setting;

import java.util.ArrayList;

public class SettingGroup {

    private final String name;
    private static ArrayList<Setting> values;

    public SettingGroup(String name) {
        this.name = name;
        this.values = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public static ArrayList<Setting> getValues() {
        return values;
    }
}
