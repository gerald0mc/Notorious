package me.gavin.notorious;

import me.gavin.notorious.event.EventProcessor;
import me.gavin.notorious.gui.ClickGuiScreen;
import me.gavin.notorious.hack.hacks.client.ClickGUI;
import me.gavin.notorious.manager.HackManager;
import me.gavin.notorious.manager.MessageManager;
import me.gavin.notorious.manager.RotationManager;
import me.gavin.notorious.util.font.CFontLoader;
import me.gavin.notorious.util.font.CFontRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;

/**
 * @author Gav06
 * @since 6/15/2021
 */

public class Notorious {

    public static Notorious INSTANCE;

    public final HackManager hackManager;
    public final ClickGuiScreen clickGuiScreen;
    public final CFontRenderer fontRenderer;
    public final MessageManager messageManager;
    public final RotationManager rotationManager;

    public Notorious() {
        INSTANCE = this;

        hackManager = new HackManager();
        fontRenderer = new CFontRenderer(CFontLoader.HELVETICA, true, true);
        clickGuiScreen = new ClickGuiScreen();
        messageManager = new MessageManager();
        rotationManager = new RotationManager();

        new EventProcessor();

        MinecraftForge.EVENT_BUS.register(this);
        Display.setTitle(NotoriousMod.NAME_VERSION);
    }
}