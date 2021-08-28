package me.gavin.notorious.notifications;

import java.awt.*;

public enum NotificationType {
	INFO(new Color(0xff6767ff)),
	WARNING(new Color(0xffffbd67)),
	ERROR(new Color(0xffff6767));

	Color color;

	NotificationType(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
