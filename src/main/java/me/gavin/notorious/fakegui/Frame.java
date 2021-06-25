package me.gavin.notorious.fakegui;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.manager.HackManager;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class Frame {

    int x, y, width, height;
    Hack.Category category;

    ArrayList<ModuleButton> buttons;

    public Frame(Hack.Category category, int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 240;
        this.category = category;

        buttons = new ArrayList<>();
        int offsetY = 14;
        for(Hack hack : Notorious.INSTANCE.hackManager.getHacksFromCategory(category)) {
            buttons.add(new ModuleButton(hack, x, y + offsetY, this));
            offsetY += 14;
        }

        this.height = offsetY;
    }

    public void render(int mouseX, int mouseY) {
        Notorious.INSTANCE.fontRenderer.drawStringWithShadow(category.toString(), x + 2, y + 2, new Color(255, 255, 255, 255));
        Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 100).getRGB());
        for(ModuleButton button : buttons) {
            button.draw(mouseX, mouseY);
        }
    }

    public void onClick(int x, int y, int moduleButton) {
        for(ModuleButton button : buttons) {
            button.onClick(x, y, moduleButton);
        }
    }
}
