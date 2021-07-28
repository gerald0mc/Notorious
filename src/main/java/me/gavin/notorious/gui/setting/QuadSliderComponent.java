package me.gavin.notorious.gui.setting;

import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.setting.NumSetting;

public abstract class QuadSliderComponent extends SettingComponent {

    private final NumSetting xSetting;
    private final NumSetting ySetting;

    private boolean dragging;
    protected float sliderWidth;
    protected float sliderHeight;

    public QuadSliderComponent(NumSetting xSetting, NumSetting ySetting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.xSetting = xSetting;
        this.ySetting = ySetting;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY) && mouseButton == 0)
            dragging = true;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0)
            dragging = false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        updateLogicX(mouseX);
        updateLogicY(mouseY);
        drawPicker(mouseX, mouseY, partialTicks);
    }

    public abstract void drawPicker(int mouseX, int mouseY, float partialTicks);

    private void updateLogicX(int mouseX) {
        float difference = Math.min(width, Math.max(0, mouseX - x));
        float min = xSetting.getMin();
        float max = xSetting.getMax();
        float value = xSetting.getValue();
        sliderWidth = width * (value - min) / (max - min);
        if (dragging) {
            if (difference == 0) {
                xSetting.setValue(min);
            } else {
                float val = SliderComponent.roundToPlace(difference / width * (max - min) + min, 3);
                xSetting.setValue(val);
            }
        }
    }

    private void updateLogicY(int mouseY) {
        float difference = Math.min(height, Math.max(0, mouseY - y));
        float min = ySetting.getMin();
        float max = ySetting.getMax();
        float value = ySetting.getValue();
        sliderHeight = height * (value - min) / (max - min);
        if (dragging) {
            if (difference == 0) {
                ySetting.setValue(min);
            } else {
                float val = SliderComponent.roundToPlace(difference / height * (max - min) + min, 3);
                ySetting.setValue(val);
            }
        }
    }
}
