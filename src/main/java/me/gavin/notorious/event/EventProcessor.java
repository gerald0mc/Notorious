package me.gavin.notorious.event;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.util.ProjectionUtil;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * @author Gav06
 * @since 6/15/2021
 */

public class EventProcessor {

    public EventProcessor() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == 0)
                return;

            for (Hack hack : Notorious.INSTANCE.hackManager.getHacks()) {
                if (hack.getBind() == Keyboard.getEventKey()) {
                    hack.toggle();
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        ProjectionUtil.updateMatrix();
    }
}