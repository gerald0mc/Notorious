package me.gavin.notorious.event.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class RenderEntityEvent extends Event {

    private final Entity entity;
    private final double x, y, z;

    private RenderEntityEvent(Entity entity, double x, double y, double z) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Entity getEntity() {
        return entity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public static class Pre extends RenderEntityEvent {

        public Pre(Entity entity, double x, double y, double z) {
            super(entity, x, y, z);
        }
    }

    public static class Post extends RenderEntityEvent {

        public Post(Entity entity, double x, double y, double z) {
            super(entity, x, y, z);
        }
    }
}