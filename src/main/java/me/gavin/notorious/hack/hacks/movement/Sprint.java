package me.gavin.notorious.hack.hacks.movement;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @skidded from gavhack stay mad
 */

@RegisterHack(name = "Sprint", description = "Holds sprint.", category = Hack.Category.Movement)
public class Sprint extends Hack {

    @RegisterSetting
    public final ModeSetting sprintMode = new ModeSetting("SprintMode", "Legit", "Legit", "Rage");

    @SubscribeEvent
    public void onLivingUpdate(PlayerLivingUpdateEvent event) {
        if(sprintMode.getMode().equals("Legit")) {
            if (!mc.player.isSneaking() && !mc.player.isHandActive() && !mc.player.collidedHorizontally) {
                if (!mc.player.isSprinting())
                    mc.player.setSprinting(true);
            }
        }else {
            if (mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {
                if (!mc.player.isSprinting())
                    mc.player.setSprinting(true);
            }
        }
    }
}
