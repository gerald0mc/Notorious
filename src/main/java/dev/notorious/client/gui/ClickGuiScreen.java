package dev.notorious.client.gui;

import dev.notorious.client.Notorious;
import dev.notorious.client.gui.manage.Frame;
import dev.notorious.client.hacks.Hack;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGuiScreen extends GuiScreen {
    private final ArrayList<Frame> frames;

    public ClickGuiScreen(){
        this.frames = new ArrayList<>();

        int offset = 0;
        for (Hack.Category category : Hack.Category.values()){
            frames.add(new Frame(category, 20 + offset, 20));
            offset += 120;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (Frame frame : frames){
            frame.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (Frame frame : frames){
            frame.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state){
        super.mouseReleased(mouseX, mouseY, state);
        for (Frame frame : frames){
            frame.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (Frame frame : frames){
            frame.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void onGuiClosed(){
        super.onGuiClosed();
        Notorious.HACK_MANAGER.disableHack("GUI", false);
        for (Frame frame : frames){
            frame.onGuiClosed();
        }
    }

    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }
}