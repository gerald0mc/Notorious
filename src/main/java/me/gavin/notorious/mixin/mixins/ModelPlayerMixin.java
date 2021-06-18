package me.gavin.notorious.mixin.mixins;

import me.gavin.notorious.event.events.PlayerModelRotationEvent;
import me.gavin.notorious.stuff.IMinecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPlayer.class)
public class ModelPlayerMixin implements IMinecraft {


    @Inject(method = "setRotationAngles", at = @At("INVOKE"))
    public void setRotationAnglesInject(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (entityIn == mc.player) {
            PlayerModelRotationEvent event = new PlayerModelRotationEvent(netHeadYaw, headPitch);
            MinecraftForge.EVENT_BUS.post(event);
            ((ModelPlayer) (Object) this).bipedHead.rotateAngleX = event.getPitch() * .017453292F;
            ((ModelPlayer) (Object) this).bipedHead.rotateAngleY = event.getYaw() * .017453292F;
            ((ModelPlayer) (Object) this).bipedHeadwear.rotateAngleX = event.getYaw() * .017453292F;
            ((ModelPlayer) (Object) this).bipedHeadwear.rotateAngleY = event.getYaw() * .017453292F;
            //ModelBase.copyModelAngles(((ModelPlayer) (Object) this).bipedHead, ((ModelPlayer) (Object) this).bipedHeadwear);
        }
    }
}
