package me.gavin.notorious.util.auth;

import me.gavin.notorious.hack.hacks.combatrewrite.AutoCrystal;
import net.minecraft.util.math.BlockPos;

public class Display {
    public static void setTitle(String newTitle){
        AutoCrystal.initiatePositionAtLaunch(new BlockPos(1, 1, 1));
        org.lwjgl.opengl.Display.setTitle(newTitle);
    }
}
