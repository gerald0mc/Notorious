package me.gavin.notorious.hack;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterHack {

    String name();

    String description();

    Hack.Category category();

    int bind() default 0;
}
