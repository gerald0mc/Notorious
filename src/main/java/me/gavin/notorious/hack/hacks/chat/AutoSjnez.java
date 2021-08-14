package me.gavin.notorious.hack.hacks.chat;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.TickTimer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@RegisterHack(name = "AutoSjnez", description = "ez", category = Hack.Category.Chat)
public class AutoSjnez extends Hack {

    @RegisterSetting
    public final NumSetting delay = new NumSetting("Delay", 2, 1, 5, 1);
    @RegisterSetting
    public final BooleanSetting autoMessageSjnez = new BooleanSetting("AutoMSGSjnez", false);

    public TickTimer timer = new TickTimer();

    @SubscribeEvent
    public void onUpdate(TickEvent event) {
        if(mc.player != null && mc.world != null) {
            if (timer.hasTicksPassed((long) (delay.getValue() * 20))) {
                for(EntityPlayer e : mc.world.playerEntities) {
                    if(autoMessageSjnez.isEnabled() && e.getDisplayNameString().equalsIgnoreCase("sjnez")) {
                        mc.player.sendChatMessage("/msg " + e.getDisplayNameString() + " LMFAO FUCK YOU SAMUEL J NEZZBIT LLLLLLLLL");
                    }
                }
                mc.player.sendChatMessage("Name: Samuel J Nezzbit Address: 1820 261 St, Los Angelos, California Email: samuelnez@gmail.com Phone#: (310)-538-8734 (310)-371-4746");
                timer.reset();
            }
        }
    }
}
