package dev.notorious.client.hacks.client;

import dev.notorious.client.Notorious;
import dev.notorious.client.event.impl.TextRenderEvent;
import dev.notorious.client.hacks.Hack;
import dev.notorious.client.hacks.RegisterHack;
import dev.notorious.client.value.impl.*;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Locale;

@RegisterHack(name = "Test", displayName = "Test Hack", description = "Description!", category = Hack.Category.CLIENT, bind = Keyboard.KEY_RSHIFT)
public class Test extends Hack {
    ValueBoolean valueBoolean = new ValueBoolean("Boolean", "Boolean", "Just a test Boolean Setting.", true);
    ValueInteger valueInteger = new ValueInteger("Integer", "Integer", "Just a test Integer Setting.", 2, 1, 3);
    ValueDouble valueDouble = new ValueDouble("Integer", "Integer", "Just a test Integer Setting.", 2.5, 0.5, 3.5);
    ValueFloat valueFloat = new ValueFloat("Integer", "Integer", "Just a test Integer Setting.", 3.6f, 0.3f, 4.7f);
    ValueMode valueMode = new ValueMode("Mode", "Mode", "Just a test Mode setting.", new String[]{"One", "Two", "Three"}, "One");
    ValueString valueString = new ValueString("String", "String", "Just a test String setting.", "Hello, Minecraft!");
    ValueColor valueColor = new ValueColor("Color", "Color", "Just a test Color setting.", new Color(255, 0, 125, 255));
    ValueBind valueBind = new ValueBind("TestBind", "Bind", "Just a test Bind setting.", Keyboard.KEY_NONE);

    public void onRender(TextRenderEvent event){
        mc.fontRenderer.drawStringWithShadow("Notorious - " + Notorious.VERSION, 2, 2, -1);
        mc.fontRenderer.drawStringWithShadow("Settings - [" + getValues().size() + "]", 2, 12, -1);
        mc.fontRenderer.drawStringWithShadow("Boolean - " + valueBoolean.getValue(), 2, 22, -1);
        mc.fontRenderer.drawStringWithShadow("Integer - " + valueInteger.getValue(), 2, 32, -1);
        mc.fontRenderer.drawStringWithShadow("Double - " + valueDouble.getValue(), 2, 42, -1);
        mc.fontRenderer.drawStringWithShadow("Float - " + valueFloat.getValue(), 2, 52, -1);
        mc.fontRenderer.drawStringWithShadow("Mode - " + valueMode.getValue(), 2, 62, -1);
        mc.fontRenderer.drawStringWithShadow("String - " + valueString.getValue(), 2, 72, -1);
        mc.fontRenderer.drawStringWithShadow("Color - RGB: [" + valueColor.getRed() + ", " + valueColor.getGreen() + ", " + valueColor.getBlue() + "], HSB: [" + (int) (valueColor.getHue() * 360) + ", " + (int) (valueColor.getSaturation() * 100) + ", " + (int) (valueColor.getBrightness() * 100) + "], ALPHA: " + valueColor.getAlpha(), 2, 82, -1);
        mc.fontRenderer.drawStringWithShadow("Bind - " + Keyboard.getKeyName(valueBind.getValue()).toUpperCase(), 2, 92, -1);
    }

    public void onEnable(){
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("This works! (ENABLED)"));
    }

    public void onDisable(){
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("This works! (DISABLED)"));
    }
}