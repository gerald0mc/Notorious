package dev.notorious.client.event.impl;

import dev.notorious.client.event.Event;

public class TextRenderEvent extends Event {
    private final float partialTicks;

    public TextRenderEvent(final float partialTicks){
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks(){
        return partialTicks;
    }
}
