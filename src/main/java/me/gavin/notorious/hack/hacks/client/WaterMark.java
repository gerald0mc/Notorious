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
    public final ColorSetting rgb = new ColorSetting("RGB", 255, 255, 255);

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        double x = 0;
        int yOffset = 2;
        double y = yOffset;
        final String watermark = NotoriousMod.NAME_VERSION;
        Font font = ((Font)Notorious.INSTANCE.hackManager.getHack(Font.class));
        Color colorRainbow;
        int intRainbow;
        float time = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).length.getValue();
        float saturation = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).saturation.getValue();
        if(mode.getMode().equals("Rainbow")) {
            colorRainbow = ColorUtil.colorRainbow((int) time, saturation, 1f);
        }else {
            colorRainbow = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).guiColor.getAsColor();
        }
        if(mode.getMode().equals("Rainbow")) {
            intRainbow = ColorUtil.getRainbow(time, saturation);
        }else {
            intRainbow = ((ClickGUI)Notorious.INSTANCE.hackManager.getHack(ClickGUI.class)).guiColor.getAsColor().getRGB();
        }
        if(font.isEnabled()) {
            Gui.drawRect((int) x - 3, (int) y - 2, (int) (x + 1), (int) (y + notorious.fontRenderer.getHeight() + 2), 0x90000000);
            Gui.drawRect((int) x - 4, (int) y - 2, (int) x + 1, (int) (y + notorious.fontRenderer.getHeight() + 2), intRainbow);
        }else {
            Gui.drawRect((int) x - 3, (int) y - 2, (int) (x + 1), (int) (y + mc.fontRenderer.FONT_HEIGHT + 2), 0x90000000);
            Gui.drawRect((int) x - 4, (int) y - 2, (int) x + 1, (int) (y + mc.fontRenderer.FONT_HEIGHT + 2), intRainbow);
        }
        if(font.isEnabled()) {
            notorious.fontRenderer.drawStringWithShadow(watermark, x + 2, y, colorRainbow);
        }else {
            mc.fontRenderer.drawStringWithShadow(watermark, (int) x + 2, (int) y, intRainbow);
        }
    }
}
