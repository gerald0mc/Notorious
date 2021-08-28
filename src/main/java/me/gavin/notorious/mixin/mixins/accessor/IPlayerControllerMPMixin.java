package me.gavin.notorious.mixin.mixins.accessor;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerControllerMP.class)
public interface IPlayerControllerMPMixin {

	@Accessor("curBlockDamageMP")
	float getCurBlockDamageMP();

	@Accessor("curBlockDamageMP")
	void setCurBlockDamageMP(float curBlockDamageMPIn);

}
