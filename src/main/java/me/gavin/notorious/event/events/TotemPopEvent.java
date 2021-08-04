package me.gavin.notorious.event.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class TotemPopEvent extends Event {

    private final String name;
    private final int popCount;
    private final int entId;

    public TotemPopEvent(String name, int count, int entId) {
        this.name = name;
        this.popCount = count;
        this.entId = entId;
    }

    public String getName() {
        return name;
    }

    public int getPopCount() {
        return popCount;
    }

    public int getEntityId() {
        return entId;
    }
}
