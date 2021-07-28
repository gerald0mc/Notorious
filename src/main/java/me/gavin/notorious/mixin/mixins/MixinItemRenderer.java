package me.gavin.notorious.mixin.mixins;

import me.gavin.notorious.hack.hacks.render.ViewModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemRenderer.class})
public class MixinItemRenderer {

    @Inject(method = "renderItemSide", at = @At("HEAD"))
    public void renderItemSide(EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        if (ViewModel.INSTANCE.isEnabled()) {
            GlStateManager.scale(ViewModel.INSTANCE.scaleX.getValue() / 100F, ViewModel.INSTANCE.scaleY.getValue() / 100F, ViewModel.INSTANCE.scaleZ.getValue() / 100F);
            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
                GlStateManager.translate(ViewModel.INSTANCE.translateX.getValue() / 200F, ViewModel.INSTANCE.translateY.getValue() / 200F, ViewModel.INSTANCE.translateZ.getValue() / 200F);
                GlStateManager.rotate(ViewModel.INSTANCE.rotateX.getValue(), 1, 0, 0);
                GlStateManager.rotate(ViewModel.INSTANCE.rotateY.getValue(), 0, 1, 0);
                GlStateManager.rotate(ViewModel.INSTANCE.rotateZ.getValue(), 0, 0, 1);
            } else if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
                GlStateManager.translate(-ViewModel.INSTANCE.translateX.getValue() / 200F, ViewModel.INSTANCE.translateY.getValue() / 200F, ViewModel.INSTANCE.translateZ.getValue() / 200F);
                GlStateManager.rotate(-ViewModel.INSTANCE.rotateX.getValue(), 1, 0, 0);
                GlStateManager.rotate(ViewModel.INSTANCE.rotateY.getValue(), 0, 1, 0);
                GlStateManager.rotate(ViewModel.INSTANCE.rotateZ.getValue(), 0, 0, 1);
            }
        }
    }
}
