package me.gavin.notorious.hack.hacks.world;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterValue;
import me.gavin.notorious.misc.BlockUtil;
import me.gavin.notorious.setting.Value;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@RegisterHack(name = "BedFucker", description = "Fucks beds", category = Hack.Category.World, bind = Keyboard.KEY_J)
public class BedFucker extends Hack {

    @RegisterValue
    public final Value<BreakMode> breakModeValue = new Value<BreakMode>("Break Mode", BreakMode.NORMAL);

    private BlockPos targetedBlock = null;

    @SubscribeEvent
    public void onLivingUpdate(PlayerLivingUpdateEvent event) {
        if (targetedBlock == null) {
            for (BlockPos pos : BlockUtil.getSurroundingBlocks(4, true)) {
                final Block block = mc.world.getBlockState(pos).getBlock();

                if (block == Blocks.BED) {
                    targetedBlock = pos;
                    break;
                }
            }
        } else {
            if (mc.world.getBlockState(targetedBlock).getBlock() == Blocks.AIR) {
                targetedBlock = null;
                return;
            }

            if (targetedBlock.getDistance((int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ) > 4) {
                targetedBlock = null;
                return;
            }


            //mc.player.swingArm(EnumHand.MAIN_HAND);
            BlockUtil.damageBlock(targetedBlock, EnumFacing.UP, breakModeValue.getValue() == BreakMode.PACKET);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (targetedBlock != null) {
            GlStateManager.pushMatrix();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.disableLighting();
            GlStateManager.disableTexture2D();
            final AxisAlignedBB bb = mc.world.getBlockState(targetedBlock).getSelectedBoundingBox(mc.world, targetedBlock).offset(
                    -mc.getRenderManager().viewerPosX,
                    -mc.getRenderManager().viewerPosY,
                    -mc.getRenderManager().viewerPosZ);
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.blendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_SRC_ALPHA);
            RenderGlobal.renderFilledBox(bb, 1f, 1f, 1f, 0.5f);
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
    }

    public enum BreakMode {
        PACKET,
        NORMAL
    }
}