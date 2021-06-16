package me.gavin.notorious.event.events;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Gav06
 *
 * @since 6/16/2021
 */

public class PlayerWalkingUpdateEvent extends Event {

    private final Stage stage;

    public PlayerWalkingUpdateEvent(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public enum Stage {
        PRE,
        POST
    }
}
