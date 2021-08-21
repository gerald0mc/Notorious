package me.gavin.notorious.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiscordUtil {

    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        String ip;
        if(Minecraft.getMinecraft().isIntegratedServerRunning()) {
            ip = "Singleplayer";
        }else {
            ip = Objects.requireNonNull(Minecraft.getMinecraft().getCurrentServerData()).serverIP;
        }
        eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));

        String discordID = "866094794517643265";
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
