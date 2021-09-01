package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "ToggleMessage", description = "ez", category = Hack.Category.Chat)
public class ToggleMessage extends Hack {

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        for (Hack hack : notorious.hackManager.getHacks()) {
            if(!hack.isEnabled() && (System.currentTimeMillis() - hack.lastDisabledTime) < 250) {
                notorious.messageManager.sendRemovableMessage(ChatFormatting.BOLD + hack.getName() + ChatFormatting.RESET + ChatFormatting.RED + ChatFormatting.BOLD + " DISABLED!", 1);
            }else if(hack.isEnabled() && (System.currentTimeMillis() - hack.lastEnabledTime) < 250) {
                notorious.messageManager.sendRemovableMessage(ChatFormatting.BOLD + hack.getName() + ChatFormatting.RESET + ChatFormatting.GREEN + ChatFormatting.BOLD + " ENABLED!", 1);
            }
        }
    }
}
