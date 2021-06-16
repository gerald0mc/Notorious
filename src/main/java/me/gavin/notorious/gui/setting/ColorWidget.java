package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.AbstractComponent;
import me.gavin.notorious.gui.api.Rect;
import me.gavin.notorious.setting.ColorSetting;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class ColorWidget extends AbstractComponent {

    private final ColorSetting setting;

    private boolean open;
    private final int openHeight;
    private final Rect headerArea;

    private final ArrayList<SliderWidget> colorSliders;

    public ColorWidget(ColorSetting color, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = color;
        this.headerArea = new Rect(x, y, width, height);
        openHeight = height * 4;
        this.colorSliders = new ArrayList<>();
        colorSliders.add(new SliderWidget(color.getRed(), x, y, width, height));
        colorSliders.add(new SliderWidget(color.getGreen(), x, y, width, height));
        colorSliders.add(new SliderWidget(color.getRed(), x, y, width, height));
        colorSliders.add(new SliderWidget(color.getRed(), x, y, width, height));

    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0x90000000);
        Notorious.INSTANCE.fontRenderer.drawStringWithShadow(setting.getName(), x + 2, y + 2, new Color(-1));

        this.headerArea.x = x;
        this.headerArea.y = y;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (headerArea.isMouseInside(mouseX, mouseY)) {
            if (!open) {
                this.height = openHeight;
            } else {
                this.height = openHeight / 4;
            }
            open = !open;
        }

        if (isMouseInside(mouseX, mouseY)) {

        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {

    }
}
