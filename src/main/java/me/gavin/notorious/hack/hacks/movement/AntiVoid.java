package me.gavin.notorious.hack.hacks.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "AntiVoid", description = "ez", category = Hack.Category.Movement)
public class AntiVoid extends Hack {

    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "TP", "TP", "Jump", "Freeze");

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + mode.getMode() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        double yLevel = mc.player.posY;
        if(mode.getMode().equals("TP")) {
            if(yLevel <= .5) {
                mc.player.setPosition(mc.player.posX, mc.player.posY + 2, mc.player.posZ);
                notorious.messageManager.sendMessage("Attempting to TP out of void hole.");
            }
        }else if(mode.getMode().equals("Jump")){
            if(yLevel <= .9) {
                mc.player.jump();
                notorious.messageManager.sendMessage("Attempting to jump out of void hole.");
            }
        }else {
            // skidded from zware :trol:
            if (!mc.player.noClip && yLevel <= 0.5D) {
                RayTraceResult trace = mc.world.rayTraceBlocks(mc.player.getPositionVector(), new Vec3d(mc.player.posX, 0.0D, mc.player.posZ), false, false, false);
                if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK)
                    return;
                mc.player.setVelocity(0.0D, 0.0D, 0.0D);
                if (mc.player.getRidingEntity() != null)
                    mc.player.getRidingEntity().setVelocity(0.0D, 0.0D, 0.0D);
            }
        }
    }
}
