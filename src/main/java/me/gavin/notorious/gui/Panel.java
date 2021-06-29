package me.gavin.notorious.gui;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.*;
import me.gavin.notorious.hack.Hack;

import java.util.ArrayList;

public class Panel extends AbstractDragComponent {

    private final ArrayList<AbstractToggleContainer> buttons;

    public Panel(Hack.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.buttons = new ArrayList<>();

        for (Hack hack : Notorious.INSTANCE.hackManager.getHacksFromCategory(category)) {
            buttons.add(new Button(hack, x, y, width, height));
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {

    }

    public ArrayList<AbstractToggleContainer> getButtons() {
        return buttons;
    }
}