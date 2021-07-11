package me.gavin.notorious.hack.hacks.misc;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

@RegisterHack(name = "AutoLog", description = "ez", category = Hack.Category.Misc)
public class AutoLog extends Hack {

    @RegisterSetting
    public final NumSetting health = new NumSetting("HealthToLog", 16f, 1f, 36f, 1f);

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if(mc.player.getHealth() <= health.getValue()) {
            Objects.requireNonNull(mc.getConnection()).handleDisconnect(new SPacketDisconnect(new TextComponentString("Logged at " + "[" + mc.player.getHealth() + "]")));
            toggle();
        }
    }
}
