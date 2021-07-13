package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.hack.hacks.client.Font;
import me.gavin.notorious.setting.ColorSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class ColorComponent extends SettingComponent {

    private final ColorSetting setting;

    private final ArrayList<SliderComponent> sliderComponents;

    private boolean open;

    public ColorComponent(ColorSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
        sliderComponents = new ArrayList<>();
        sliderComponents.add(new SliderComponent(setting.getRed(), x, y, width, height));
        sliderComponents.add(new SliderComponent(setting.getGreen(), x, y, width, height));
        sliderComponents.add(new SliderComponent(setting.getBlue(), x, y, width, height));
        sliderComponents.add(new SliderComponent(setting.getAlpha(), x, y, width, height));
    }

    private Font font = (Font) Notorious.INSTANCE.hackManager.getHack(Font.class);

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        Gui.drawRect(x, y, x + width, y + height, setting.getAsColor().getRGB());

        if (font.isEnabled()) {
            Notorious.INSTANCE.fontRenderer.drawStringWithShadow(setting.getName(), x + 3, y + 3, Color.WHITE);
            Notorious.INSTANCE.fontRenderer.drawStringWithShadow(open ? "-" : "+", x + width - 8, y + 3, Color.WHITE);
        } else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName(), x + 3, y + 3, -1);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(open ? "-" : "+", x + width - 8, y + 3, -1);
        }

        if (open) {
            int yOffset = y + height;
            for (SliderComponent component : sliderComponents) {
                component.x = x;
                component.y = yOffset;
                component.render(mouseX, mouseY, partialTicks);
                yOffset += component.height;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY)) {
            open = !open;
        }

        if (open) {
            for (SliderComponent component : sliderComponents) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (open) {
            for (SliderComponent component : sliderComponents) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) { }

    @Override
    public int getTotalHeight() {
        if (open) {
            return height + (height * 4);
        } else {
            return height;
        }
    }
}
