package me.gavin.notorious.gui.setting;

import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.setting.SettingGroup;

public class GroupComponent extends SettingComponent {

    private final SettingGroup setting;

    private boolean open;

    public GroupComponent(SettingGroup setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {

    }

    @Override
    public int getTotalHeight() {
        return 0;
    }
}
