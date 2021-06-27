package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.setting.ModeSetting;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ModeComponent extends SettingComponent {

    private final ModeSetting setting;

    public ModeComponent(ModeSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0xCF000000);
        Notorious.INSTANCE.fontRenderer.drawStringWithShadow(setting.getName() + " <" + setting.getMode() + ">", x + 3f, y + 3f, Color.WHITE);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                setting.cycle(false);
            } else if (mouseButton == 1) {
                setting.cycle(true);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void keyTyped(char keyChar, int keyCode) { }
}
