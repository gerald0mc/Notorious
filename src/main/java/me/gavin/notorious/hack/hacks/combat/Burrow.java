package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.BlockUtil;
import me.gavin.notorious.util.InventoryUtil;
import me.gavin.notorious.util.MappingUtil;
import me.gavin.notorious.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;

@RegisterHack(name = "Burrow", description = "ez", category = Hack.Category.Combat)
public class Burrow extends Hack {

    @RegisterSetting
    public final ModeSetting type = new ModeSetting("Type", "Packet", "Packet", "Normal");
    @RegisterSetting
    public final ModeSetting block = new ModeSetting("Block", "Obsidian", "Obsidian", "EChest", "Chest");
    @RegisterSetting
    public final NumSetting force = new NumSetting("Force", 1.5f, -5.0f, 10.0f, 0.1f);
    @RegisterSetting
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    @RegisterSetting
    public final BooleanSetting instant = new BooleanSetting("Instant", true);
    @RegisterSetting
    public final BooleanSetting auto = new BooleanSetting("Auto", true);
    @RegisterSetting
    public final NumSetting range = new NumSetting("Range", 4, 1, 6, 0.25f);
    @RegisterSetting
    public final BooleanSetting center = new BooleanSetting("Center", false);
    @RegisterSetting
    public final BooleanSetting bypass = new BooleanSetting("Bypass", false);

    int swapBlock = -1;
    Vec3d centerBlock = Vec3d.ZERO;
    BlockPos oldPos;
    Block blockW = Blocks.OBSIDIAN;
    boolean flag;

    @Override
    public void onEnable() {
        mc.player.motionX = 0;
        mc.player.motionZ = 0;
        centerBlock = this.getCenter(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (centerBlock != Vec3d.ZERO && center.getValue()) {
            double x_diff = Math.abs(centerBlock.x - mc.player.posX);
            double z_diff = Math.abs(centerBlock.z - mc.player.posZ);
            if (x_diff <= 0.1 && z_diff <= 0.1) {
                centerBlock = Vec3d.ZERO;
            } else {
                double motion_x = centerBlock.x - mc.player.posX;
                double motion_z = centerBlock.z - mc.player.posZ;
                mc.player.motionX = motion_x / 2;
                mc.player.motionZ = motion_z / 2;
            }
        }

        oldPos = mc.player.getPosition();
        switch (block.getMode()) {
            case "Obsidian":
                swapBlock = InventoryUtil.checkSlotsBlock(Blocks.OBSIDIAN);
                break;
            case "EChest":
                swapBlock = InventoryUtil.checkSlotsBlock(Blocks.ENDER_CHEST);
                break;
            case "Chest":
                swapBlock = InventoryUtil.checkSlotsBlock(Blocks.CHEST);
        }
        if (swapBlock == -1) {
            this.disable();
            return;
        }
        if (instant.getValue()) {
            this.setTimer(50f);
        }
        if (type.getMode().equals("Normal")) {
            mc.player.jump();
        }
    }

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if (type.getMode().equals("Normal")) {
            if (mc.player.posY > (oldPos.getY() + 1.04)) {
                int old = mc.player.inventory.currentItem;
                InventoryUtil.switchToSlot(swapBlock);
                BlockUtil.placeBlock(oldPos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);
                InventoryUtil.switchToSlot(old);
                mc.player.motionY = force.getValue();
                this.disable();
            }
        } else {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997, mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214, mc.player.posZ, true));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821, mc.player.posZ, true));
            int old = mc.player.inventory.currentItem;
            InventoryUtil.switchToSlot(swapBlock);
            BlockUtil.placeBlock(oldPos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);
            InventoryUtil.switchToSlot(old);
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + force.getValue(), mc.player.posZ, false));
            if(bypass.getValue() && !mc.player.isSneaking()) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                mc.player.setSneaking(true);
                mc.playerController.updateController();
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                mc.player.setSneaking(false);
                mc.playerController.updateController();
            }
            this.disable();
        }
    }

    private void setTimer(float value) {
        try {
            Field timer = Minecraft.class.getDeclaredField(MappingUtil.timer);
            timer.setAccessible(true);
            Field tickLength = Timer.class.getDeclaredField(MappingUtil.tickLength);
            tickLength.setAccessible(true);
            tickLength.setFloat(timer.get(mc), 50.0F / value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Vec3d getCenter(double posX, double posY, double posZ) {
        double x = Math.floor(posX) + 0.5D;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5D ;

        return new Vec3d(x, y, z);
    }
}
