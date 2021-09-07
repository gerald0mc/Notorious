package dev.notorious.client.event;

public class Event extends net.minecraftforge.fml.common.eventhandler.Event {
    private boolean cancelled;
    private final Stage stage;

    public Event(){
        this.stage = Stage.PRE;
    }

    public Event(final Stage stage){
        this.stage = stage;
    }

    public Stage getStage(){
        return stage;
    }

    public boolean isCancelled(){
        return cancelled;
    }

    public void setCancelled(boolean cancelled){
        this.cancelled = cancelled;
    }

    public boolean isPre(){
        return stage == Stage.PRE;
    }

    public boolean isPost(){
        return stage == Stage.POST;
    }

    public enum Stage{
        PRE,
        POST
    }
}
