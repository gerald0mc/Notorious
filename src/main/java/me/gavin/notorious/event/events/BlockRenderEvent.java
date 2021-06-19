package me.gavin.notorious.event.events;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BlockRenderEvent extends Event {

    private final BlockPos blockPos;

    public BlockRenderEvent(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
