package me.gavin.notorious.hack.hacks.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.notifications.NotificationType;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ModeSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "Notifications", description = "ez", category = Hack.Category.Client)
public class Notification extends Hack {

    @RegisterSetting
    public final ModeSetting style = new ModeSetting("Style", "Skeet", "Skeet", "Basic");
    @RegisterSetting
    public final BooleanSetting moduleToggle = new BooleanSetting("ModuleToggle", true);

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        for (Hack hack : notorious.hackManager.getHacks()) {
            if(moduleToggle.isEnabled()) {
                if(!hack.isEnabled() && (System.currentTimeMillis() - hack.lastDisabledTime) < 250) {
                    notorious.notificationManager.show(new me.gavin.notorious.notifications.Notification("ToggleMessage", ChatFormatting.BOLD + hack.getName() + ChatFormatting.RESET + " has been " + ChatFormatting.RED + "DISABLED!", NotificationType.INFO));
                }else if(hack.isEnabled() && (System.currentTimeMillis() - hack.lastEnabledTime) < 250) {
                    notorious.notificationManager.show(new me.gavin.notorious.notifications.Notification("ToggleMessage", ChatFormatting.BOLD + hack.getName() + ChatFormatting.RESET + " has been " + ChatFormatting.GREEN + "ENABLED!", NotificationType.INFO));
                }
            }
        }
    }
}
