package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterValue;
import me.gavin.notorious.setting.Value;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "FastPlace", description = "Use items faster", category = Hack.Category.Player)
public class FastPlace extends Hack {

    @RegisterValue
    public final Value<Float> cumShotAmount = new Value<>("Shit", 1f, 0.1f, 5f);

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        mc.rightClickDelayTimer = 0;
    }
}