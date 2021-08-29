package me.gavin.notorious.hack.hacks.client;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;

@RegisterHack(name = "Notifications", description = "ez", category = Hack.Category.Client)
public class Notification extends Hack {

    @RegisterSetting
    public final ModeSetting style = new ModeSetting("Style", "Normal", "Normal", "Basic");
}
