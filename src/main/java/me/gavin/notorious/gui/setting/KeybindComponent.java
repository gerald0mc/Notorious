package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.Bindable;
import me.gavin.notorious.gui.api.SettingComponent;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class KeybindComponent extends SettingComponent {

    private final Bindable setting;

    private boolean listening = false;

    public KeybindComponent(Bindable setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0xCF000000);
        if (listening) {
            Notorious.INSTANCE.fontRenderer.drawStringWithShadow("Listening...", x + 3f, y + 3f, Color.WHITE);
        } else {
            Notorious.INSTANCE.fontRenderer.drawStringWithShadow("<" + Keyboard.getKeyName(setting.getBind()) + ">", x + 3f, y + 3f, Color.WHITE);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY) && mouseButton == 0) {
            listening = !listening;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
        if (listening) {
            listening = false;
            if (keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_BACK) {
                setting.setBind(0);
                return;
            }

            setting.setBind(keyCode);
        }
    }
}