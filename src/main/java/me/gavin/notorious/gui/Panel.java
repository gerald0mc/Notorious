package me.gavin.notorious.gui;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.*;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.hacks.client.ClickGUI;
import me.gavin.notorious.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class Panel extends AbstractDragComponent {

    private final ArrayList<AbstractToggleContainer> buttons;

    private final Hack.Category category;

    public Panel(Hack.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.buttons = new ArrayList<>();
        this.category = category;

        for (Hack hack : Notorious.INSTANCE.hackManager.getHacksFromCategory(category)) {
            buttons.add(new Button(hack, x, y, width, 15));
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        float time = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).length.getValue();
        float saturation = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).saturation.getValue();
        int color;
        if(((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).colorMode.getMode().equals("Rainbow")) {
            color = ColorUtil.getRainbow(time, saturation);
        }else {
            color = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).guiColor.getAsColor().getRGB();
        }
        Gui.drawRect(x, y, x + width, y + height, color);
        if(((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).customFont.isEnabled()) {
            Notorious.INSTANCE.fontRenderer.drawStringWithShadow(category.name(), x + 3f, y + 3f, Color.WHITE);
        }else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(category.name(), x + 3f, y + 3f, new Color(255, 255, 255).getRGB());
        }

        int yOffset = height;
        for (AbstractToggleContainer button : buttons) {
            button.x = this.x;
            button.y = this.y + yOffset;
            yOffset += button.getTotalHeight();
            button.render(mouseX, mouseY, partialTicks);
        }

        updateDragPosition(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY) && mouseButton == 0) {
            startDragging(mouseX, mouseY);
        }

        for (AbstractToggleContainer button : buttons) {
            button.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            stopDragging(mouseX, mouseY);
        }

        for (AbstractToggleContainer button : buttons) {
            button.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
        for (AbstractToggleContainer button : buttons) {
            button.keyTyped(keyChar, keyCode);
        }
    }

    public ArrayList<AbstractToggleContainer> getButtons() {
        return buttons;
    }
}