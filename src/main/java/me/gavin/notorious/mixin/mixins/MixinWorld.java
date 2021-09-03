package me.gavin.notorious.mixin.mixins;

import me.gavin.notorious.event.events.EntityRemoveEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class MixinWorld {
    @Inject(method = "onEntityRemoved", at = @At("HEAD"), cancellable = true)
    public void onEntityRemoved(Entity entity, CallbackInfo info) {
        EntityRemoveEvent event = new EntityRemoveEvent(entity);
        MinecraftForge.EVENT_BUS.post(event);
    }
}
