package me.gavin.notorious.hack.hacks.client;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@RegisterHack(name = "ArrayList", description = "Shows enabled modules.", category = Hack.Category.Client)
public class ArrayList extends Hack {

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Color color = new Color(255, 255, 255, 255);
            int currY = notorious.fontRenderer.getHeight() + 2;
            int count = 0;

            for(Hack m : notorious.hackManager.getHacks()) {
                if(m.isEnabled()) {
                    notorious.fontRenderer.drawStringWithShadow(m.getName(), 4, currY + 10, color);
                    currY += mc.fontRenderer.FONT_HEIGHT;
                    count++;
                }
            }
        }
    }
}
