package me.gavin.notorious.event.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.UUID;

public class ConnectionEvent extends Event {

    private final UUID uuid;
    private final EntityPlayer entity;
    private final String name;

    public ConnectionEvent(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.entity = null;
    }

    public ConnectionEvent(EntityPlayer entity, UUID uuid, String name) {
        this.entity = entity;
        this.uuid = uuid;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public EntityPlayer getEntity() {
        return this.entity;
    }
}
