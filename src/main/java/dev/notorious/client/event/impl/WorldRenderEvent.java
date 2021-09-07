package dev.notorious.client.event.impl;

import dev.notorious.client.event.Event;

public class WorldRenderEvent extends Event {
    private final float partialTicks;

    public WorldRenderEvent(final float partialTicks){
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks(){
        return partialTicks;
    }
}
