package me.gavin.notorious.hack.hacks.client;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;

@RegisterHack(name = "ClickGUI", description = "Opens the click gui", category = Hack.Category.Client)
public class ClickGUI extends Hack {

    @RegisterSetting
    public final ModeSetting colorMode = new ModeSetting("ColorMode", "Rainbow", "Rainbow", "RGB");
    @RegisterSetting
    public final ColorSetting guiColor = new ColorSetting("RGBColor", 255, 0, 0, 255);
    @RegisterSetting
    public final NumSetting length = new NumSetting("Length", 8f, 1f, 15f, 1f);
    @RegisterSetting
    public final NumSetting saturation = new NumSetting("Saturation", 0.6f, 0.1f, 1f, 0.1f);

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(notorious.clickGuiScreen);
        disable();
    }
}