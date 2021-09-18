package me.gavin.notorious.mixin.mixins;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.hacks.render.PopESP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderPlayer.class})
public class RenderPlayerMixin {

    @Inject(method = "renderEntityName", at = @At("HEAD"), cancellable = true)
    public void renderEntityNameHook(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo info) {
        if(Notorious.INSTANCE.hackManager.getHack(PopESP.class).isEnabled()) {
            info.cancel();
        }
    }
}
