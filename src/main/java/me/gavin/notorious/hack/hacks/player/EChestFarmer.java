package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.util.BlockUtil;
import me.gavin.notorious.util.InventoryUtil;
import me.gavin.notorious.util.NColor;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@RegisterHack(name = "EChestFarmer", description = "ez", category = Hack.Category.Player)
public class EChestFarmer extends Hack {
    
    @RegisterSetting
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    @RegisterSetting
    public final BooleanSetting packet = new BooleanSetting("Packet", true);
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new NColor(255, 255, 255, 125));
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new NColor(255, 255, 255, 255));

    public BlockPos result = null;
    public int slot;

    @SubscribeEvent
    public void onUpdate(TickEvent event) {
        slot = InventoryUtil.checkSlotsBlock(Blocks.ENDER_CHEST);
        result = mc.objectMouseOver.getBlockPos();
        if(slot != -1) {
            InventoryUtil.switchToHotbarSlot(slot, false);
        }
        if(slot == -1) {
            notorious.messageManager.sendMessage("No echests in hotbar to farm sorry.");
            toggle();
            return;
        }
        if(!BlockUtil.canBeClicked(result)) {
            notorious.messageManager.sendMessage("Please have your cursor over a placeable block.");
            toggle();
            return;
        }
        if(BlockUtil.canBeClicked(result) && mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.ENDER_CHEST)) {
            BlockUtil.placeBlock(result, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), true);
        }
        for(BlockPos pos : BlockUtil.getSurroundingBlocks(5, true)) {
            final Block block = mc.world.getBlockState(pos).getBlock();
            if(block instanceof BlockEnderChest) {
                slot = InventoryUtil.getItemSlot(Items.DIAMOND_PICKAXE);
                if(slot != -1) {
                    InventoryUtil.switchToHotbarSlot(slot, false);
                }
                if(mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                    BlockUtil.damageBlock(result, packet.getValue(), rotate.getValue());
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(result != null) {
            final AxisAlignedBB bb = new AxisAlignedBB(result);
            RenderUtil.renderOutlineBB(bb, outlineColor.getAsColor());
            RenderUtil.renderFilledBB(bb, boxColor.getAsColor());
        }
    }
}
