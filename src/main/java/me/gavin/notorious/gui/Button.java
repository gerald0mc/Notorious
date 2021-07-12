package me.gavin.notorious.gui;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.AbstractToggleContainer;
import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.gui.setting.BooleanComponent;
import me.gavin.notorious.gui.setting.KeybindComponent;
import me.gavin.notorious.gui.setting.ModeComponent;
import me.gavin.notorious.gui.setting.SliderComponent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.hacks.client.ClickGUI;
import me.gavin.notorious.hack.hacks.client.Font;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.setting.Setting;
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
            } else if (setting instanceof ModeSetting) {
                components.add(new ModeComponent((ModeSetting) setting, x, y, width, height));
            } else if (setting instanceof NumSetting) {
                components.add(new SliderComponent((NumSetting) setting, x, y, width, height));
            }
        }

        components.add(new KeybindComponent(hack, x + 5, y, width, height));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Font font = ((Font)Notorious.INSTANCE.hackManager.getHack(Font.class));
        int renderYOffset = height;
        int intRainbow;
        float time = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).length.getValue();
        float saturation = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).saturation.getValue();
        if(((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).colorMode.getMode().equals("Rainbow")) {
            intRainbow = ColorUtil.getRainbow(time, saturation);
        }else {
            intRainbow = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).guiColor.getAsColor().getRGB();
        }
        Gui.drawRect(x, y, x + width, y + height, isMouseInside(mouseX, mouseY) ? 0xCC0C0C0C : 0xCC000000);
        Gui.drawRect(x, y, x + width, y + height, hack.isEnabled() ? intRainbow : 0xCC000000);
        if(open) {
            if(font.isEnabled()) {
                Notorious.INSTANCE.fontRenderer.drawStringWithShadow("-", x + width - 8f, y + 5f, Color.WHITE);
            }else {
                mc.fontRenderer.drawStringWithShadow("-", x + width - 8f, y + 5f, new Color(255, 255, 255).getRGB());
            }
        }else {
            if(font.isEnabled()) {
                Notorious.INSTANCE.fontRenderer.drawStringWithShadow("+", x + width - 8f, y + 5f, Color.WHITE);
            }else {
                mc.fontRenderer.drawStringWithShadow("+", x + width - 8f, y + 5f, new Color(255, 255, 255).getRGB());
            }
        }
        if(font.isEnabled()) {
            Notorious.INSTANCE.fontRenderer.drawStringWithShadow(hack.getName(), x + 2f, y + 5f, Color.WHITE);
        }else {
            mc.fontRenderer.drawStringWithShadow(hack.getName(), x + 2f, y + 5f, new Color(255, 255, 255).getRGB());
        }

        if (open) {
            for (SettingComponent component : components) {
                component.x = this.x;
                component.y = this.y + renderYOffset;
                renderYOffset += component.height;
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
                h += component.height;
            }
            return height + h;
        } else {
            return height;
        }
    }
}