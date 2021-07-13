package me.gavin.notorious.gui.api;

public abstract class SettingComponent extends AbstractComponent {
    public SettingComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public abstract int getTotalHeight();
}
