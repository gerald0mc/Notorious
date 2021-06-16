package me.gavin.notorious.setting;

import me.gavin.notorious.gui.api.Toggleable;

public class BooleanSetting extends Setting implements Toggleable {

    private boolean value;

    public BooleanSetting(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public void toggle() {
        this.value = !value;
    }

    @Override
    public boolean isEnabled() {
        return value;
    }
}