package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.event.events.PlayerDamageBlockEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.mixin.mixins.accessor.IRenderGlobal;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

@RegisterHack(name = "PacketMine", description = "ez", category = Hack.Category.Player)
public class PacketMine extends Hack {

    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Both", "Both", "Outline", "Box");
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new Color(117, 0, 255, 255));
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new Color(117, 0, 255, 65));
    @RegisterSetting
    public final BooleanSetting fade = new BooleanSetting("Fade", true);

    private BlockPos renderBlock;
    public boolean fill;
    public boolean outline;

    @SubscribeEvent
    public void onEvent(PlayerInteractEvent.LeftClickBlock event) {
        if(canBreak(event.getPos())) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
            if(renderBlock == null)
                renderBlock = event.getPos();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (renderBlock != null && mc.world.getBlockState(renderBlock).getBlock() == Blocks.AIR) {
            renderBlock = null;
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
        ((IRenderGlobal) mc.renderGlobal).getDamagedBlocks().forEach((integer, destroyBlockProgress) -> {
            if (renderBlock != null) {
                AxisAlignedBB bb = new AxisAlignedBB(renderBlock);
                if (fade.isEnabled())
                    bb = bb.shrink((3 - (destroyBlockProgress.getPartialBlockDamage()) / (2.0 + (2.0 / 3.0))) / 9.0);
                if (outline)
                    RenderUtil.renderOutlineBB(bb, new Color(255, 255, 255));
                if (fill)
                    RenderUtil.renderFilledBB(bb, new Color(255, 255, 255, 255));
            }
        });
    }

    private boolean canBreak(BlockPos pos) {
        final IBlockState blockState = mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();

        if(mc.world.getBlockState(pos).getBlock() == Blocks.AIR)
            return false;

        return block.getBlockHardness(blockState, mc.world, pos) != -1;
    }
}
