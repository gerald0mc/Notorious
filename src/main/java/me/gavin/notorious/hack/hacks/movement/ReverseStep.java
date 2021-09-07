package me.gavin.notorious.hack.hacks.movement;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "ReverseStep", description = "ez", category = Hack.Category.Movement)
public class ReverseStep extends Hack {

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if(mc.player != null && mc.world != null && mc.player.onGround && !mc.player.isSneaking() && !mc.player.isInWater() && !mc.player.isDead && !mc.player.isInLava() && !mc.player.isOnLadder() && !mc.player.noClip && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
            if(mc.player.onGround) {
                mc.player.motionY -= 1.0;
            }
        }
    }
}
