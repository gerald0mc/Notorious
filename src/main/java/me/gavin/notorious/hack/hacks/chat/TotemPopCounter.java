package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.event.events.TotemPopEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.notifications.Notification;
import me.gavin.notorious.notifications.NotificationType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "TotemPopCounter", description = "Counts totem pops", category = Hack.Category.Chat)
public class TotemPopCounter extends Hack {

	public void onDeath(String name, int pops, int entId) {
		final String s = pops == 1 ? "" : "s";
		notorious.messageManager.sendRemovableMessage(ChatFormatting.RED + name + ChatFormatting.RESET + " has died after popping " + ChatFormatting.GREEN + pops + ChatFormatting.RESET + " totem" + s, entId);
		if(Notorious.INSTANCE.hackManager.getHack(me.gavin.notorious.hack.hacks.client.Notification.class).isEnabled()) {
			notorious.notificationManager.show(new Notification("Totem Pop", String.format("%s has died after popping %s...", name, pops), NotificationType.INFO));
		}
	}

	@SubscribeEvent
	public void onPop(TotemPopEvent event) {
		if (event.getPopCount() == 1) {
			notorious.messageManager.sendRemovableMessage(ChatFormatting.RED + event.getName() + ChatFormatting.RESET + " has popped a totem", event.getEntityId());
			if(Notorious.INSTANCE.hackManager.getHack(me.gavin.notorious.hack.hacks.client.Notification.class).isEnabled()) {
				notorious.notificationManager.show(new Notification("Totem Pop", String.format("%s has popped a totem...", event.getName(), event.getPopCount()), NotificationType.INFO));
			}
		} else {
			notorious.messageManager.sendRemovableMessage(ChatFormatting.RED + event.getName() + ChatFormatting.RESET + " has popped " + ChatFormatting.GREEN + event.getPopCount() + ChatFormatting.RESET + " totems", event.getEntityId());
			if(Notorious.INSTANCE.hackManager.getHack(me.gavin.notorious.hack.hacks.client.Notification.class).isEnabled()) {
				notorious.notificationManager.show(new Notification("Totem Pop", String.format("%s has popped %s totems...", event.getName(), event.getPopCount()), NotificationType.INFO));
			}
		}
	}
}