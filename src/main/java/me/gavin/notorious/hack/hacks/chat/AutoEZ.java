package me.gavin.notorious.hack.hacks.chat;

import me.gavin.notorious.event.events.TotemPopEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.TickTimer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "AutoEZ", description = "ez", category = Hack.Category.Chat)
public class AutoEZ extends Hack {

    @RegisterSetting
    public final NumSetting delay = new NumSetting("Delay", 5, 1, 15, 1);

    public TickTimer timer = new TickTimer();

    public void onDeath(String name) {
        if(timer.hasTicksPassed((long) delay.getValue() * 20)) {
            mc.player.sendChatMessage("LOL EZ " + name + " stay dead nn!");
        }
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if(event.getPopCount() == 1) {
            if(timer.hasTicksPassed((long) delay.getValue() * 20)) {
                mc.player.sendChatMessage("OMG " + event.getName() + " your so bad stop popping!");
                timer.reset();
            }
        }else {
            if(timer.hasTicksPassed((long) delay.getValue() * 20)) {
                mc.player.sendChatMessage("OMG " + event.getName() + " your so bad stop popping!");
                timer.reset();
            }
        }
    }
}
