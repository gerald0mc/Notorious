package dev.notorious.client.hacks;

import org.lwjgl.input.Keyboard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterHack {
    String name();
    String displayName();
    Hack.Category category();

    String description() default "No description.";
    boolean persistent() default false;
    boolean hidden() default false;
    int bind() default Keyboard.KEY_NONE;
}
