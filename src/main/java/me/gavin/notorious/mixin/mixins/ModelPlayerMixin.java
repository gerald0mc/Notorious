package me.gavin.notorious.mixin.mixins;

import me.gavin.notorious.event.events.PlayerModelRotationEvent;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPlayer.class)
public class ModelPlayerMixin {

    //@Inject(method = "render", at = @At("HEAD"))
    //public void renderInject(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {

    //}

    @Inject(method = "setRotationAngles", at = @At("HEAD"))
    public void setRotationAnglesInject(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        PlayerModelRotationEvent event = new PlayerModelRotationEvent(netHeadYaw, headPitch);
        MinecraftForge.EVENT_BUS.post(event);
        headPitch = event.getPitch();
        netHeadYaw = event.getYaw();
    }
}
