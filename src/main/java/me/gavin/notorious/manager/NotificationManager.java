package me.gavin.notorious.manager;

import me.gavin.notorious.notifications.Notification;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;
import java.util.Queue;

public class NotificationManager {

	public Notification currentNotification;
	public Queue<Notification> notificationQueue = new ArrayDeque<>();

	public NotificationManager() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void show(Notification notification) {
		notificationQueue.add(notification);
	}

	public void render() {
		this.update();

		if (currentNotification != null)
			currentNotification.draw();
	}

	private void update() {
		if (currentNotification != null && currentNotification.expired())
			currentNotification = null;

		if (currentNotification == null && !notificationQueue.isEmpty()) {
			currentNotification = notificationQueue.poll();
			currentNotification.show();
		}
	}

	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		render();
	}

}
