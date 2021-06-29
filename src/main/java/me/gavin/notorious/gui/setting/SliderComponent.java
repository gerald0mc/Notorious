package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderComponent extends SettingComponent {

    private final NumSetting setting;

    private float sliderWidth;

    private boolean draggingSlider;

    public SliderComponent(NumSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        updateSliderLogic(mouseX, mouseY);
        Gui.drawRect(x, y, x + width, y + height, 0xCF000000);
        Gui.drawRect(x, y, x + (int) sliderWidth, y + height, 0xFFFF0000);
        Notorious.INSTANCE.fontRenderer.drawStringWithShadow(setting.getName() + " <" + setting.getValue() + ">", x + 3f, y + 3f, Color.WHITE);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY) && mouseButton == 0) {
            draggingSlider = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && draggingSlider)
            draggingSlider = false;
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
    }

    private void updateSliderLogic(int mouseX, int mouseY) {
        float diff = Math.min(width, Math.max(0, mouseX - x));
        float min = setting.getMin();
        float max = setting.getMax();
        sliderWidth = width * (setting.getValue() - min) / (max - min);
        if (draggingSlider) {
            if (diff == 0) {
                setting.setValue(setting.getMin());
            } else {
                float value = roundToPlace(diff / width * (max - min) + min, 1);
                setting.setValue(value);
            }
        }
    }

    private float roundToPlace(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

       ; ; ; ; ;
     ;           ;
    ;         ; ; ; ; ; ; ; ; ; ; ; ; ; ;
    ;                                  ; ;
    ;         ; ; ; ; ; ; ; ; ; ; ; ; ; ;
     ;           ;
       ; ; ; ; ;
}