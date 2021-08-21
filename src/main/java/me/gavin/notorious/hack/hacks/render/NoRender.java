package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "NoRender", description = "ez", category = Hack.Category.Render)
public class NoRender extends Hack {

    @RegisterSetting
    private final BooleanSetting explosions = new BooleanSetting("Explosions", true);

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if(event.getPacket() instanceof SPacketExplosion && this.explosions.getValue()) {
            event.setCanceled(true);
        }
    }
}
