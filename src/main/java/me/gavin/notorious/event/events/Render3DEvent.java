package me.gavin.notorious.event.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class Render3DEvent extends Event {
    private final float partialTicks;

    public Render3DEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
