package me.gavin.notorious.gui.api;

import java.util.ArrayList;

public abstract class AbstractToggleContainer extends AbstractComponent {

    protected boolean open;
    protected final Toggleable toggleable;
    protected final ArrayList<SettingComponent> components;

    public AbstractToggleContainer(Toggleable toggleable, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.toggleable = toggleable;
        components = new ArrayList<>();
    }

    public Toggleable getToggleable() {
        return toggleable;
    }

    public abstract int getTotalHeight();

    public ArrayList<SettingComponent> getComponents() {
        return components;
    }
}
