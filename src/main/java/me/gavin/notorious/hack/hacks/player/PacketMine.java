package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.event.events.PlayerDamageBlockEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "PacketMine", description = "ez", category = Hack.Category.Player)
public class PacketMine extends Hack {

    @SubscribeEvent
    public void onEvent(PlayerDamageBlockEvent event) {
        if(canBreak(event.pos)) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
            event.setCanceled(true);
        }
    }

    private boolean canBreak(BlockPos pos) {
        final IBlockState blockState = mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();

        return block.getBlockHardness(blockState, mc.world, pos) != -1;
    }
}
