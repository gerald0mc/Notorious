package me.gavin.notorious.gui.setting;

import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ColorComponent extends SettingComponent {

    private boolean open;

    private final ColorSetting colorSetting;

    protected final Minecraft mc = Minecraft.getMinecraft();

    private final CustomSliderComponent hueSlider;
    private final CustomSliderComponent alphaSlider;
    private final QuadSliderComponent pickerSliders;

    public ColorComponent(ColorSetting colorSetting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.colorSetting = colorSetting;
        hueSlider = new CustomSliderComponent(colorSetting.getHue(), x, y, width, 12) {
            @Override
            public void drawCustomSlider(int mouseX, int mouseY, float partialTicks) {
                drawHueSlider(x, y, width, height);
                Gui.drawRect(x + (int)sliderWidth - 1, y, x + (int)sliderWidth + 1, y + height, 0xFFFFFFFF);
            }
        };
        alphaSlider = new CustomSliderComponent(colorSetting.getAlpha(), x, y, width, 12) {
            @Override
            public void drawCustomSlider(int mouseX, int mouseY, float partialTicks) {
                RenderUtil.drawSideGradientRect(x, y, x + width, y + height, Color.BLACK.getRed(), colorSetting.getColor().getRGB());
                Gui.drawRect(x + (int)sliderWidth - 1, y, x + (int)sliderWidth + 1, y + height, 0xFFFFFFFF);
            }
        };
        // very broken chinese code kys
        pickerSliders = new QuadSliderComponent(colorSetting.getHSB()[1], colorSetting.getHSB()[2], x, y, width - 2, width - 2) {
            @Override
            public int getTotalHeight() {
                return height;
            }

            @Override
            public void keyTyped(char keyChar, int keyCode) { }

            @Override
            public void drawPicker(int mouseX, int mouseY, float partialTicks) {
                RenderUtil.drawMultiColoredRect(x, y, x + width, y + height, Color.WHITE, Color.getHSBColor(colorSetting.getHSB()[0], 1f, 1f), Color.BLACK, Color.BLACK);
                Gui.drawRect(x + (int)sliderWidth - 1, y + (int)sliderHeight - 1, x + (int)sliderWidth + 1, y + (int)sliderHeight + 1, 0xFF909090);
            }
        };
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0x90BF0000);
        mc.fontRenderer.drawStringWithShadow(colorSetting.getName(), x + 2f, y + 2f, -1);
        if (open) {
            pickerSliders.x = (this.x + 1);
            pickerSliders.y = (this.y + this.height);
            hueSlider.x = (this.x + 1);
            hueSlider.y = (this.y + this.height + pickerSliders.getTotalHeight());
            alphaSlider.x = (this.x);
            alphaSlider.y = (this.y +  this.height + pickerSliders.height + hueSlider.height);
            hueSlider.render(mouseX, mouseY, partialTicks);
            alphaSlider.render(mouseX, mouseY, partialTicks);
            pickerSliders.render(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY)) {
            open = !open;
        }
        if (open) {
            hueSlider.mouseClicked(mouseX, mouseY, mouseButton);
            alphaSlider.mouseClicked(mouseX, mouseY, mouseButton);
            pickerSliders.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (open) {
            hueSlider.mouseReleased(mouseX, mouseY, mouseButton);
            alphaSlider.mouseReleased(mouseX, mouseY, mouseButton);
            pickerSliders.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public int getTotalHeight() {
        if (open) {
            return height + pickerSliders.height + alphaSlider.height + hueSlider.height;
        } else {
            return height;
        }
    }

    @Override
    public void keyTyped(char keyChar, int keycode) { }

    public void drawHueSlider(int x, int y, int width, int height) {
        int step = 0;

        if (height > width) {
            Gui.drawRect(x, y, x + width, y + 4, 0xFFFF0000);
            y += 4;

            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                RenderUtil.drawSideGradientRect(x, y + step * (height / 6), x + width, y + (step + 1) * (height / 6), previousStep, nextStep);
                step++;
            }
        } else {
            for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
                int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
                RenderUtil.drawSideGradientRect(x + step * (width / 6), y, x + (step + 1) * (width / 6), y + height, previousStep, nextStep);
                step++;
            }
        }
    }
}