package me.gavin.notorious.gui.setting;

import me.gavin.notorious.gui.api.SettingComponent;
import me.gavin.notorious.setting.StringSetting;
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
        Gui.drawRect(x, y, x + width, y + height, 0xCF000000);
        if(isTyping) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName() + " <" + currentString.getString() + ">", x + 9f, y + 5f, -1);
        }else {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName() + " <" + setting.getString() + ">", x + 9f, y + 5f, -1);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) { }

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
            }else if(keyCode == 47 && (Keyboard.isKeyDown((int)157) || Keyboard.isKeyDown((int)29))) {
                try {
                    setString(currentString.getString() + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
                }catch (Exception var5) {var5.printStackTrace();}
            }else if(ChatAllowedCharacters.isAllowedCharacter((char)keyChar)) {
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
        this.setString("");
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
