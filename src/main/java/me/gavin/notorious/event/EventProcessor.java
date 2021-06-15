package me.gavin.notorious.event;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.Hack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class EventProcessor {

    public EventProcessor() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_RSHIFT)
                Minecraft.getMinecraft().displayGuiScreen(Notorious.INSTANCE.CLICK_GUI);

            for (Hack hack : Notorious.INSTANCE.HACK_MANAGER.getHacks()) {
                if (hack.getKeybind() == Keyboard.getEventKey()) {
                    hack.toggle();
                }
            }
        }
    }
}
