package me.gavin.notorious.hack.hacks.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.friend.Friends;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.hack.hacks.player.PacketMine;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.RenderUtil;
import me.gavin.notorious.util.RotationUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

@RegisterHack(name = "PacketAutoCity", description = "ez", category = Hack.Category.Combat)
public class PacketAutoCity extends Hack {

    @RegisterSetting
    public final NumSetting range = new NumSetting("Range", 7, 0, 9, 1);
    @RegisterSetting
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Both", "Both", "Outline", "Box");
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new Color(117, 0, 255, 255));
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new Color(117, 0, 255, 65));

    private boolean firstRun;
    private BlockPos mineTarget = null;
    private BlockPos renderBlock = null;
    private EntityPlayer closestTarget;

    @Override
    public void onEnable() {
        firstRun = true;
    }

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        findClosestTarget();
        if (closestTarget == null) {
            if (firstRun) {
                firstRun = false;
                notorious.messageManager.sendMessage(ChatFormatting.WHITE.toString() + "Enabled" + ChatFormatting.RESET + ", no one to city!");
            }
            toggle();
            return;
        }
        if (firstRun && mineTarget != null) {
            firstRun = false;
            notorious.messageManager.sendMessage(TextFormatting.WHITE + " Attempting to mine: " + ChatFormatting.BLUE + closestTarget.getName());
        }
        findCityBlock();
        if(mineTarget != null) {
            int newSlot = -1;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack == ItemStack.EMPTY) {
                    continue;
                }
                if ((stack.getItem() instanceof ItemPickaxe)) {
                    newSlot = i;
                    break;
                }
            }
            if (newSlot != -1) {
                mc.player.inventory.currentItem = newSlot;
            }
            boolean wasEnabled = notorious.hackManager.getHack(PacketMine.class).isEnabled();
            if(!wasEnabled) {
                notorious.hackManager.getHack(PacketMine.class).toggle();
            }
            Vec3d target = new Vec3d(mineTarget);
            if(rotate.isEnabled()) {
                RotationUtil.faceVector(target, true);
            }
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, mineTarget, EnumFacing.DOWN));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mineTarget, EnumFacing.DOWN));
            if(!wasEnabled) {
                notorious.hackManager.getHack(PacketMine.class).toggle();
            }
            toggle();
        } else {
            notorious.messageManager.sendMessage("No city blocks to mine!");
            toggle();
        }
    }

    public BlockPos findCityBlock() {
        Double dist = (double) this.range.getValue();
        Vec3d vec = closestTarget.getPositionVector();
        if(mc.player.getPositionVector().distanceTo(vec) <= dist) {
            BlockPos targetX = new BlockPos(vec.add(1, 0, 0));
            BlockPos targetXMinus = new BlockPos(vec.add(-1, 0, 0));
            BlockPos targetZ = new BlockPos(vec.add(0, 0, 1));
            BlockPos targetZMinus = new BlockPos(vec.add(0, 0, -1));
            if(canBreak(targetX)) {
                mineTarget = targetX;
            }
            if(!canBreak(targetX) && canBreak(targetXMinus)) {
                mineTarget = targetXMinus;
            }
            if(!canBreak(targetX) && !canBreak(targetXMinus) && canBreak(targetZ)) {
                mineTarget = targetZ;
            }
            if(!canBreak(targetX) && !canBreak(targetXMinus) && !canBreak(targetZ) && canBreak(targetZMinus)) {
                mineTarget = targetZMinus;
            }
            if((!canBreak(targetX) && !canBreak(targetXMinus) && !canBreak(targetZ) && !canBreak(targetZMinus)) || mc.player.getPositionVector().distanceTo(vec) > dist) {
                mineTarget = null;
            }
        }
        renderBlock = mineTarget;
        return mineTarget;
    }

    private boolean canBreak(BlockPos pos) {
        final IBlockState blockState = mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, mc.world, pos) != -1;
    }

    private void findClosestTarget() {
        List<EntityPlayer> playerList = mc.world.playerEntities;

        closestTarget = null;

        for (EntityPlayer target : playerList) {
            if (target == mc.player) {
                continue;
            }
            if (!isLiving(target)) {
                continue;
            }
            if ((target).getHealth() <= 0) {
                continue;
            }
            if (closestTarget == null) {
                closestTarget = target;
                continue;
            }
            if (mc.player.getDistance(target) < mc.player.getDistance(closestTarget)) {
                closestTarget = target;
            }
        }
    }

    public static boolean isLiving(Entity e) {
        return e instanceof EntityLivingBase;
    }
}
