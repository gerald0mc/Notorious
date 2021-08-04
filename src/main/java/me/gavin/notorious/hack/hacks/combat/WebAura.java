package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.BlockUtil;
import me.gavin.notorious.util.RenderUtil;
import me.gavin.notorious.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gerald0mc alone wait not skidded!??!!??!/!
 * @since 7/21/21
 */

@RegisterHack(name = "WebAura", description = "ez", category = Hack.Category.Combat)
public class WebAura extends Hack {

    @RegisterSetting
    public final NumSetting delay = new NumSetting("Delay", 5, 1, 10, 1);
    @RegisterSetting
    public final NumSetting range = new NumSetting("Range", 4, 1, 6, 1);
    @RegisterSetting
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    @RegisterSetting
    public final BooleanSetting packet = new BooleanSetting("Packet", true);
    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Both", "Both", "Outline", "Box");
    @RegisterSetting
    public final BooleanSetting render = new BooleanSetting("Render", true);
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new Color(255, 255, 255, 255));
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new Color(255, 255, 255, 125));

    public BlockPos pos;
    public List<BlockPos> target = new ArrayList<>();
    public boolean fill;
    public boolean outline;
    public Timer timer = new Timer();

    @SubscribeEvent
    public void onTick(TickEvent event) {
        target.clear();
        for(EntityPlayer e : mc.world.playerEntities) {
            if(e.equals(mc.player) || e.isInWater() || e.isInLava())
                return;
            if(e.getDistance(mc.player) <= range.getValue()) {
                pos = new BlockPos(e.posX, e.posY - 1D, e.posZ);
                if(mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.WEB) && mc.player.ticksExisted == (int) delay.getValue() && !mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                    target.add(pos);
                    BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for(BlockPos blockPos : target) {
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
            if(outline && render.isEnabled())
                RenderUtil.renderOutlineBB(new AxisAlignedBB(blockPos), outlineColor.getAsColor());
            if(fill && render.isEnabled())
                RenderUtil.renderFilledBB(new AxisAlignedBB(blockPos), boxColor.getAsColor());
        }
    }
}
