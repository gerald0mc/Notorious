package dev.notorious.client.event.impl;

import dev.notorious.client.event.Event;
import net.minecraft.network.Packet;

public class SendPacketEvent extends Event {
    private final Packet<?> packet;

    public SendPacketEvent(Packet<?> packet){
        super(Stage.PRE);
        this.packet = packet;
    }

    public Packet<?> getPacket(){
        return packet;
    }
}
