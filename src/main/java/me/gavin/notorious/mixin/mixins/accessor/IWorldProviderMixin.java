package me.gavin.notorious.mixin.mixins.accessor;

import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldProvider.class)
public interface IWorldProviderMixin {

    @Accessor("lightBrightnessTable")
    void setLightBrightnessTableAccessor(float[] table);

    @Accessor("lightBrightnessTable")
    float[] getLightBrightnessTableAccessor();
}
