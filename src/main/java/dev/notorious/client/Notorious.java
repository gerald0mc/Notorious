package dev.notorious.client;

import dev.notorious.client.event.EventManager;
import dev.notorious.client.managers.CommandManager;
import dev.notorious.client.managers.HackManager;
import dev.notorious.client.managers.PlayerManager;
import dev.notorious.client.managers.friend.FriendManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(name = Notorious.NAME, modid = Notorious.MOD_ID, version = Notorious.VERSION)
public class Notorious {
    public static final String NAME = "Notorious";
    public static final String MOD_ID = "notorious";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MOD_ID)
    public static Notorious INSTANCE;

    public static final Logger LOGGER = LogManager.getLogger("Notorious");

    public static HackManager HACK_MANAGER;
    public static CommandManager COMMAND_MANAGER;
    public static FriendManager FRIEND_MANAGER;
    public static PlayerManager PLAYER_MANAGER;
    public static EventManager EVENT_MANAGER;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        LOGGER.info("Started Initialization process for Notorious v" + VERSION + "!");

        HACK_MANAGER = new HackManager();
        COMMAND_MANAGER = new CommandManager();
        FRIEND_MANAGER = new FriendManager();
        PLAYER_MANAGER = new PlayerManager();
        EVENT_MANAGER = new EventManager();

        LOGGER.info("Finished Initialization process for Notorious v" + VERSION + "!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        LOGGER.info("Started Post-Initialization process for Notorious v" + VERSION + "!");

        Display.setTitle("Notorious - " + VERSION);

        LOGGER.info("Finished Post-Initialization process for Notorious v" + VERSION + "!");
    }
}
