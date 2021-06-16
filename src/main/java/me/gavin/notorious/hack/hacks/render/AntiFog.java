package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import org.lwjgl.input.Keyboard;

/**
 * @author Gav06
 * @since 6/15/2021
 */

// EntityRendererMixin.java

@RegisterHack(name = "AntiFog", description = "Prevents fog from rendering", category = Hack.Category.Render, bind = Keyboard.KEY_G)
public class AntiFog extends Hack {
}