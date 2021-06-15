package me.gavin.notorious.misc;

import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;

import java.util.ArrayList;

public class BlockUtil implements IMinecraft {

    public static ArrayList<BlockPos> getSurroundingBlocks(int radius, boolean motion) {
        final ArrayList<BlockPos> posList = new ArrayList<>();
        BlockPos playerPos = mc.player.getPosition().add(0, 1, 0);
        if (motion)
            playerPos.add(mc.player.motionX, mc.player.motionY, mc.player.motionZ);

        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    posList.add(new BlockPos(x, y, z).add(playerPos));
                }
            }
        }

        return posList;
    }

    public static void damageBlock(BlockPos position, EnumFacing facing, boolean packet) {
        if (packet) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, position, facing));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, position, facing));
        } else {
            if (mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getGameType() == GameType.CREATIVE) {
                mc.playerController.clickBlock(position, facing);
            } else {
                mc.playerController.onPlayerDamageBlock(position, facing);
            }
        }
    }
}
