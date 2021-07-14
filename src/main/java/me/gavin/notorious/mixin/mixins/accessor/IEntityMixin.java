package me.gavin.notorious.mixin.mixins.accessor;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface IEntityMixin {

    @Accessor("inPortal")
    boolean inPortalAccessor();
}
