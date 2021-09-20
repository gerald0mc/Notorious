package me.gavin.notorious.hack.hacks.render;

import com.google.common.eventbus.Subscribe;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
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
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RegisterHack(name = "HoleESP", description = "ez", category = Hack.Category.Render)
public class HoleESP extends Hack {
    @RegisterSetting
    public final NumSetting range = new NumSetting("Range", 5f, 1f, 16f, 1f);
    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Both", "Both", "Outline", "Box");
    @RegisterSetting
    public final ColorSetting bedrockOutlineColor = new ColorSetting("SafeOutlineColor", 255, 0, 0, 255);
    @RegisterSetting
    public final ColorSetting bedrockFillColor = new ColorSetting("SafeFillColor", 255, 0, 0, 125);
    @RegisterSetting
    public final ColorSetting obsidianOutlineColor = new ColorSetting("UnSafeOutlineColor", 0, 0, 255, 255);
    @RegisterSetting
    public final ColorSetting obsidianFillColor = new ColorSetting("UnSafeFillColor", 0, 0, 255, 125);

    private List<BlockPos> holes = new ArrayList<>();
    public boolean fill;
    public boolean outline;
    private final BlockPos[] surroundOffset = BlockUtil.toBlockPos(BlockUtil.holeOffsets);

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if (this.mc.player.ticksExisted % 2 == 0)
            return;
        this.holes = this.calcHoles();
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        int size = this.holes.size();
        for (BlockPos pos : this.holes) {
            Color fillColor = this.isSafe(pos) ? new Color(this.bedrockFillColor.getAsColor().getRed(), this.bedrockFillColor.getAsColor().getGreen(), this.bedrockFillColor.getAsColor().getBlue(), 125) : new Color(this.obsidianFillColor.getAsColor().getRed(), this.obsidianFillColor.getAsColor().getGreen(), this.obsidianFillColor.getAsColor().getBlue(), 125);
            Color outlineColor = this.isSafe(pos) ? new Color(this.bedrockOutlineColor.getAsColor().getRed(), this.bedrockOutlineColor.getAsColor().getGreen(), this.bedrockOutlineColor.getAsColor().getBlue()) : new Color(this.obsidianOutlineColor.getAsColor().getRed(), this.obsidianOutlineColor.getAsColor().getGreen(), this.obsidianOutlineColor.getAsColor().getBlue());
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
            if(fill)
                RenderUtil.renderFilledBB(new AxisAlignedBB(pos),fillColor);
            if(outline)
                RenderUtil.renderOutlineBB(new AxisAlignedBB(pos),outlineColor);
        }
    }

    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> safeSpots = new ArrayList<>( );
        List<BlockPos> positions = BlockUtil.getSphere(this.range.getValue(), false);
        int size = positions.size();
        for (BlockPos pos : positions) {
            if ( ! this.mc.world.getBlockState ( pos ).getBlock ( ).equals ( Blocks.AIR ) || ! this.mc.world.getBlockState ( pos.add ( 0 , 1 , 0 ) ).getBlock ( ).equals ( Blocks.AIR ) || ! this.mc.world.getBlockState ( pos.add ( 0 , 2 , 0 ) ).getBlock ( ).equals ( Blocks.AIR ) )
                continue;
            boolean isSafe = true;
            for (BlockPos offset : this.surroundOffset) {
                Block block = this.mc.world.getBlockState ( pos.add ( offset ) ).getBlock ( );
                if ( block == Blocks.BEDROCK || block == Blocks.OBSIDIAN ) continue;
                isSafe = false;
            }
            if ( ! isSafe ) continue;
            safeSpots.add ( pos );
        }
        return safeSpots;
    }

    private boolean isSafe(BlockPos pos) {
        boolean isSafe = true;
        for (BlockPos offset : this.surroundOffset) {
            if (this.mc.world.getBlockState(pos.add( offset )).getBlock() == Blocks.BEDROCK) continue;
            isSafe = false;
            break;
        }
        return isSafe;
    }
}
