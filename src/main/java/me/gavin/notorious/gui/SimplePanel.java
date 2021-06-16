package me.gavin.notorious.gui;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.AbstractComponent;
import me.gavin.notorious.hack.Hack;

import java.util.ArrayList;

public class SimplePanel extends AbstractComponent {

    public final SimpleHeader header;

    public final ArrayList<SimplePanelButton> buttons;

    public SimplePanel(Hack.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.header = new SimpleHeader(category.name(), x, y, width, 16);
        this.buttons = new ArrayList<>();

        int yOffset = header.height;
        for (Hack hack : Notorious.INSTANCE.hackManager.getHacksFromCategory(category)) {
            buttons.add(new SimplePanelButton(hack, x, yOffset, width, 14));
            yOffset += 14;
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        header.render(mouseX, mouseY, partialTicks);
        for (SimplePanelButton button : buttons) {
            button.render(mouseX, mouseY, partialTicks);
        }
        updatePositions(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (header.isMouseInside(mouseX, mouseY) && mouseButton == 0)
            header.mouseClicked(mouseX, mouseY, mouseButton);

        for (SimplePanelButton button : buttons) {
            button.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        header.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
    }

    private void updatePositions(int mouseX, int mouseY) {
        header.updateDragPosition(mouseX, mouseY);
        this.x = header.x;
        this.y = header.y;

        for (SimplePanelButton button : buttons) {
            button.x = x;
            button.y = this.y + button.startingY;
        }
    }
}
