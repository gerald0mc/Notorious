package me.gavin.notorious.notifications;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.stuff.IMinecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class Notification implements IMinecraft {

	public String title;
	public String message;
	public NotificationType type;

	public long start;

	public Notification(String title, String message, NotificationType type) {
		this.title = title;
		this.message = message;
		this.type = type;

	}

	public void show() {
		start = System.currentTimeMillis();
	}

	public void draw() {
		int width = mc.fontRenderer.getStringWidth(message) + 2;

		ScaledResolution sr = new ScaledResolution(mc);

		if(Notorious.INSTANCE.hackManager.getHack(me.gavin.notorious.hack.hacks.client.Notification.class).style.getMode().equals("Skeet")) {
			Gui.drawRect(sr.getScaledWidth() - width, sr.getScaledHeight() - 40, sr.getScaledWidth(), sr.getScaledHeight() - 5, new Color(0, 0, 0, 255).getRGB());
			Gui.drawRect(sr.getScaledWidth() - width, sr.getScaledHeight() - 40, sr.getScaledWidth() - width + 5, sr.getScaledHeight() - 5, type.color.getRGB());
		}else {
			Gui.drawRect(sr.getScaledWidth() - width, sr.getScaledHeight() - 20, sr.getScaledWidth(), sr.getScaledHeight() - 5, 0x7F000000);
		}

		if(Notorious.INSTANCE.hackManager.getHack(me.gavin.notorious.hack.hacks.client.Notification.class).style.getMode().equals("Skeet")) {
			mc.fontRenderer.drawString(title, sr.getScaledWidth() - width + 8, sr.getScaledHeight() - 2 - 35, -1);
			mc.fontRenderer.drawString(message, sr.getScaledWidth() - width + 8, sr.getScaledHeight() - 15, -1);
		}else {
			mc.fontRenderer.drawString(message, sr.getScaledWidth() - width + 4, sr.getScaledHeight() - 15, -1);
		}
	}

	public long timeLeft() {
		return System.currentTimeMillis() - start;
	}

	public boolean expired() {
		return timeLeft() > 2000;
	}

}
