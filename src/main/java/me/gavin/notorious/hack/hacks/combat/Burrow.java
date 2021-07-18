package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.BlockUtil;
import me.gavin.notorious.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

@RegisterHack(name = "Burrow", description = "ez", category = Hack.Category.Combat)
public class Burrow extends Hack {

    @RegisterSetting
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    @RegisterSetting
    public final BooleanSetting sneak = new BooleanSetting("Sneak", true);
    @RegisterSetting
    public final NumSetting offset = new NumSetting("Offset", 1f, -20f, 20f, 1f);

    private BlockPos originalPos;
    private int slot;
    private int oldSlot;

    @Override
    public void onEnable() {
        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        oldSlot = mc.player.inventory.currentItem;
        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) || intersectsWithEntity(this.originalPos)) {
            notorious.messageManager.sendMessage("Already in a block please leave this one before attempting again.");
            disable();
            return;
        }
        doBurrow();
        disable();
    }

    public void doBurrow() {
        if(mc.world == null)
            return;

        if (InventoryUtil.getItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1 && InventoryUtil.getItemSlot(Item.getItemFromBlock(Blocks.ENDER_CHEST)) == -1) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Can't find obsidian or echest in hotbar!"));
            disable();
            return;
        }

        if(InventoryUtil.getItemSlot(Item.getItemFromBlock(Blocks.ENDER_CHEST)) > 1) {
            slot = InventoryUtil.getItemSlot(Item.getItemFromBlock(Blocks.ENDER_CHEST));
            InventoryUtil.switchToSlot(slot);
        } else {
            slot = InventoryUtil.getItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN));
            InventoryUtil.switchToSlot(slot);
        }

        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));

        boolean sneaking = mc.player.isSneaking();
        if (sneak.getValue()) {
            if (sneaking) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            }
        }
        BlockUtil.placeBlock(originalPos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset.getValue(), mc.player.posZ, false));
        InventoryUtil.switchToSlot(oldSlot);
        if (sneak.getValue()) {
            if (sneaking) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
    }

    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }
}
