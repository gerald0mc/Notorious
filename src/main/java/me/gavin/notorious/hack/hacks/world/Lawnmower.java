package me.gavin.notorious.hack.hacks.world;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.util.BlockUtil;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.MathUtil;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gav06 and gerald0mc
 * @since 6/15/2021
 */

@RegisterHack(name = "Lawnmower", description = "Mines tall grass and stuff around you", category = Hack.Category.World)
public class Lawnmower extends Hack {

    @RegisterSetting
    public final NumSetting delay = new NumSetting("Delay", 5, 1, 10, 1);

    public ArrayList<BlockPos> posList;

    private BlockPos targetBlock;

    @SubscribeEvent
    public void onLivingUpdate(PlayerLivingUpdateEvent event) {
        posList = BlockUtil.getSurroundingBlocks(4, true);

        for (BlockPos pos : posList) {
            if (isMineable(mc.world.getBlockState(pos).getBlock()) && mc.player.ticksExisted % delay.getValue() == 0.0) {
                BlockUtil.damageBlock(pos, false, true);
            }
        }
    }

    private boolean isMineable(Block block) {
        return block == Blocks.TALLGRASS || block == Blocks.DOUBLE_PLANT || block == Blocks.RED_FLOWER || block == Blocks.YELLOW_FLOWER;
    }
}
