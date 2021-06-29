package me.gavin.notorious.hack.hacks.client;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import org.lwjgl.input.Keyboard;

@RegisterHack(name = "ClickGUI", description = "Opens the click gui", category = Hack.Category.Client)
public class ClickGUI extends Hack {

    public ClickGUI() {
        this.setBind(Keyboard.KEY_RSHIFT);
    }

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(notorious.clickGui);
        disable();
    }
}