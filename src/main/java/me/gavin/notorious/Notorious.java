package me.gavin.notorious;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.EventProcessor;
import me.gavin.notorious.gui.ClickGuiScreen;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.manager.HackManager;
import me.gavin.notorious.manager.MessageManager;
import me.gavin.notorious.util.font.CFontLoader;
import me.gavin.notorious.util.font.CFontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;

import java.awt.*;

/**
 * @author Gav06
 * @since 6/15/2021
 */

public class Notorious {

    public static Notorious INSTANCE;

    public final HackManager hackManager;
    public final ClickGuiScreen clickGui;
    public final CFontRenderer fontRenderer;
    public final MessageManager messageManager;

    public Notorious() {
        INSTANCE = this;

        hackManager = new HackManager();
        fontRenderer = new CFontRenderer(CFontLoader.HELVETICA, true, true);
        clickGui = new ClickGuiScreen();
        messageManager = new MessageManager();

        new EventProcessor();

        MinecraftForge.EVENT_BUS.register(this);
        Display.setTitle(NotoriousMod.NAME_VERSION);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        int yOffset = 2;
        for (Hack hack : hackManager.getHacks()) {
            ChatFormatting color = hack.isEnabled() ? ChatFormatting.GREEN : ChatFormatting.RED;
            fontRenderer.drawStringWithShadow(color + hack.getName(), 2.0, yOffset, new Color(-1));
            yOffset += fontRenderer.getHeight() + 3;
        }
    }
}