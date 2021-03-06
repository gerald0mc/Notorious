package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

@RegisterHack(name = "Glint", description = "ez", category = Hack.Category.Render)
public class Glint extends Hack {

    @RegisterSetting
    public final ColorSetting colorShit = new ColorSetting("Color", 255, 255, 255, 255);
    @RegisterSetting
    public static NumSetting saturation = new NumSetting("Saturation", 1.0F, 0.1F, 1.0F, 0.1F);
    @RegisterSetting
    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", true);

    public static Color getColor(long offset, float fade) {
        float hue = (float) (System.nanoTime() + offset) / 1.0E10F % 1.0F;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, saturation.getValue(), 1.0F)), 16);
        Color c = new Color((int) color);
        return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade, c.getAlpha() / 255.0F);
    }
}
