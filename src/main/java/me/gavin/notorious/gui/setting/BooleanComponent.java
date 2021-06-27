package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.setting.BooleanSetting;
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
        Notorious.INSTANCE.fontRenderer.drawStringWithShadow(setting.getName(), x + 3f, y + 3f, Color.WHITE);
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