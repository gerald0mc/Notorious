package dev.notorious.client.gui.manage;

import dev.notorious.client.Notorious;
import dev.notorious.client.gui.buttons.Button;
import dev.notorious.client.gui.buttons.impl.HackButton;
import dev.notorious.client.hacks.Hack;
import dev.notorious.client.util.IMinecraft;
import dev.notorious.client.util.RenderUtils;

import java.awt.*;
import java.util.ArrayList;

public class Frame implements IMinecraft {
    private final String title;

    private final ArrayList<Button> buttons;

    private int x;
    private int y;
    private final int width;
    private int height;

    public Frame(Hack.Category category, int x, int y){
        this.title = category.getName();

        this.x = x;
        this.y = y;
        this.width = 110;
        this.height = 240;

        buttons = new ArrayList<>();

        int offset = 17;
        for (Hack hack : Notorious.HACK_MANAGER.getHacks(category)){
            buttons.add(new HackButton(hack, x, y + offset, this));
            offset += 14;
        }

        height = offset;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        RenderUtils.drawOutlinedRect(x, y, x + width, y + 16, 0.5f,  new Color(255, 0, 0), new Color(0, 0, 0));
        RenderUtils.drawRect(x, y + 16, x + width, y + 17, new Color(0, 0, 0, 110));
        RenderUtils.drawRect(x, y + height, x + width, y + height + 1, new Color(0, 0, 0, 110));
        mc.fontRenderer.drawStringWithShadow(title, x + 3, y + 4, -1);

        for (Button button : buttons){
            button.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Button button : buttons){
            button.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state){
        for (Button button : buttons){
            button.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        for (Button button : buttons){
            button.keyTyped(typedChar, keyCode);
        }
    }

    public void onGuiClosed(){
        for (Button button : buttons){
            button.onGuiClosed();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
