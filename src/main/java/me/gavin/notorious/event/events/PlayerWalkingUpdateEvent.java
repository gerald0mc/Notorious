package me.gavin.notorious.event.events;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Gav06
 *
 * @since 6/16/2021
 */

public class PlayerWalkingUpdateEvent extends Event {

    public static class Pre extends PlayerWalkingUpdateEvent { }

    public static class Post extends PlayerWalkingUpdateEvent { }
}
