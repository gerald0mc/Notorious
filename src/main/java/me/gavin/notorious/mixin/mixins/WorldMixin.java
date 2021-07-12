package me.gavin.notorious.mixin.mixins;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.hacks.render.Weather;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {

    @Inject(method = "getRainStrength", at = @At("HEAD"), cancellable = true)
    public void getRainStrengthInject(float delta, CallbackInfoReturnable<Float> cir) {
        Weather weather = (Weather) Notorious.INSTANCE.hackManager.getHack(Weather.class);
        if (weather.isEnabled())
            cir.setReturnValue(weather.rainStrength.getValue());
    }
}