package me.gavin.notorious.mixin.mixins.accessor;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayerSP.class)
public interface IEntityPlayerSPMixin {

    @Accessor("positionUpdateTicks")
    void positionUpdateTicksAccessor(int ticks);

    @Accessor("positionUpdateTicks")
    int getPositionUpdateTicksAccessor();
}
