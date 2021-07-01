package me.gavin.notorious.hack.hacks.client;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.NotoriousMod;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.ColorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@RegisterHack(name = "WaterMark", description = "ez", category = Hack.Category.Client)
public class WaterMark extends Hack {

    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Rainbow", "Rainbow", "RGB");
    @RegisterSetting
    public final NumSetting length = new NumSetting("Length", 8f, 1f, 15f, 1f);
    @RegisterSetting
    public final NumSetting saturation = new NumSetting("Saturation", 0.6f, 0.1f, 1f, 0.1f);
    @RegisterSetting
    public final ColorSetting rgb = new ColorSetting("RGB", 255, 255, 255);

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        double x = 0;
        int yOffset = 2;
        double y = yOffset;
        final String watermark = NotoriousMod.NAME_VERSION;
        Color colorRainbow;
        int intRainbow;
        float time = length.getValue();
        float bruh = saturation.getValue();
        if(mode.getMode().equals("Rainbow")) {
            colorRainbow = ColorUtil.colorRainbow(time, bruh);
        }else {
            colorRainbow = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).guiColor.getAsColor();
        }
        if(mode.getMode().equals("Rainbow")) {
            intRainbow = ColorUtil.getRainbow(time, bruh);
        }else {
            intRainbow = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).guiColor.getAsColor().getRGB();
        }
        Gui.drawRect((int) x - 3, (int) y - 2, (int) (x + 1), (int) (y + notorious.fontRenderer.getHeight() + 2), 0x90000000);
        Gui.drawRect((int) x - 4, (int) y - 2, (int) x + 1, (int) (y + notorious.fontRenderer.getHeight() + 2), intRainbow);
        if(((Font)Notorious.INSTANCE.hackManager.getHack(Font.class)).isEnabled()) {
            notorious.fontRenderer.drawStringWithShadow(watermark, x + 2, y, colorRainbow);
        }else {
            mc.fontRenderer.drawStringWithShadow(watermark, (int) x + 2, (int) y, intRainbow);
        }
    }
}
