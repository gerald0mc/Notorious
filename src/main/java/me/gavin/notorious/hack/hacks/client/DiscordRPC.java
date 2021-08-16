package me.gavin.notorious.hack.hacks.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.util.DiscordUtil;

@RegisterHack(name = "DiscordRPC", description = "ez", category = Hack.Category.Client)
public class DiscordRPC extends Hack {

    @Override
    public void onEnable() {
        if(mc.world != null && mc.player != null) {
            DiscordUtil.startRPC();
            notorious.messageManager.sendMessage("Starting " + ChatFormatting.GREEN + ChatFormatting.BOLD + "RPC" + ChatFormatting.RESET + "!");
        }else {
            toggle();
        }
    }

    @Override
    public void onDisable() {
        if(mc.world != null && mc.player != null) {
            DiscordUtil.stopRPC();
            notorious.messageManager.sendMessage("Stopping " + ChatFormatting.RED + ChatFormatting.BOLD + "RPC" + ChatFormatting.RESET + "!");
        }
    }
}
