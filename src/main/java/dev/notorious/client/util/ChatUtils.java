package dev.notorious.client.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;

public class ChatUtils implements IMinecraft {
    public static void sendChatMessage(final String message){
        if (mc.player == null || mc.world == null || mc.ingameGUI == null) return;

        try {
            TextComponentString component = new TextComponentString((true ? ChatFormatting.DARK_AQUA + "<" + ChatFormatting.AQUA + "Notorious" + ChatFormatting.DARK_AQUA + "> " : "") + ChatFormatting.AQUA + message);
            mc.ingameGUI.getChatGUI().printChatMessage(component);
        } catch (IndexOutOfBoundsException | NullPointerException ignored) {}
    }

    public static void sendChatMessage(final String message, final boolean watermark){
        if (mc.player == null || mc.world == null || mc.ingameGUI == null) return;

        try {
            TextComponentString component = new TextComponentString((watermark ? ChatFormatting.DARK_AQUA + "<" + ChatFormatting.AQUA + "Notorious" + ChatFormatting.DARK_AQUA + "> " : "") + ChatFormatting.AQUA + message);
            mc.ingameGUI.getChatGUI().printChatMessage(component);
        } catch (IndexOutOfBoundsException | NullPointerException ignored) {}
    }

    public static void sendChatMessage(final String message, final int id){
        if (mc.player == null || mc.world == null || mc.ingameGUI == null) return;

        try {
            TextComponentString component = new TextComponentString((true ? ChatFormatting.DARK_AQUA + "<" + ChatFormatting.AQUA + "Notorious" + ChatFormatting.DARK_AQUA + "> " : "") + ChatFormatting.AQUA + message);
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, id);
        } catch (IndexOutOfBoundsException | NullPointerException ignored) {}
    }

    public static void sendChatMessage(final String message, final boolean watermark, final int id){
        if (mc.player == null || mc.world == null || mc.ingameGUI == null) return;

        try {
            TextComponentString component = new TextComponentString((watermark ? ChatFormatting.DARK_AQUA + "<" + ChatFormatting.AQUA + "Notorious" + ChatFormatting.DARK_AQUA + "> " : "") + ChatFormatting.AQUA + message);
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, id);
        } catch (IndexOutOfBoundsException | NullPointerException ignored) {}
    }

    public static void sendRawMessage(final String message){
        if (mc.player == null || mc.world == null || mc.ingameGUI == null) return;
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(message));
    }

    public static void sendPlayerMessage(final String message){
        if (mc.player == null || mc.world == null) return;
        mc.player.connection.sendPacket(new CPacketChatMessage(message));
    }
}