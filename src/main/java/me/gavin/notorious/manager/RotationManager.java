package me.gavin.notorious.manager;

import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.mixin.mixins.accessor.ICPacketPlayerMixin;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RotationManager {

    public RotationManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public float desiredPitch;
    public float desiredYaw;

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (!shouldRotate)
            return;

        if (event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            final ICPacketPlayerMixin accessor = (ICPacketPlayerMixin) packet;
            accessor.setYawAccessor(desiredYaw);
            accessor.setPitchAccessor(desiredPitch);
        }
    }

    public boolean shouldRotate = false;
}
