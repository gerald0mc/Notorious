package me.gavin.notorious.hack.hacks.other;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import org.lwjgl.input.Keyboard;

@RegisterHack(name = "ClickGUI", description = "Opens the click gui", category = Hack.Category.Other, bind = Keyboard.KEY_I)
public class ClickGUI extends Hack {

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(notorious.clickGui);
        disable();
    }
}
