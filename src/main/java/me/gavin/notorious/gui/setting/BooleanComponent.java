package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.hack.hacks.client.ClickGUI;
import me.gavin.notorious.hack.hacks.client.Font;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class BooleanComponent extends SettingComponent {

    private final BooleanSetting setting;

    public BooleanComponent(BooleanSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
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
        Gui.drawRect(x, y, x + width, y + height, setting.isEnabled() ? color : 0xCF000000);
        Gui.drawRect(x, y, x + 2, y + height, color);
        if(((Font)Notorious.INSTANCE.hackManager.getHack(Font.class)).isEnabled()) {
            Notorious.INSTANCE.fontRenderer.drawStringWithShadow(setting.getName(), x + 9f, y + 5f, Color.WHITE);
        }else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName(), x + 9f, y + 5f, new Color(255, 255, 255).getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseInside(mouseX, mouseY) && mouseButton == 0)
            setting.toggle();
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void keyTyped(char keyChar, int keyCode) { }
}