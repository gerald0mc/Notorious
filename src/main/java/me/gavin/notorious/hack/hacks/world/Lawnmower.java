package me.gavin.notorious.hack.hacks.world;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.misc.BlockUtil;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * @author Gav06
 * @since 6/15/2021
 */

@RegisterHack(name = "Lawnmower", description = "Mines tall grass and stuff around you", category = Hack.Category.World, bind = Keyboard.KEY_R)
public class Lawnmower extends Hack {

    @RegisterSetting
    public final NumSetting delay = new NumSetting("Delay", 2, 1, 10, 1);

    @SubscribeEvent
    public void onLivingUpdate(PlayerLivingUpdateEvent event) {
        final ArrayList<BlockPos> posList = BlockUtil.getSurroundingBlocks(4, true);
        for (BlockPos pos : posList) {
            if (isMineable(mc.world.getBlockState(pos).getBlock()) && mc.player.ticksExisted % delay.getValue() == 0.0) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.playerController.clickBlock(pos, EnumFacing.UP);
            }
        }
    }

    private boolean isMineable(Block block) {
        return block == Blocks.TALLGRASS || block == Blocks.DOUBLE_PLANT || block == Blocks.RED_FLOWER || block == Blocks.YELLOW_FLOWER;
    }
}
