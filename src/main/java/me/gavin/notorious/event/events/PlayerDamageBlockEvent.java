package me.gavin.notorious.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerDamageBlockEvent extends Event {
    public BlockPos pos;

    public EnumFacing facing;

    public PlayerDamageBlockEvent(BlockPos pos, EnumFacing facing) {
        this.pos = pos;
        this.facing = facing;
    }
}
