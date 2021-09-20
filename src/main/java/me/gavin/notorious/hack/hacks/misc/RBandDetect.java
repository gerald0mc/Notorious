package me.gavin.notorious.hack.hacks.misc;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.hack.hacks.combat.AutoCrystal;
import me.gavin.notorious.hack.hacks.movement.AutoHop;
import me.gavin.notorious.hack.hacks.movement.Step;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "RBandDetect", description = "ez", category = Hack.Category.Misc)
public class RBandDetect extends Hack {

    @RegisterSetting public final BooleanSetting disableOnLag = new BooleanSetting("DisableOnLag", true);
    @RegisterSetting public final BooleanSetting step = new BooleanSetting("Step", true);

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if(event.getPacket() instanceof SPacketPlayerPosLook) {
            notorious.messageManager.sendError("Rubberband detected!");
            if (disableOnLag.isEnabled() && (step.isEnabled())) {
                notorious.messageManager.sendMessage("Toggling modules.");
                if(Notorious.INSTANCE.hackManager.getHack(Step.class).isEnabled()) {
                    Notorious.INSTANCE.hackManager.getHack(Step.class).toggle();
                }
            }
        }
    }
}
