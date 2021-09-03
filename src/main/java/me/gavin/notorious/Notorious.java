package me.gavin.notorious;

import me.gavin.notorious.event.EventProcessor;
import me.gavin.notorious.friend.Friends;
import me.gavin.notorious.gui.ClickGuiScreen;
import me.gavin.notorious.manager.*;
import me.gavin.notorious.util.TotemPopListener;
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
    public final TotemPopListener popListener;
    public final Friends friend;
    public final NotificationManager notificationManager;
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
        notificationManager = new NotificationManager();
        configManager = new ConfigManager();

        new EventProcessor();

        MinecraftForge.EVENT_BUS.register(this);

        configManager.load();
        configManager.attach();
    }
}