package me.gavin.notorious.mixin.mixins.accessor;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketPlayer.class)
public interface ICPacketPlayerMixin {

    @Accessor("yaw")
    void setYawAccessor(float yaw);

    @Accessor("pitch")
    void setPitchAccessor(float pitch);

}
