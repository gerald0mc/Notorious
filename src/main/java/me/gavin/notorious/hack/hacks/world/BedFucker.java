package me.gavin.notorious.hack.hacks.world;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.event.events.PlayerModelRotationEvent;
import me.gavin.notorious.event.events.PlayerWalkingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.mixin.mixins.accessor.IEntityPlayerSPMixin;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.util.BlockUtil;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.MathUtil;
import me.gavin.notorious.util.NColor;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

/**
 * @author Gav06
 * @since 6/15/2021
 */

@RegisterHack(name = "BedFucker", description = "Fucks beds", category = Hack.Category.World)
public class BedFucker extends Hack {

    @RegisterSetting
    public final NumSetting range = new NumSetting("Range", 5f, 0f, 6f, 0.5f);
    @RegisterSetting
    public final ModeSetting renderMode = new ModeSetting("RenderMode", "Both", "Both", "Box", "Outline");
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new NColor(255, 255, 255, 125));
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new NColor(255, 255, 255, 255));

    private BlockPos targetedBlock = null;

    @SubscribeEvent
    public void onLivingUpdate(PlayerWalkingUpdateEvent.Pre event) {
        if (targetedBlock == null) {
            for (BlockPos pos : BlockUtil.getSurroundingBlocks(MathHelper.ceil(range.getValue()), true)) {
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

            if (targetedBlock.getDistance(mc.player.getPosition().getX(), mc.player.getPosition().getY(), mc.player.getPosition().getZ()) > range.getValue()) {
                targetedBlock = null;
                return;
            }

            BlockUtil.damageBlock(targetedBlock, false, true);
        }
    }

//    @SubscribeEvent
//    public void onPlayerModelRotate(PlayerModelRotationEvent event) {
//        if (targetedBlock != null) {
//
//            //event.setYaw(rotation[0] - MathHelper.wrapDegrees(mc.player.rotationYaw));
//            event.setPitch(rotation[1]);
//        }
//    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (targetedBlock != null) {
            final AxisAlignedBB bb = new AxisAlignedBB(targetedBlock);
            RenderUtil.renderFilledBB(bb, boxColor.getAsColor());
            RenderUtil.renderOutlineBB(bb, outlineColor.getAsColor());
        }
    }
}