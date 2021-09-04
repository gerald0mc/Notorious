package me.gavin.notorious.hack.hacks.misc;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.hack.hacks.combatrewrite.AutoCrystal;
import me.gavin.notorious.hack.hacks.movement.AutoHop;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "RubberbandDetect", description = "ez", category = Hack.Category.Misc)
public class RubberBandDetect extends Hack {

    @RegisterSetting
    public final BooleanSetting disableOnLag = new BooleanSetting("DisableOnLag", true);
    @RegisterSetting
    public final BooleanSetting autoCrytal = new BooleanSetting("AutoCrystal", true);
    @RegisterSetting
    public final BooleanSetting autoHop = new BooleanSetting("AutoHop", true);

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if(event.getPacket() instanceof SPacketPlayerPosLook) {
            notorious.messageManager.sendError("Rubberband detected!");
            if (disableOnLag.isEnabled() && (autoCrytal.isEnabled() || autoHop.isEnabled())) {
                notorious.messageManager.sendMessage("Toggling modules.");
                if(Notorious.INSTANCE.hackManager.getHack(AutoCrystal.class).isEnabled()) {
                    Notorious.INSTANCE.hackManager.getHack(AutoCrystal.class).toggle();
                }
                if(Notorious.INSTANCE.hackManager.getHack(AutoHop.class).isEnabled()) {
                    Notorious.INSTANCE.hackManager.getHack(AutoHop.class).toggle();
                }
            }
        }
    }
}
