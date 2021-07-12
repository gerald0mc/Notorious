package me.gavin.notorious.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickTimer {

    private long ticksPassed = 0L;
    private long lastTicks = ticksPassed;

    public TickTimer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        ticksPassed++;
    }

    public boolean hasTicksPassed(long ticks) {
        return ticksPassed - lastTicks > ticks;
    }

    public void reset() {
        lastTicks = ticksPassed;
    }
}
