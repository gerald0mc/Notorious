package me.gavin.notorious.hack.hacks.world;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.misc.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

@RegisterHack(name = "Lawnmower", description = "Mines tall grass and stuff around you", category = Hack.Category.World, bind = Keyboard.KEY_R)
public class Lawnmower extends Hack {

    @SubscribeEvent
    public void onLivingUpdate(PlayerLivingUpdateEvent event) {
        final ArrayList<BlockPos> posList = BlockUtil.getSurroundingBlocks(4, true);
        for (BlockPos pos : posList) {
            if (isMineable(mc.world.getBlockState(pos).getBlock())) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
            }
        }
    }

    private boolean isMineable(Block block) {
        return block == Blocks.TALLGRASS || block == Blocks.DOUBLE_PLANT || block == Blocks.RED_FLOWER || block == Blocks.YELLOW_FLOWER;
    }
}
