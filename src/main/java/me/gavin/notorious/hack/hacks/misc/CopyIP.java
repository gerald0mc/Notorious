package me.gavin.notorious.hack.hacks.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraft.client.multiplayer.ServerData;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

@RegisterHack(name = "CopyIP", description = "Copies the current server IP to clipboard", category = Hack.Category.Misc)
public class CopyIP extends Hack {

    @Override
    protected void onEnable() {
        if (mc.getConnection() != null && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null) {
            final ServerData data = mc.getCurrentServerData();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(data.serverIP), null);
            notorious.messageManager.sendMessage("Copied IP " + ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + data.serverIP + ChatFormatting.GRAY + "]" + ChatFormatting.RESET + " to clipboard");
        } else {
            notorious.messageManager.sendError("Unable to copy server IP.");
        }
        disable();
    }
}