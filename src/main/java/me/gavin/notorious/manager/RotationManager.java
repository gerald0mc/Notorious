package me.gavin.notorious.manager;

import me.gavin.notorious.event.events.PlayerModelRotationEvent;
import me.gavin.notorious.event.events.PlayerWalkingUpdateEvent;
import me.gavin.notorious.stuff.IMinecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RotationManager implements IMinecraft {

    public RotationManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private boolean shouldRotate;

    private float oldPitch;
    private float oldYaw;

    private float desiredPitch;
    private float desiredYaw;

    @SubscribeEvent
    public void onUpdate(PlayerWalkingUpdateEvent event) {
        if (!shouldRotate)
            return;

        if (event.getStage() == PlayerWalkingUpdateEvent.Stage.PRE) {
            oldPitch = mc.player.rotationPitch;
            oldYaw = mc.player.rotationYaw;

            mc.player.rotationPitch = desiredPitch;
            mc.player.rotationYaw = desiredYaw;
        } else {
            mc.player.rotationPitch = oldPitch;
            mc.player.rotationYaw = oldYaw;
        }
    }

//    @SubscribeEvent
//    public void onModelRotate(PlayerModelRotationEvent event) {
//        if (!shouldRotate)
//            return;
//
//        event.setPitch(desiredPitch);
//        event.setYaw(desiredYaw);
//    }

    public void setRotation(float yaw, float pitch) {
        this.desiredYaw = yaw;
        this.desiredPitch = pitch;
    }

    public void setShouldRotate(boolean rotate) {
        shouldRotate = rotate;
    }

    public boolean shouldRotate() {
        return shouldRotate;
    }
}
