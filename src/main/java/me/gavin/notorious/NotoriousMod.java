package me.gavin.notorious;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.gavin.notorious.util.auth.*;

import java.io.IOException;
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
    public static final String VERSION = "beta-0.5.5";
    public static final String NAME_VERSION = MOD_NAME + " " + VERSION;
    public static final String HWID_URL = ""; //pastebin url goes here

    public static List<String> hwidList = new ArrayList<>();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new Notorious();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        try {
            Notorious.INSTANCE.configManager.load();
        } catch (IOException e) {
            e.printStackTrace();;
        }
            
        this.Verify();
    }
        
    public void Verify(){
        
        hwidList = NetworkUtil.getHWIDList();

        
        if(!hwidList.contains(HWIDUtil.getEncryptedHWID())){
            
            FrameUtil.Display();
            throw new NoStackTraceThrowable("Verify HWID Failed!");
        }

    }

    @SubscribeEvent
    public void onTick(PlayerLivingUpdateEvent event) {
        Notorious.INSTANCE.hackManager.getSortedHacks().sort(Comparator.comparing(hack -> -Notorious.INSTANCE.fontRenderer.getStringWidth(hack.getName() + hack.getMetaData())));
    }
}
