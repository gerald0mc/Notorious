package dev.notorious.client.event.impl;

import dev.notorious.client.event.Event;
import net.minecraft.entity.player.EntityPlayer;

public class DeathEvent extends Event {
    private final EntityPlayer entity;

    public DeathEvent(EntityPlayer entity){
        this.entity = entity;
    }

    public EntityPlayer getEntity(){
        return entity;
    }
}
