package me.gavin.notorious.mixin.mixins;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Gav06
 * @since 6/15/2021
 */

@Mixin(EntityPlayerSP.class)
public class EntityPlayerSPMixin {

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    public void onLivingUpdateInject(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PlayerLivingUpdateEvent());
    }
}