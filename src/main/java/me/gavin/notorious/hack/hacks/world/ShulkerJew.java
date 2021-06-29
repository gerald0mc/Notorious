package me.gavin.notorious.hack.hacks.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.BlockUtil;
import me.gavin.notorious.util.NColor;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "ShulkerJew", description = "Automatically breaks shulkers.", category = Hack.Category.World)
public class ShulkerJew extends Hack {

    @RegisterSetting
    public final NumSetting range = new NumSetting("Range", 5f, 0f, 6f, 0.5f);
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new NColor(255, 255, 255, 125));
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new NColor(255, 255, 255, 255));

    private BlockPos targetedBlock = null;

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + range.getValue() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onLivingUpdate(PlayerLivingUpdateEvent event) {
        if (targetedBlock == null) {
            for (BlockPos pos : BlockUtil.getSurroundingBlocks(5, true)) {
                final Block block = mc.world.getBlockState(pos).getBlock();

                if (block instanceof BlockShulkerBox && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE) {
                    targetedBlock = pos;
                    break;
                }
            }
        } else {
            if (mc.world.getBlockState(targetedBlock).getBlock() == Blocks.AIR) {
                targetedBlock = null;
                return;
            }

            if (targetedBlock.getDistance(mc.player.getPosition().getX(), mc.player.getPosition().getY(), mc.player.getPosition().getZ()) > range.getValue()) {
                targetedBlock = null;
                return;
            }

            BlockUtil.damageBlock(targetedBlock, false, true);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (targetedBlock != null) {
            final AxisAlignedBB bb = new AxisAlignedBB(targetedBlock);
            RenderUtil.renderFilledBB(bb, boxColor.getAsColor());
            RenderUtil.renderOutlineBB(bb, outlineColor.getAsColor());
        }
    }
}
