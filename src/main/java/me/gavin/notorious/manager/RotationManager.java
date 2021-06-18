package me.gavin.notorious.manager;

import me.gavin.notorious.event.events.PlayerWalkingUpdateEvent;
import me.gavin.notorious.stuff.IMinecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RotationManager implements IMinecraft {

    private float previousYaw;
    private float previousPitch;

    public float desiredYaw;
    public float desiredPitch;

    public RotationManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTickPre(PlayerWalkingUpdateEvent.Pre event) {
        previousYaw = mc.player.rotationYaw;
        previousPitch = mc.player.rotationPitch;
        mc.player.rotationYaw = desiredYaw;
        mc.player.rotationPitch = desiredPitch;
    }

    @SubscribeEvent
    public void onTickPost(PlayerWalkingUpdateEvent.Post event) {
        mc.player.rotationPitch = previousPitch;
        mc.player.rotationYaw = previousYaw;
        desiredYaw = mc.player.rotationYaw;
        desiredPitch = mc.player.rotationPitch;
    }
}
