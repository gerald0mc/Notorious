package me.gavin.notorious.gui.api;

public abstract class AbstractDragComponent extends AbstractComponent {
    public AbstractDragComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    private int dragX, dragY;
    public boolean dragging;

    public void startDragging(int mouseX, int mouseY) {
        if (isMouseInside(mouseX, mouseY)) {
            dragging = true;
            dragX = mouseX - x;
            dragY = mouseY - y;
        }
    }

    public void stopDragging(int mouseX, int mouseY) {
        this.dragging = false;
    }

    public void updateDragPosition(int mouseX, int mouseY) {
        if (this.dragging) {
            x = (mouseX - dragX);
            y = (mouseY - dragY);
        }
    }
}
