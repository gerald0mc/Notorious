package me.gavin.notorious.hack.hacks.client;

import me.gavin.notorious.fakegui.ClickGUIScreen;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import org.lwjgl.input.Keyboard;

@RegisterHack(name = "MaxClickGUI", description = "TEST GUI", category = Hack.Category.Client, bind = Keyboard.KEY_RSHIFT)
public class MaxClickGUI extends Hack {

    @Override
    public void onEnable() {
        mc.displayGuiScreen(ClickGUIScreen.INSTANCE);
        toggle();
    }
}
