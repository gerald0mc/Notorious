package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@RegisterHack(name = "Weather", description = "ez", category = Hack.Category.Render)
public class Weather extends Hack {

    @RegisterSetting
    public final NumSetting rainStrength = new NumSetting("RainStrength", 0f, 0f, 3f, 1f);
}

