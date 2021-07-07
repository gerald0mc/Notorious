package me.gavin.notorious.hack.hacks.misc;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "WeaknessLog", description = "ez", category = Hack.Category.Misc)
public class WeaknessLog extends Hack {

    @RegisterSetting
    public final BooleanSetting announce = new BooleanSetting("Announce", true);

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if(mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            if(announce.isEnabled()) {
                mc.player.sendChatMessage("Stop weaknessing retard");
            }
            mc.getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString("Ew you got weaknessed")));
            toggle();
        }
    }
}
