package me.gavin.notorious.setting;

import me.gavin.notorious.hack.Hack;

public class Setting {

    private final String name;
    private Hack hack;

    private SettingGroup valueGroup;

    public Setting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setHack(Hack hack) {
        this.hack = hack;
    }

    public void setGroup(SettingGroup group) {
        this.valueGroup = group;
        group.getValues().add(this);
    }
}
