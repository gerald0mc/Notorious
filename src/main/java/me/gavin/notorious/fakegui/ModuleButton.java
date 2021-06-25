package me.gavin.notorious.fakegui;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.Hack;

import java.awt.*;

public class ModuleButton {

    int x, y, width, height;
    Hack hack;
    Frame frame;

    public ModuleButton(Hack hack, int x, int y, Frame frame) {
        this.hack = hack;
        this.x = x;
        this.y = y;
        this.frame = frame;
        this.width = frame.width;
        this.height = 14;
    }

    public void draw(int mouseX, int mouseY) {
        if(hack.isEnabled()) {
            Notorious.INSTANCE.fontRenderer.drawStringWithShadow(hack.getName(), x + 6, y + 2, new Color(255, 0, 0, 255));
        }else {
            Notorious.INSTANCE.fontRenderer.drawStringWithShadow(hack.getName(), x + 6, y + 2, new Color(255, 255, 255, 255));
        }
    }

    public void onClick(int x, int y, int button) {
        if(x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
            hack.toggle();
        }
    }
}
