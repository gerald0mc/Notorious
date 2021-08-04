package me.gavin.notorious.hack.hacks.client;

import me.gavin.notorious.NotoriousMod;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.ColorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@RegisterHack(name = "SkeetWatermark", description = "ez", category = Hack.Category.Client)
public class SkeetWatermark extends Hack {

    @RegisterSetting
    public final NumSetting x = new NumSetting("X", 2.0f, 0.1f, 1000.0f, 0.1f);
    @RegisterSetting
    public final NumSetting y = new NumSetting("Y", 2.0f, 0.1f, 600.0f, 0.1f);
    @RegisterSetting
    public final BooleanSetting version = new BooleanSetting("Version", false);
    @RegisterSetting
    public final BooleanSetting name = new BooleanSetting("Name", true);

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        String string;
        if(version.isEnabled()) {
            string = NotoriousMod.NAME_VERSION;
        }else {
            string = NotoriousMod.MOD_NAME;
        }
        if(name.isEnabled()) {
            string += " \u23d0 " + mc.player.getDisplayNameString();
        }
        //background
        Gui.drawRect((int) x.getValue(), (int) y.getValue(), (int) x.getValue() + mc.fontRenderer.getStringWidth(string) + 4, (int) y.getValue() + mc.fontRenderer.FONT_HEIGHT + 3, new Color(0, 0, 0, 255).getRGB());
        //rainbow line
        Gui.drawRect((int) x.getValue(), (int) y.getValue(), (int) x.getValue() + mc.fontRenderer.getStringWidth(string) + 4, (int) y.getValue() + 1, ColorUtil.getRainbow(6f, 1f));
        //string
        mc.fontRenderer.drawStringWithShadow(string, x.getValue() + 2, y.getValue() + 2, -1);
    }
}
