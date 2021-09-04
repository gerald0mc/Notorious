package me.gavin.notorious;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import java.util.Comparator;

/**
 * @author Gav06
 * @since 6/15/2021
 */

@Mod(
        modid = NotoriousMod.MOD_ID,
        name = NotoriousMod.MOD_NAME,
        version = NotoriousMod.VERSION,
        clientSideOnly = true
)
public class NotoriousMod {

    public static final String MOD_ID = "notorious";
    public static final String MOD_NAME = "Notorious";
    public static final String VERSION = "beta-0.6";
    public static final String NAME_VERSION = MOD_NAME + " " + VERSION;
    public static final String HWID_URL = "https://pastebin.com/raw/twrT83RF";

    public static final Logger LOGGER = LogManager.getLogger("Notorious");

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new Notorious();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        Display.setTitle(NotoriousMod.NAME_VERSION);
    }

    @SubscribeEvent
    public void onTick(PlayerLivingUpdateEvent event) {
        Notorious.INSTANCE.hackManager.getSortedHacks().sort(Comparator.comparing(hack -> -Notorious.INSTANCE.fontRenderer.getStringWidth(hack.getName() + hack.getMetaData())));
    }
}
