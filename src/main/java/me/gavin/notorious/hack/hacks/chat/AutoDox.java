package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.TickTimer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@RegisterHack(name = "AutoDox", description = "ez", category = Hack.Category.Chat)
public class AutoDox extends Hack {

    @RegisterSetting
    public final ModeSetting player = new ModeSetting("Player", "Sjnez", "Sjnez", "5nt");
    @RegisterSetting
    public final NumSetting delay = new NumSetting("Delay", 10, 1, 15, 1);

    public TickTimer timer = new TickTimer();

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + player.getMode() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onUpdate(TickEvent event) {
        if(mc.player != null && mc.world != null) {
            if (timer.hasTicksPassed((long) (delay.getValue() * 20))) {
                String dox = "";
                if(player.getMode().equals("Sjnez")) {
                    dox = "Name: Samuel J Nezzbit Address: 1820 261 St, Los Angelos, California Email: samuelnez@gmail.com Phone#: (310)-538-8734 (310)-371-4746";
                }else if(player.getMode().equals("5nt")){
                    dox = "Name: Jake Findlay Piggot Address: 179 Ikwikws RdWest Vancouver Email: jakeyisad@gmail.com";
                }
                mc.player.sendChatMessage(dox);
                timer.reset();
            }
        }
    }
}
