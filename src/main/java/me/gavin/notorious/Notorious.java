package me.gavin.notorious;

import me.gavin.notorious.event.EventProcessor;
import me.gavin.notorious.friend.Friends;
import me.gavin.notorious.gui.ClickGuiScreen;
import me.gavin.notorious.manager.ConfigManager;
import me.gavin.notorious.manager.HackManager;
import me.gavin.notorious.manager.MessageManager;
import me.gavin.notorious.manager.RotationManager;
import me.gavin.notorious.util.TotemPopListener;
import me.gavin.notorious.util.font.CFontLoader;
import me.gavin.notorious.util.font.CFontRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;

import java.io.IOException;

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
    public final TotemPopListener popListener;
    public final Friends friend;
    public final ConfigManager configManager;

    public Notorious() {
        INSTANCE = this;

        hackManager = new HackManager();
        fontRenderer = new CFontRenderer(CFontLoader.HELVETICA, true, true);
        clickGuiScreen = new ClickGuiScreen();
        messageManager = new MessageManager();
        rotationManager = new RotationManager();
        popListener = new TotemPopListener();
        friend = new Friends();
        configManager = new ConfigManager();

        new EventProcessor();

        MinecraftForge.EVENT_BUS.register(this);
        Display.setTitle(NotoriousMod.NAME_VERSION);

        Runtime.getRuntime().addShutdownHook(new ShutDownHook());
    }
}