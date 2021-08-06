package me.gavin.notorious.hack.hacks.movement;

import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.mixin.mixins.accessor.ISpacketExplosionMixin;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "Velocity", description = "Cancel packets that give velocity", category = Hack.Category.Movement)
public class Velocity extends Hack {

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion explosion = (SPacketExplosion) event.getPacket();
            final ISpacketExplosionMixin accessor = (ISpacketExplosionMixin) explosion;
            accessor.setMotionXAccessor(0f);
            accessor.setMotionYAccessor(0f);
            accessor.setMotionZAccessor(0f);
        } else if (event.getPacket() instanceof SPacketEntityVelocity) {
            if (((SPacketEntityVelocity)event.getPacket()).getEntityID() == mc.player.getEntityId()) {
                event.setCanceled(true);
            }
        }
    }
}
