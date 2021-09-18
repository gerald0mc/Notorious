package me.gavin.notorious.hack.hacks.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "ToggleMessage", description = "ez", category = Hack.Category.Client)
public class ToggleMessage extends Hack {

    @RegisterSetting public final ModeSetting messageMode = new ModeSetting("MessageMode", "Removable", "Removable", "Normal");

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        for (Hack hack : notorious.hackManager.getHacks()) {
            if(!hack.isEnabled() && (System.currentTimeMillis() - hack.lastDisabledTime) < 50) {
                if(messageMode.getMode().equals("Removable")) {
                    notorious.messageManager.sendRemovableMessage(ChatFormatting.BOLD + hack.getName() + ChatFormatting.RESET + ChatFormatting.RED + ChatFormatting.BOLD + " DISABLED!", 1);
                }else {
                    notorious.messageManager.sendMessage(ChatFormatting.BOLD + hack.getName() + ChatFormatting.RESET + ChatFormatting.RED + ChatFormatting.BOLD + " DISABLED!");
                }
            }else if(hack.isEnabled() && (System.currentTimeMillis() - hack.lastEnabledTime) < 50) {
                if(messageMode.getMode().equals("Removable")) {
                    notorious.messageManager.sendRemovableMessage(ChatFormatting.BOLD + hack.getName() + ChatFormatting.RESET + ChatFormatting.GREEN + ChatFormatting.BOLD + " ENABLED!", 1);
                }else {
                    notorious.messageManager.sendMessage(ChatFormatting.BOLD + hack.getName() + ChatFormatting.RESET + ChatFormatting.GREEN + ChatFormatting.BOLD + " ENABLED!");
                }
            }
        }
    }
}
