package me.gavin.notorious.gui.api;

/**
 * @author Gav06
 * @since 6/15/2021
 */

public abstract class AbstractComponent extends Rect {
    public AbstractComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public abstract void render(int mouseX, int mouseY, float partialTicks);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);

    public abstract void keyTyped(char keyChar, int keyCode);

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
