package me.gavin.notorious.gui;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.AbstractToggleContainer;
import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.gui.setting.*;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.hacks.client.ClickGUI;
import me.gavin.notorious.hack.hacks.client.Font;
import me.gavin.notorious.setting.*;
import me.gavin.notorious.stuff.IMinecraft;
import me.gavin.notorious.util.ColorUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class Button extends AbstractToggleContainer implements IMinecraft {

    private final Hack hack;

    public Button(Hack hack, int x, int y, int width, int height) {
        super(hack, x, y, width, height);
        this.hack = hack;
        for (Setting setting : hack.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new BooleanComponent((BooleanSetting) setting, x, y, width, height));
            }else if (setting instanceof ModeSetting) {
                components.add(new ModeComponent((ModeSetting) setting, x, y, width, height));
            }else if (setting instanceof NumSetting) {
                components.add(new SliderComponent((NumSetting) setting, x, y, width, height));
            }else if (setting instanceof ColorSetting) {
                components.add(new SexyColorComponent((ColorSetting) setting, x, y, width, height));
            }else if (setting instanceof StringSetting) {
                components.add(new StringComponent((StringSetting) setting, x, y, width, height));
            }
        }

        components.add(new KeybindComponent(hack, x + 5, y, width, height));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Font font = Notorious.INSTANCE.hackManager.getHack(Font.class);
        int renderYOffset = height;
        int intRainbow;
        Color colorRainbow;
        float time = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).length.getValue();
        float saturation = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).saturation.getValue();
        if(Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).colorMode.getMode().equals("Rainbow")) {
            intRainbow = ColorUtil.getRainbow(time, saturation);
        }else {
            intRainbow = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor().getRGB();
        }
        if(Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).colorMode.getMode().equals("Rainbow")) {
            colorRainbow = ColorUtil.colorRainbow((int) time, saturation, 1f);
        }else {
            colorRainbow = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor();
        }
        Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, (int) Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).backgroundAlpha.getValue()).getRGB());
        if(open) {
            if(font.isEnabled()) {
                Notorious.INSTANCE.fontRenderer.drawStringWithShadow("-", x + width - 8f, y + 2f, Color.WHITE);
            }else {
                mc.fontRenderer.drawStringWithShadow("-", x + width - 8f, y + 2f, new Color(255, 255, 255).getRGB());
            }
        }else {
            if(font.isEnabled()) {
                Notorious.INSTANCE.fontRenderer.drawStringWithShadow("+", x + width - 8f, y + 4f, Color.WHITE);
            }else {
                mc.fontRenderer.drawStringWithShadow("+", x + width - 8f, y + 2f, new Color(255, 255, 255).getRGB());
            }
        }
        if(font.isEnabled()) {
            Notorious.INSTANCE.fontRenderer.drawStringWithShadow(hack.getName(), x + 2f, y + 4f, hack.isEnabled() ? colorRainbow : new Color(255, 255, 255, 255));
        }else {
            mc.fontRenderer.drawStringWithShadow(hack.getName(), x + 2f, y + 2f, hack.isEnabled() ? intRainbow : -1);
        }

        if (open) {
            for (SettingComponent component : components) {
                component.x = this.x;
                component.y = this.y + renderYOffset;
                renderYOffset += component.getTotalHeight();
                component.render(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                hack.toggle();
            }else if (mouseButton == 1) {
                open = !open;
            }else if(mouseButton == 2) {
                if(hack.isDrawn()) {
                    hack.setUndrawn();
                    Notorious.INSTANCE.messageManager.sendMessage(hack.getName() + " is no longer Drawn.");
                }else {
                    hack.setDrawn();
                    Notorious.INSTANCE.messageManager.sendMessage(hack.getName() + " is now Drawn.");
                }
            }
        }

        if (open) {
            for (SettingComponent component : components) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (open) {
            for (SettingComponent component : components) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
        if (open) {
            for (SettingComponent component : components) {
                component.keyTyped(keyChar, keyCode);
            }
        }
    }

    @Override
    public int getTotalHeight() {
        if (open) {
            int h = 0;
            for (SettingComponent component : components) {
                h += component.getTotalHeight();
            }
            return height + h;
        } else {
            return height;
        }
    }
}