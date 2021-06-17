package me.gavin.notorious.gui.api;

import java.util.ArrayList;

public abstract class AbstractToggleContainer extends AbstractComponent {

    private final Toggleable toggleable;
    private final ArrayList<SettingComponent> components;

    public AbstractToggleContainer(Toggleable toggleable, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.toggleable = toggleable;
        components = new ArrayList<>();
    }

    public Toggleable getToggleable() {
        return toggleable;
    }

    public ArrayList<SettingComponent> getComponents() {
        return components;
    }
}
