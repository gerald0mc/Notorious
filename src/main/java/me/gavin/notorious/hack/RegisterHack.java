package me.gavin.notorious.hack;

import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Gav06
 * @since 6/15/2021
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterHack {

    String name();

    String description();

    Hack.Category category();
}