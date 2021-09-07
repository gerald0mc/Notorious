package dev.notorious.client.event.impl;

import dev.notorious.client.event.Event;
import net.minecraft.network.Packet;

public class ReceivePacketEvent extends Event {
    private final Packet<?> packet;

    public ReceivePacketEvent(Packet<?> packet){
        super(Stage.PRE);
        this.packet = packet;
    }

    public Packet<?> getPacket(){
        return packet;
    }
}
