package me.gavin.notorious;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

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
    public static final String VERSION = "1.0";
    public static final String NAME_VERSION = MOD_NAME + " " + VERSION;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new Notorious();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Notorious.INSTANCE.hackManager.getSortedHacks().sort(Comparator.comparing(hack -> -Notorious.INSTANCE.fontRenderer.getStringWidth(hack.getName())));
    }
}