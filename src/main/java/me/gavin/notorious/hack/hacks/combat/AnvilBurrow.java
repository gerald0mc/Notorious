package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.mixin.mixins.accessor.IMinecraftMixin;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.util.BlockUtil;
import me.gavin.notorious.util.InventoryUtil;
import me.gavin.notorious.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

@RegisterHack(name = "AnvilBurrow", description = "Drops a anvil inside of you to act as a burrow.", category = Hack.Category.Combat, bind = Keyboard.KEY_H)
public class AnvilBurrow extends Hack {

    @RegisterSetting
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    @RegisterSetting
    public final BooleanSetting packet = new BooleanSetting("Packet", false);
    @RegisterSetting
    public final BooleanSetting switchToAnvil = new BooleanSetting("SwitchToAnvil", true);

    int slot;
    public float yaw = 0.0f;
    public float pitch = 0.0f;
    public boolean rotating = false;
    private BlockPos placeTarget;
    public ItemAnvilBlock anvil;
    private EntityPlayer finalTarget;

    @Override
    public void onEnable() {
        doAnvilBurrow();
        toggle();
    }

    private void doAnvilBurrow() {
        finalTarget = mc.player;
        if(finalTarget != null) {
            placeTarget = getTargetPos(finalTarget);
        }
        if(placeTarget != null && finalTarget != null) {
            placeAnvil(placeTarget);
        }
    }

    public void placeAnvil(BlockPos pos) {
        if (this.rotate.isEnabled()) {
            this.rotateToPos(pos);
        }
        if (this.switchToAnvil.isEnabled() && !isHoldingAnvil()) {
            this.switchToAnvil();
        }
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, false, this.packet.getValue(), mc.player.isSneaking());
    }

    private boolean isHoldingAnvil() {
        if(mc.player.getHeldItemMainhand().getItem() == anvil)
            return true;
        else
            return false;
    }

    private void switchToAnvil() {
        if(mc.player.getHeldItemMainhand().getItem() != anvil) {
            slot = InventoryUtil.getItemSlot(anvil);
            if(slot != -1) {
                InventoryUtil.switchToSlot(slot);
            }
        }
    }

    public List<BlockPos> getPlaceableBlocksAboveEntity(Entity target) {
        BlockPos pos;
        BlockPos playerPos = new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        for (int i = (int)Math.floor(mc.player.posY + 2.0); i <= 256 && BlockUtil.isPositionPlaceable(pos = new BlockPos(Math.floor(mc.player.posX), (double)i, Math.floor(mc.player.posZ)), false) != 0 && BlockUtil.isPositionPlaceable(pos, false) != -1 && BlockUtil.isPositionPlaceable(pos, false) != 2; ++i) {
            positions.add(pos);
        }
        return positions;
    }

    public BlockPos getTargetPos(Entity target) {
        double distance = -1.0;
        BlockPos finalPos = null;
        for (BlockPos pos : this.getPlaceableBlocksAboveEntity(target)) {
            if (distance != -1.0 && !(mc.player.getDistanceSq(pos) < MathUtil.square(distance))) continue;
            finalPos = pos;
            distance = mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        }
        return finalPos;
    }

    public void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float)(vec.x - (double)pos.getX());
            float f1 = (float)(vec.y - (double)pos.getY());
            float f2 = (float)(vec.z - (double)pos.getZ());
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, direction, vec, hand);
        }
        mc.player.swingArm(EnumHand.MAIN_HAND);
        ((IMinecraftMixin)mc).setRightClickDelayTimerAccessor(4);
    }

    private void rotateToPos(BlockPos pos) {
        if (this.rotate.isEnabled()) {
            float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double)((float)pos.getX() + 0.5f), (double)((float)pos.getY() - 0.5f), (double)((float)pos.getZ() + 0.5f)));
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }
}
