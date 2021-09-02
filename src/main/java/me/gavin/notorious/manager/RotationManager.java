package me.gavin.notorious.manager;

import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.mixin.mixins.accessor.ICPacketPlayerMixin;
import me.gavin.notorious.stuff.IMinecraft;
import me.gavin.notorious.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RotationManager implements IMinecraft {

    public RotationManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean shouldRotate = false;
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

    public void rotateToEntity(Entity entity) {
        final float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
        shouldRotate = true;
        desiredYaw = angle[0];
        desiredPitch = angle[1];
    }

    public void rotateToPosition(BlockPos position) {
        final float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d(position.getX() + 0.5f, position.getY() - 0.5f, position.getZ()));
        shouldRotate = true;
        desiredYaw = angle[0];
        desiredPitch = angle[1];
    }
}
