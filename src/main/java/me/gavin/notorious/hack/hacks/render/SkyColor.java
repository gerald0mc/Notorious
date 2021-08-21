package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.hack.hacks.client.ClickGUI;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

@RegisterHack(name = "SkyColor", description = "ez", category = Hack.Category.Render)
public class SkyColor extends Hack {

    @RegisterSetting
    public final ModeSetting colorMode = new ModeSetting("ColorMode", "ClientSync", "ClientSync", "RGB");
    @RegisterSetting
    public final ColorSetting rgba = new ColorSetting("SkyColor", 125, 0, 255, 255);
    @RegisterSetting
    public final BooleanSetting fog = new BooleanSetting("Fog", true);

    @SubscribeEvent
    public void fogColors(final EntityViewRenderEvent.FogColors event) {
        if(colorMode.getMode().equals("RGB")) {
            event.setRed(rgba.getAsColor().getRed() / 255f);
            event.setGreen(rgba.getAsColor().getGreen() / 255f);
            event.setBlue(rgba.getAsColor().getBlue() / 255f);
        }else {
            event.setRed(Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor().getRed() / 255f);
            event.setGreen(Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor().getGreen() / 255f);
            event.setBlue(Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor().getBlue() / 255f);
        }
    }

    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        if(fog.getValue()) {
            event.setDensity(0.0f);
            event.setCanceled(true);
        }
    }
}
