package me.gavin.notorious.hack.hacks.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack( name = "Step", description = "Automatically moves you up a block", category = Hack.Category.Movement)
public class Step extends Hack {

    @RegisterSetting
    public final ModeSetting stepType = new ModeSetting("StepType", "NCP", "NCP", "Vanilla");
    @RegisterSetting
    public final NumSetting stepHeight = new NumSetting("Height", 2f, 0.5f, 3f, 0.5f);

    private int ticks;

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + stepType.getMode() + ChatFormatting.RESET + "]";
    }
    @SubscribeEvent
    public void onLivingUpdate(PlayerLivingUpdateEvent event) {
        if(stepType.getMode().equals("NCP")) {
            final double[] dir = forward(0.1);
            boolean twofive = false;
            boolean two = false;
            boolean onefive = false;
            boolean one = false;
            if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 2.6, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 2.4, dir[1])).isEmpty()) {
                twofive = true;
            }
            if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 2.1, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.9, dir[1])).isEmpty()) {
                two = true;
            }
            if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.6, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.4, dir[1])).isEmpty()) {
                onefive = true;
            }
            if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.0, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 0.6, dir[1])).isEmpty()) {
                one = true;
            }
            if (mc.player.collidedHorizontally && (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) && mc.player.onGround) {
                if(one && stepHeight.getValue() >= 1.0) {
                    final double[] oneOffset = { 0.42, 0.753 };
                    for (int i = 0; i < oneOffset.length; ++i) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + oneOffset[i], mc.player.posZ, mc.player.onGround));
                    }
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
                    this.ticks = 1;
                }
                if(onefive && this.stepHeight.getValue() >= 1.5) {
                    final double[] oneFiveOffset = { 0.42, 0.75, 1.0, 1.16, 1.23, 1.2 };
                    for(int i = 0; i < oneFiveOffset.length; ++i) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + oneFiveOffset[i], mc.player.posZ, mc.player.onGround));
                    }
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 1.5, mc.player.posZ);
                    this.ticks = 1;
                }
                if(two && this.stepHeight.getValue() >= 2.0) {
                    final double[] twoOffset = { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 };
                    for(int i = 0; i < twoOffset.length; ++i) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + twoOffset[i], mc.player.posZ, mc.player.onGround));
                    }
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 2.0, mc.player.posZ);
                    this.ticks = 2;
                }
                if(twofive && this.stepHeight.getValue() >= 2.5) {
                    final double[] twoFiveOffset = { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 };
                    for(int i = 0; i < twoFiveOffset.length; ++i) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + twoFiveOffset[i], mc.player.posZ, mc.player.onGround));
                    }
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 2.5,     mc.player.posZ);
                    this.ticks = 2;
                }
            }
        }else {
            mc.player.stepHeight = stepHeight.getValue();
        }
    }

    public static double[] forward(final double speed) {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.5f;
    }
}
