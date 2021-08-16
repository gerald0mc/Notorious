package me.gavin.notorious.hack.hacks.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.friend.Friend;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.ColorUtil;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@RegisterHack(name = "FriendList", description = "ez", category = Hack.Category.Client)
public class FriendList extends Hack {

    @RegisterSetting
    public final NumSetting x = new NumSetting("X", 2.0f, 0.1f, 1000.0f, 0.1f);
    @RegisterSetting
    public final NumSetting y = new NumSetting("Y", 2.0f, 0.1f, 600.0f, 0.1f);
    @RegisterSetting
    public final ModeSetting colorMode = new ModeSetting("ColorMode", "ColorSync", "ColorSync", "RGB", "Rainbow");
    @RegisterSetting
    public final ColorSetting rgb = new ColorSetting("Color", 255, 255, 255, 255);

    int yOffset;

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        yOffset = 2;
        String homies = ChatFormatting.BOLD + "Homies:";
        int color;
        if(colorMode.getMode().equals("ColorSync")) {
            color = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor().getRGB();
        }else if(colorMode.getMode().equals("RGB")) {
            color = rgb.getAsColor().getRGB();
        }else {
            color = ColorUtil.getRainbow(6f, 1f);
        }
        mc.fontRenderer.drawStringWithShadow(homies, x.getValue(), y.getValue(), color);
        if(!notorious.friend.getFriends().isEmpty()) {
            for (Friend f : notorious.friend.getFriends()) {
                mc.fontRenderer.drawStringWithShadow(f.getName(), x.getValue(), y.getValue() + yOffset + 6, -1);
                yOffset += mc.fontRenderer.FONT_HEIGHT + 0.5;
            }
        }
    }
}
