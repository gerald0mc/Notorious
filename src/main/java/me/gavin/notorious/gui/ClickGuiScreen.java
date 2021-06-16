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

    public final ArrayList<SimplePanel> panels;

    public ClickGuiScreen() {
        this.panels = new ArrayList<>();

        int xOffset = 0;
        for (Hack.Category category : Hack.Category.values()) {
            panels.add(new SimplePanel(category, 15 + xOffset, 15, 85, 200));
            xOffset += 95;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        for (SimplePanel panel : panels) {
            panel.render(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (SimplePanel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (SimplePanel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char keychar, int keycode) throws IOException {
        super.keyTyped(keychar, keycode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}