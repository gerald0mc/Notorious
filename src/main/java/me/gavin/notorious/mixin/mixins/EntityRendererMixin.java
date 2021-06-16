package me.gavin.notorious.mixin.mixins;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.hacks.render.AntiFog;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Gav06
 * @since 6/15/2021
 */

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Inject(method = "setupFog", at = @At("RETURN"))
    public void setupFogInject(int startCoords, float partialTicks, CallbackInfo ci) {
        if (Notorious.INSTANCE.hackManager.getHack(AntiFog.class).isEnabled())
            GlStateManager.disableFog();
    }
}
