package dev.notorious.client.gui.buttons.impl;

import dev.notorious.client.gui.buttons.Button;
import dev.notorious.client.gui.manage.Frame;
import dev.notorious.client.hacks.Hack;
import dev.notorious.client.util.RenderUtils;

import java.awt.*;

public class HackButton extends Button {
    private final Hack hack;

    private boolean open = false;

    public HackButton(final Hack hack, final int x, final int y, final Frame parent){
        super(x, y, parent);
        this.hack = hack;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderUtils.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), new Color(0, 0, 0, 110));
        RenderUtils.drawOutlinedRect(getX() + 1, getY() + 0.5f, getX() + getWidth() - 1, getY() + getHeight() - 0.5f, 0.5f, hack.isToggled() ? new Color(255, 0, 0) : new Color(0, 0, 0, 0), new Color(0, 0, 0));
        mc.fontRenderer.drawStringWithShadow(hack.getName(), getX() + 4, getY() + 3.5f, hack.isToggled() ? Color.WHITE.getRGB() : Color.LIGHT_GRAY.getRGB());
        mc.fontRenderer.drawStringWithShadow(open ? "-" : "+", getX() + getWidth() - 10, getY() + 3.5f, hack.isToggled() ? Color.WHITE.getRGB() : Color.LIGHT_GRAY.getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovering(mouseX, mouseY)){
            if (mouseButton == 0){
                hack.toggle(true);
            }

            if (mouseButton == 1){
                open = !open;
            }
        }
    }

    @Override
    public void onGuiClosed(){
        open = false;
    }
}
