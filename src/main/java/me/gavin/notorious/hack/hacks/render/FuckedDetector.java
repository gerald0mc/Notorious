package me.gavin.notorious.hack.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.util.BlockUtil;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gerald0mc and gav06
 * @since 7/18/21
 * #TODO fix the echest shit
 */

@RegisterHack(name = "FuckedDetector", description = "ez", category = Hack.Category.Render)
public class FuckedDetector extends Hack {

    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Both", "Both", "Outline", "Box");
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new Color(255, 255, 255, 255));
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new Color(255, 255, 255, 125));
    @RegisterSetting
    public final BooleanSetting self = new BooleanSetting("Self", true);

    public BlockPos pos;
    public boolean fill;
    public boolean outline;
    public List<BlockPos> fuckedEntities = new ArrayList<>();

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + mode.getMode() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        fuckedEntities.clear();
        for(EntityPlayer e : mc.world.playerEntities) {
            if(e.equals(mc.player) && !self.isEnabled())
                return;
            pos = new BlockPos(e.posX, e.posY, e.posZ);
            if(canPlaceCrystal(pos))
                fuckedEntities.add(pos);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(mode.getMode().equals("Both")) {
            outline = true;
            fill = true;
        }else if(mode.getMode().equals("Outline")) {
            outline = true;
            fill = false;
        }else {
            fill = true;
            outline = false;
        }
        for(BlockPos blockPos : fuckedEntities) {
            if(outline)
                RenderUtil.renderOutlineBB(new AxisAlignedBB(blockPos), outlineColor.getAsColor());
            if(fill)
                RenderUtil.renderFilledBB(new AxisAlignedBB(blockPos), boxColor.getAsColor());
        }
    }

    //This was taken from w+2 because I couldn't figure out how to do this
    public boolean canPlaceCrystal(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();
        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
            Block floor = mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
            Block ceil = mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();
            if (floor == Blocks.AIR && ceil == Blocks.AIR && mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos.add(0, 1, 0))).isEmpty() && mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos.add(0, 2, 0))).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
