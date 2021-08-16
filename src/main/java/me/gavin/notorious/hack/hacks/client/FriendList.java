package me.gavin.notorious.hack.hacks.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.friend.Friend;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@RegisterHack(name = "FriendList", description = "ez", category = Hack.Category.Client)
public class FriendList extends Hack {

    @RegisterSetting
    public final NumSetting x = new NumSetting("X", 2.0f, 0.1f, 1000.0f, 0.1f);
    @RegisterSetting
    public final NumSetting y = new NumSetting("Y", 2.0f, 0.1f, 600.0f, 0.1f);

    int yOffset;

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        yOffset = 2;
        String homies = ChatFormatting.BOLD + "Homies:";
        mc.fontRenderer.drawStringWithShadow(homies, x.getValue(), y.getValue(), new Color(0, 255, 0).getRGB());
        if(!notorious.friend.getFriends().isEmpty()) {
            for (Friend f : notorious.friend.getFriends()) {
                mc.fontRenderer.drawStringWithShadow(f.getName(), x.getValue(), y.getValue() + yOffset + 6, -1);
                yOffset += mc.fontRenderer.FONT_HEIGHT + 0.5;
            }
        }
    }
}
