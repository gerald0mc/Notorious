package dev.notorious.client.gui.buttons;

import dev.notorious.client.gui.manage.Frame;
import net.minecraft.client.Minecraft;

public class Button {
    protected static final Minecraft mc = Minecraft.getMinecraft();

    private boolean visible;

    private int x;
    private int y;
    private int width;
    private int height;

    private final Frame parent;
    public Button(final int x, final int y, final Frame parent){
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.width = parent.getWidth();
        this.height = 14;
        this.visible = true;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks){

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    public void mouseReleased(int mouseX, int mouseY, int state){

    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public void onGuiClosed(){

    }

    public void update(int mouseX, int mouseY, float partialTicks){

    }

    public boolean isHovering(int mouseX, int mouseY){
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public boolean isVisible(){
        return visible;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Frame getParent() {
        return parent;
    }
}