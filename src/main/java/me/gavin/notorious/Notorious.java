package me.gavin.notorious;

import me.gavin.notorious.event.EventProcessor;
import me.gavin.notorious.gui.ClickGuiScreen;
import me.gavin.notorious.manager.HackManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;

/**
 * @author Gav06
 * @since 6/15/2021
 */

public class Notorious {

    public static Notorious INSTANCE;

    public final HackManager HACK_MANAGER;
    public final ClickGuiScreen CLICK_GUI;

    public Notorious() {
        HACK_MANAGER = new HackManager();
        CLICK_GUI = new ClickGuiScreen();

        new EventProcessor();

        MinecraftForge.EVENT_BUS.register(this);
        Display.setTitle(NotoriousMod.NAME_VERSION);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        final String s = NotoriousMod.NAME_VERSION;
    }
}