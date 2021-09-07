package dev.notorious.client.event;

import dev.notorious.client.event.impl.DeathEvent;
import dev.notorious.client.util.IMinecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class EventManager implements IMinecraft {
    public EventManager(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if (mc.player == null || mc.world == null) return;

        for (EntityPlayer player : new ArrayList<>(mc.world.playerEntities)){
            if (player.getHealth() <= 0){
                MinecraftForge.EVENT_BUS.post(new DeathEvent(player));
            }
        }
    }
}