package me.gavin.notorious.hack.hacks.client;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;

@RegisterHack(name = "ClickGUI", description = "Opens the click gui", category = Hack.Category.Client)
public class ClickGUI extends Hack {

    @RegisterSetting
    public final BooleanSetting customFont = new BooleanSetting("CustomFont", true);

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(notorious.clickGuiScreen);
        disable();
    }
}