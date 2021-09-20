package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.BlockUtil;
import me.gavin.notorious.util.rewrite.InventoryUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@RegisterHack(name = "Burrow", description = "ez", category = Hack.Category.Combat)
public class Burrow extends Hack {

    @RegisterSetting private final NumSetting height = new NumSetting("Height", 5, -5, 5, 1);
    @RegisterSetting private final BooleanSetting preferEChests = new BooleanSetting("PreferEChests", true);
    @RegisterSetting private final BooleanSetting lessPackets = new BooleanSetting("ConservePackets", false);

    private static Burrow INSTANCE;
    public BlockPos startPos;

    private int obbySlot = -1;

    public Burrow() {
        INSTANCE = this;
    }

    public static Burrow getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        if(mc.player != null && mc.world != null) {
            toggle();
            return;
        }
        this.obbySlot = InventoryUtil.findBlock(Blocks.OBSIDIAN, 0, 9);
        int eChestSlot = InventoryUtil.findBlock(Blocks.ENDER_CHEST, 0, 9);
        if ((this.preferEChests.getValue() || this.obbySlot == -1) && eChestSlot != -1) {
            this.obbySlot = eChestSlot;
        } else {
            this.obbySlot = InventoryUtil.findBlock(Blocks.OBSIDIAN, 0, 9);
            if (this.obbySlot == -1) {
                notorious.messageManager.sendError("Toggling no blocks to place with.");
                this.disable();
            }
        }
    }

    @Override
    public void onUpdate() {
        if(mc.player != null && mc.world != null)
            return;
        int startSlot = this.mc.player.inventory.currentItem;
        this.mc.getConnection().sendPacket(new CPacketHeldItemChange(this.obbySlot));
        this.startPos = new BlockPos(this.mc.player.getPositionVector());
        if (this.lessPackets.getValue()) {
            this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 0.45, this.mc.player.posZ, true));
            this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 0.79, this.mc.player.posZ, true));
            this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 1.1, this.mc.player.posZ, true));
        } else {
            this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 0.41, this.mc.player.posZ, true));
            this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 0.75, this.mc.player.posZ, true));
            this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 1.0, this.mc.player.posZ, true));
            this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 1.16, this.mc.player.posZ, true));
        }
        boolean onEChest = this.mc.world.getBlockState(new BlockPos(this.mc.player.getPositionVector())).getBlock() == Blocks.ENDER_CHEST;
        if (placeBlock(onEChest ? this.startPos.up() : this.startPos) && !this.mc.player.capabilities.isCreativeMode) {
            this.mc.getConnection().sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + (double)this.height.getValue(), this.mc.player.posZ, false));
        }
        if (startSlot != -1) {
            this.mc.getConnection().sendPacket(new CPacketHeldItemChange(startSlot));
        }
        this.disable();
    }

    public boolean placeBlock(BlockPos pos) {
        if (!BlockUtil.mc.world.getBlockState(pos).getBlock().isReplaceable((IBlockAccess)BlockUtil.mc.world, pos)) {
            return false;
        }
        boolean alreadySneaking = BlockUtil.mc.player.isSneaking();
        for (int i = 0; i < 6; ++i) {
            EnumFacing side = EnumFacing.values()[i];
            IBlockState offsetState = BlockUtil.mc.world.getBlockState(pos.offset(side));
            if (!offsetState.getBlock().canCollideCheck(offsetState, false) || offsetState.getMaterial().isReplaceable()) continue;
            boolean activated = offsetState.getBlock().onBlockActivated(BlockUtil.mc.world, pos, BlockUtil.mc.world.getBlockState(pos), (EntityPlayer)BlockUtil.mc.player, EnumHand.MAIN_HAND, side, 0.5f, 0.5f, 0.5f);
            if (activated && !alreadySneaking) {
                mc.getConnection().sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            }
            mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos.offset(side), side.getOpposite(), EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
            mc.player.swingArm(EnumHand.MAIN_HAND);
            if (!activated || alreadySneaking) continue;
            mc.getConnection().sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        return true;
    }
}
