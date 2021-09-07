package me.gavin.notorious.hack.hacks.movement;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author gerald0mc
 * @since 7/5/21
 * :troll:
 */

@RegisterHack(name = "AutoHop", description = "ching chong autohop", category = Hack.Category.Movement)
public class AutoHop extends Hack {

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if(mc.player.onGround && !mc.player.isSneaking() && !mc.player.isInLava() && !mc.player.isInWater() && !mc.player.isOnLadder() && !mc.player.noClip && !mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.jump();
        }
    }
}
