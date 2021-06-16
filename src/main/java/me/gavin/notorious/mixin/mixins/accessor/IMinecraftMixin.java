package me.gavin.notorious.mixin.mixins.accessor;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface IMinecraftMixin {

    @Accessor("rightClickDelayTimer")
    void setRightClickDelayTimerAccessor(int timer);
}
