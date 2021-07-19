package me.gavin.notorious.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.gavin.notorious.NotoriousMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class DiscordUtil {

    private static String discordID = "866094794517643265";
    private static DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    private static String ip;

    public static void startRPC() {
        final ServerData data = Minecraft.getMinecraft().getCurrentServerData();
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        if(Minecraft.getMinecraft().isIntegratedServerRunning()) {
            ip = "Singleplayer";
        }else {
            ip = Minecraft.getMinecraft().getCurrentServerData().serverIP;
        }
        eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));

        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.details = "Owning on " + ip;
        discordRichPresence.largeImageKey = "big_papa";
        discordRichPresence.largeImageText = "discord.gg/nPBPJRcuqP";
        discordRichPresence.smallImageKey = "jerking_off";
        discordRichPresence.smallImageText = "Busting a nut in your mom.";
        discordRichPresence.state = null;
        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}
