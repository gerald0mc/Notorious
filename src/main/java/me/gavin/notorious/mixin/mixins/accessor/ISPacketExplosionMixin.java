package me.gavin.notorious.mixin.mixins.accessor;

import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketExplosion.class)
public interface ISPacketExplosionMixin {

    @Accessor("motionX")
    void setMotionXAccessor(float value);

    @Accessor("motionY")
    void setMotionYAccessor(float value);

    @Accessor("motionZ")
    void setMotionZAccessor(float value);
}
