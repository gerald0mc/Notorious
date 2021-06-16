package me.gavin.notorious.hack.hacks.other;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;

@RegisterHack(name = "ClickGUI", description = "Opens the click gui", category = Hack.Category.Other)
public class ClickGUI extends Hack {

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(notorious.clickGui);
        disable();
    }
}
