package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.hack.hacks.client.ClickGUI;
import me.gavin.notorious.hack.hacks.client.Font;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.Setting;
import me.gavin.notorious.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ColorComponent extends SettingComponent {

    private final ColorSetting setting;

    private boolean open = false;

    public ColorComponent( ColorSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
        redSlider = new SliderComponent(setting.getRed(), x, y, width, height);
        greenSlider = new SliderComponent(setting.getGreen(), x, y, width, height);
        blueSlider = new SliderComponent(setting.getBlue(), x, y, width, height);
        alphaSlider = new SliderComponent(setting.getAlpha(), x, y, width, height);
    }

    SliderComponent redSlider;
    SliderComponent greenSlider;
    SliderComponent blueSlider;
    SliderComponent alphaSlider;

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        int color;
        Font font = ((Font)Notorious.INSTANCE.hackManager.getHack(Font.class));
        float time = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).length.getValue();
        float saturation = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).saturation.getValue();
        if(((ClickGUI) Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).colorMode.getMode().equals("Rainbow")) {
            color = ColorUtil.getRainbow(time, saturation);
        }else {
            color = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).guiColor.getAsColor().getRGB();
        }
        if(!open) {
            Gui.drawRect(x, y, x + width, y + height, setting.getAsColor().getRGB());
            Gui.drawRect(x, y, x + 2, y + height, color);
            if(font.isEnabled()) {
                Notorious.INSTANCE.fontRenderer.drawStringWithShadow(setting.getName(), x + 9f, y + 5f, Color.WHITE);
                Notorious.INSTANCE.fontRenderer.drawStringWithShadow("+", x + width - 8f, y + 5f, Color.WHITE);
            }else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName(), x + 9f, y + 5f, new Color(255, 255, 255).getRGB());
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("+", x + width - 8f, y + 5f, new Color(255, 255, 255).getRGB());
            }
        }else {
            Gui.drawRect(x, y, x + width, y + height, setting.getAsColor().getRGB());
            Gui.drawRect(x, y, x + 2, y + height, color);
            if(font.isEnabled()) {
                Notorious.INSTANCE.fontRenderer.drawStringWithShadow(setting.getName(), x + 9f, y + 5f, Color.WHITE);
                Notorious.INSTANCE.fontRenderer.drawStringWithShadow("-", x + width - 8f, y + 5f, Color.WHITE);
            }else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName(), x + 9f, y + 5f, new Color(255, 255, 255).getRGB());
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("-", x + width - 8f, y + 5f, new Color(255, 255, 255).getRGB());
            }
            redSlider.render(mouseX, mouseY, partialTicks);
            greenSlider.render(mouseX, mouseY, partialTicks);
            blueSlider.render(mouseX, mouseY, partialTicks);
            alphaSlider.render(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isMouseInside(mouseX, mouseY) && mouseButton == 1)
            open = !open;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void keyTyped(char keyChar, int keyCode) { }
}
