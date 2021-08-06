package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.BlockUtil;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

@RegisterHack(name = "HoleESP", description = "ez", category = Hack.Category.Render)
public class HoleESP extends Hack {
    @RegisterSetting
    public final NumSetting range = new NumSetting("Range", 15.0f, 1.0f, 30.0f, 1.0f);
    @RegisterSetting
    public final NumSetting rangeY = new NumSetting("RangeY", 15.0f, 1.0f, 30.0f, 1.0f);
    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Both", "Both", "Outline", "Box");
    @RegisterSetting
    public final ColorSetting safeOutlineColor = new ColorSetting("Outline", new Color(117, 0, 255, 255));
    @RegisterSetting
    public final ColorSetting safeBoxColor = new ColorSetting("Box", new Color(117, 0, 255, 65));
    @RegisterSetting
    public final ColorSetting unsafeOutlineColor = new ColorSetting("Outline", new Color(117, 0, 255, 255));
    @RegisterSetting
    public final ColorSetting unsafeBoxColor = new ColorSetting("Box", new Color(117, 0, 255, 65));

    public boolean fill;
    public boolean outline;
    public static HoleESP INSTANCE;

    public HoleESP() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        assert mc.getRenderViewEntity() != null;
        final Vec3i playerPos = new Vec3i(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        for (int x = (int) (playerPos.getX() - this.range.getValue()); x < playerPos.getX() + this.range.getValue(); ++x) {
            for (int z = (int) (playerPos.getZ() - this.range.getValue()); z < playerPos.getZ() + this.range.getValue(); ++z) {
                for (int y = (int) (playerPos.getY() + this.rangeY.getValue()); y > playerPos.getY() - this.rangeY.getValue(); --y) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && (!pos.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)))) {
                        if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                            if(outline)
                                RenderUtil.renderOutlineBB(new AxisAlignedBB(pos), safeOutlineColor.getAsColor());
                            if(fill)
                                RenderUtil.renderFilledBB(new AxisAlignedBB(pos), safeBoxColor.getAsColor());
                        }else if(isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.down()).getBlock()) && isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.east()).getBlock()) && isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.west()).getBlock()) && isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.south()).getBlock())) {
                            if(isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.north()).getBlock())) {
                                if(outline)
                                    RenderUtil.renderOutlineBB(new AxisAlignedBB(pos), unsafeOutlineColor.getAsColor());
                                if(fill)
                                    RenderUtil.renderFilledBB(new AxisAlignedBB(pos), unsafeBoxColor.getAsColor());
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isBlockUnSafe(final Block block) {
        return BlockUtil.unSafeBlocks.contains(block);
    }
}
