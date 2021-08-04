package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

@RegisterHack(name = "HoleESP", description = "ez", category = Hack.Category.Render)
public class HoleESP extends Hack {

    private ArrayList<Block> getSafeHoles(EntityLivingBase e) {
        ArrayList<Block> surroundblocks = new ArrayList<>();
        BlockPos entityblock = new BlockPos(Math.floor(e.posX), Math.floor(e.posY), Math.floor(e.posZ));
        if(mc.world.getBlockState(entityblock.north()).getBlock() == Blocks.BEDROCK
                && mc.world.getBlockState(entityblock.east()).getBlock() == Blocks.BEDROCK
                && mc.world.getBlockState(entityblock.south()).getBlock() == Blocks.BEDROCK
                && mc.world.getBlockState(entityblock.west()).getBlock() == Blocks.BEDROCK) {
            surroundblocks.add(mc.world.getBlockState(entityblock.north()).getBlock());
            surroundblocks.add(mc.world.getBlockState(entityblock.east()).getBlock());
            surroundblocks.add(mc.world.getBlockState(entityblock.south()).getBlock());
            surroundblocks.add(mc.world.getBlockState(entityblock.west()).getBlock());
        }
        return surroundblocks;
    }
}
