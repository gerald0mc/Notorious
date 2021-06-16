package me.gavin.notorious.hack.hacks.world;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterValue;
import me.gavin.notorious.misc.BlockUtil;
import me.gavin.notorious.setting.Value;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

/**
 * @author Gav06
 * @since 6/15/2021
 */

@RegisterHack(name = "BedFucker", description = "Fucks beds", category = Hack.Category.World, bind = Keyboard.KEY_J)
public class BedFucker extends Hack {

    @RegisterValue
    public final Value<BreakMode> breakModeValue = new Value<>("Break Mode", BreakMode.NORMAL);

    private BlockPos targetedBlock = null;

    @SubscribeEvent
    public void onLivingUpdate(PlayerLivingUpdateEvent event) {
        if (targetedBlock == null) {
            for (BlockPos pos : BlockUtil.getSurroundingBlocks(5, true)) {
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

            if (targetedBlock.getDistance(mc.player.getPosition().getX(), mc.player.getPosition().getY(), mc.player.getPosition().getZ()) > 5) {
                targetedBlock = null;
                return;
            }

            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.playerController.onPlayerDamageBlock(targetedBlock, EnumFacing.UP);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (targetedBlock != null) {
            RenderUtil.prepare();
            final AxisAlignedBB bb = new AxisAlignedBB(targetedBlock).offset(
                    -mc.getRenderManager().viewerPosX,
                    -mc.getRenderManager().viewerPosY,
                    -mc.getRenderManager().viewerPosZ);
            RenderGlobal.renderFilledBox(bb, 1f, 1f, 1f, 0.5f);
            RenderUtil.release();
        }
    }

    public enum BreakMode {
        PACKET,
        NORMAL
    }
}