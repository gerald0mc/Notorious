package me.gavin.notorious.hack.hacks.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

@RegisterHack(name = "CopyCoords", description = "ez", category = Hack.Category.Misc)
public class CopyCoords extends Hack {

    @Override
    protected void onEnable() {
        if (mc.player != null && mc.world != null) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(mc.player.getPosition().toString()), null);
            notorious.messageManager.sendMessage("Copied Coords " + ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + "X:" + mc.player.getPosition().getX() + " Y:" + mc.player.getPosition().getY() + " Z:" + mc.player.getPosition().getZ() + ChatFormatting.GRAY + "]" + ChatFormatting.RESET + " to clipboard");
        } else {
            notorious.messageManager.sendError("Unable to copy coords?");
        }
        disable();
    }
}
