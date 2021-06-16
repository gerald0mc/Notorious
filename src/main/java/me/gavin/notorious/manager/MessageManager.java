package me.gavin.notorious.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.NotoriousMod;
import me.gavin.notorious.misc.IMinecraft;
import net.minecraft.util.text.TextComponentString;

public class MessageManager implements IMinecraft {

    public final String messagePrefix = ChatFormatting.BLUE + "<" + NotoriousMod.MOD_ID + "> " + ChatFormatting.RESET;
    public final String errorPrefix = ChatFormatting.DARK_RED + "<" + NotoriousMod.MOD_ID + "> " + ChatFormatting.RESET;

    public void sendRawMessage(String message) {
        if (mc.player != null) {
            mc.player.sendMessage(new TextComponentString(message));
        }
    }

    public void sendMessage(String message) {
        sendRawMessage(messagePrefix + message);
    }

    public void sendError(String message) {
        sendRawMessage(errorPrefix + message);
    }
}
