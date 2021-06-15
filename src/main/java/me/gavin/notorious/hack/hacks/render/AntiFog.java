package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import org.lwjgl.input.Keyboard;

// EntityRendererMixin.java

@RegisterHack(name = "AntiFog", description = "Prevents fog from rendering", category = Hack.Category.Render, bind = Keyboard.KEY_G)
public class AntiFog extends Hack {
}