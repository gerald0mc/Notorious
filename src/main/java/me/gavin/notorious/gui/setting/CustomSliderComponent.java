package me.gavin.notorious.gui.setting;

import me.gavin.notorious.setting.NumSetting;

public abstract class CustomSliderComponent extends SliderComponent {
    public CustomSliderComponent(NumSetting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.updateSliderLogic(mouseX, mouseY);
        drawCustomSlider(mouseX, mouseY, partialTicks);
    }

    public abstract void drawCustomSlider(int mouseX, int mouseY, float partialTicks);
}
