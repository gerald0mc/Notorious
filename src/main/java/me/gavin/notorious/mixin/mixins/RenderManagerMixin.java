package me.gavin.notorious.mixin.mixins;

import me.gavin.notorious.event.events.RenderEntityEvent;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderManager.class)
public class RenderManagerMixin {

    @Inject(method = "renderEntity", at = @At("HEAD"))
    public void renderEntity$Inject$HEAD(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEntityEvent.Pre(entityIn, x, y, z));
    }

    @Inject(method = "renderEntity", at = @At("TAIL"))
    public void renderEntity$Inject$TAIL(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderEntityEvent.Post(entityIn, x, y, z));
    }
}