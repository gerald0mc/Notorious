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

    public ClickGuiScreen() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
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