package me.gavin.notorious.gui;

import me.gavin.notorious.hack.Hack;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Gav06
 * @since 6/15/2021
 */

public class ClickGuiScreen extends GuiScreen {

    private final ArrayList<Panel> panels = new ArrayList<>();

    public ClickGuiScreen() {
        int xoffset = 0;
        for (Hack.Category category : Hack.Category.values()) {
            panels.add(new Panel(category, 10 + xoffset, 10, 100, 15));
            xoffset += 110;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (Panel panel : panels) {
            panel.render(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Panel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Panel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char keychar, int keycode) throws IOException {
        for (Panel panel : panels) {
            panel.keyTyped(keychar, keycode);
        }

        super.keyTyped(keychar, keycode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}