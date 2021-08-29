package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.TickTimer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@RegisterHack(name = "AutoChad", description = "ez", category = Hack.Category.Chat)
public class AutoChad extends Hack {

    @RegisterSetting
    public final NumSetting delay = new NumSetting("Delay", 10, 1, 15, 1);

    public TickTimer timer = new TickTimer();

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + delay.getValue() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onUpdate(TickEvent event) {
        if (mc.player != null && mc.world != null) {
            if (timer.hasTicksPassed((long) (delay.getValue() * 20))) {
                mc.player.sendChatMessage("youtube.com/channel/UCRpxbpHD4IMS9fZmAirFaDg");
                timer.reset();
            }
        }
    }
}
