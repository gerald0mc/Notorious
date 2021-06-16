package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.AbstractComponent;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.util.MiscUtil;

import java.awt.*;

public class BooleanWidget extends AbstractComponent {

    public final BooleanSetting value;

    public BooleanWidget(BooleanSetting value, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.value = value;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        int color = -1;

        if (value.isEnabled())
            color = 0x00FF00;

        Notorious.INSTANCE.fontRenderer.drawStringWithShadow(value.getName(), x + 2f, y + 2f, new Color(color));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY) && mouseButton == 0) {
            value.toggle();
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
