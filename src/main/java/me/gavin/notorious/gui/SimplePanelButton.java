package me.gavin.notorious.gui;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.AbstractComponent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.util.MiscUtil;

import java.awt.*;

public class SimplePanelButton extends AbstractComponent {

    private final Hack hack;

    public final int startingY;

    public SimplePanelButton(Hack hack, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.startingY = y;
        this.hack = hack;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        int textColor = -1;

        if (hack.isEnabled())
            textColor = 0xFF00FF00;

        Notorious.INSTANCE.fontRenderer.drawStringWithShadow(hack.getName(), x + 2, y + 2, new Color(textColor));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY)) {
            hack.toggle();
            MiscUtil.playGuiClick();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
    }

    public Hack getHack() {
        return this.hack;
    }
}
