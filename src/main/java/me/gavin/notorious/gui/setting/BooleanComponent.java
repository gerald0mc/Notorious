package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.hack.hacks.client.ClickGUI;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class BooleanComponent extends SettingComponent {

    private final BooleanSetting setting;

    public BooleanComponent(BooleanSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, setting.isEnabled() ? 0xCFFF0000 : 0xCF000000);
        Gui.drawRect(x, y, x + 2, y + height, new Color(255, 0, 0, 255).getRGB());
        if(((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).customFont.isEnabled()) {
            Notorious.INSTANCE.fontRenderer.drawStringWithShadow(setting.getName(), x + 9f, y + 3f, Color.WHITE);
        }else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName(), x + 9f, y + 3f, new Color(255, 255, 255).getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY) && mouseButton == 0)
            setting.toggle();
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void keyTyped(char keyChar, int keyCode) { }
}