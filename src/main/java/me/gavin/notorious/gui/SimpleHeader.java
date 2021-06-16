package me.gavin.notorious.gui;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.AbstractDragComponent;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class SimpleHeader extends AbstractDragComponent {

    private final String title;

    public SimpleHeader(String title, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.title = title;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0xCF000000);
        Notorious.INSTANCE.fontRenderer.drawStringWithShadow(title, x + 2, y + 2, new Color(-1));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            startDragging(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            stopDragging(mouseX, mouseY);
        }
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) { }
}
