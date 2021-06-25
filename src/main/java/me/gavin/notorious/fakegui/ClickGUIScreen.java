package me.gavin.notorious.fakegui;

import me.gavin.notorious.hack.Hack;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGUIScreen extends GuiScreen {

    public static ClickGUIScreen INSTANCE = new ClickGUIScreen();

    ArrayList<Frame> frames;

    public ClickGUIScreen() {
        frames = new ArrayList<>();
        int offset = 0;
        for(Hack.Category category : Hack.Category.values()) {
            frames.add(new Frame(category, 10 + offset, 20));
            offset += 110;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for(Frame frame : frames) {
            frame.render(mouseX, mouseY);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for(Frame frame : frames) {
            frame.onClick(mouseX, mouseY, mouseButton);
        }
    }
}
