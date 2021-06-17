package me.gavin.notorious.mixin.mixins.accessor;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayerSP.class)
public interface IEntityPlayerSPMixin {

    @Accessor("lastReportedPitch")
    void setLastReportedPitchAccessor(float pitch);

    @Accessor("lastReportedYaw")
    void setLastReportedYawAccessor(float yaw);
}
