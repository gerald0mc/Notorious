package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.AbstractComponent;
import me.gavin.notorious.gui.api.Rect;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class SliderWidget extends AbstractComponent {

    private final NumSetting setting;
    private final float min, max, increment;
    private boolean open;

    public SliderWidget(NumSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
        this.min = setting.getMin();
        this.max = setting.getMax();
        this.increment = setting.getIncrement();
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

    public NumSetting getSetting() {
        return this.setting;
    }
}
