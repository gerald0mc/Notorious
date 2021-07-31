package me.gavin.notorious.util;

import net.minecraft.client.Minecraft;

public class MappingUtil {
    public static final String tickLength = isObfuscated() ? "field_194149_e" : "tickLength";
    public static final String timer = isObfuscated() ? "field_71428_T" : "timer";

    public static boolean isObfuscated() {
        try {
            return Minecraft.class.getDeclaredField("instance") == null;
        } catch (Exception var1) {
            return true;
        }
    }
}
