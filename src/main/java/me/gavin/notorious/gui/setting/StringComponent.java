package me.gavin.notorious.gui.setting;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.hack.hacks.client.ClickGUI;
import me.gavin.notorious.hack.hacks.client.Font;
import me.gavin.notorious.setting.StringSetting;
import me.gavin.notorious.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;

public class StringComponent extends SettingComponent {
    private final StringSetting setting;
    private boolean isTyping = false;
    private CurrentString currentString = new CurrentString("");

    public StringComponent(StringSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        Font font = Notorious.INSTANCE.hackManager.getHack(Font.class);
        float time = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).length.getValue();
        float saturation = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).saturation.getValue();
        int color;
        if(Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).colorMode.getMode().equals("Rainbow")) {
            color = ColorUtil.getRainbow(time, saturation);
        }else {
            color = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor().getRGB();
        }
        Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, (int) Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).backgroundAlpha.getValue()).getRGB());
        Gui.drawRect(x, y, x + 2, y + height, color);
        if(isTyping) {
            if(font.isEnabled()) {
                Notorious.INSTANCE.fontRenderer.drawStringWithShadow(setting.getName() + " <" + currentString.getString() + ">", x + 9f, y + 3f, Color.WHITE);
            }else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName() + " <" + currentString.getString() + ">", x + 9f, y + 1f, -1);
            }
        }else {
            if(font.isEnabled()) {
                Notorious.INSTANCE.fontRenderer.drawStringWithShadow(setting.getName() + " <" + setting.getString() + ">", x + 9f, y + 3f, Color.WHITE);
            }else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName() + " <" + setting.getString() + ">", x + 9f, y + 1f, -1);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isMouseInside(mouseX, mouseY)) {
            if(mouseButton == 0) {
                isTyping = !isTyping;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void keyTyped(char keyChar, int keyCode) {
        if(isTyping) {
            if(keyCode == 1)
                return;
            if(keyCode == 28) {
                enterString();
            }else if(keyCode == 14) {
                setString(removeLastChar(currentString.getString()));
            }else if(keyCode == 47 && (Keyboard.isKeyDown(157) || Keyboard.isKeyDown(29))) {
                try {
                    setString(currentString.getString() + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
                }catch (Exception var5) {var5.printStackTrace();}
            }else if(ChatAllowedCharacters.isAllowedCharacter(keyChar)) {
                setString(currentString.getString() + keyChar);
            }
        }
    }

    public static String removeLastChar(String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

    @Override
    public int getTotalHeight() {
        return height;
    }

    private void enterString() {
        if (this.currentString.getString().isEmpty()) {
            this.setting.setString(this.setting.getString());
        } else {
            this.setting.setString(this.currentString.getString());
        }
    }

    public void setString(String newString) {
        this.currentString = new CurrentString(newString);
    }

    public static class CurrentString {
        private String string;

        public CurrentString(String string) {
            this.string = string;
        }

        public String getString() {
            return this.string;
        }
    }
}
