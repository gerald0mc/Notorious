package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

@RegisterHack(name = "SkyColor", description = "ez", category = Hack.Category.Render)
public class SkyColor extends Hack {

    @RegisterSetting
    public final ColorSetting rgba = new ColorSetting("SkyColor", 125, 0, 255, 255);
    @RegisterSetting
    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", true);
    @RegisterSetting
    public final BooleanSetting fog = new BooleanSetting("Fog", true);

    @SubscribeEvent
    public void fogColors(final EntityViewRenderEvent.FogColors event) {
        event.setRed(rgba.getRed().getValue() / 255f);
        event.setGreen(rgba.getGreen().getValue() / 255f);
        event.setBlue(rgba.getBlue().getValue() / 255f);
    }

    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        if(fog.getValue()) {
            event.setDensity(0.0f);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent event) {
        if(rainbow.getValue()) {
            doRainbow();
        }
    }

    public void doRainbow() {
        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };
        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        rgba.getRed().setValue((color_rgb_o >> 16) & 0xFF);
        rgba.getGreen().setValue((color_rgb_o >> 8) & 0xFF);
        rgba.getBlue().setValue(color_rgb_o & 0xFF);
    }
}
