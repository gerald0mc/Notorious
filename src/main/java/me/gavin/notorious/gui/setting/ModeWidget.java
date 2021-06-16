package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.AbstractComponent;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.util.MiscUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ModeWidget extends AbstractComponent {

    private final ModeSetting setting;

    public ModeWidget(ModeSetting modeSetting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = modeSetting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0x90000000);
        Notorious.INSTANCE.fontRenderer.drawStringWithShadow(setting.getName() + " <" + setting.getMode() + ">", x + 2, y + 2, new Color(-1));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY)) {
            setting.cycle(mouseButton == 1);
            MiscUtil.playGuiClick();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
    }
}
