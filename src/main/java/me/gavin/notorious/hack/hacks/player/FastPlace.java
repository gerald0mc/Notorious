package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterValue;
import me.gavin.notorious.setting.Value;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Gav06
 * @since 6/15/2021
 */

@RegisterHack(name = "FastPlace", description = "Use items faster", category = Hack.Category.Player)
public class FastPlace extends Hack {

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        mc.rightClickDelayTimer = 0;
    }
}