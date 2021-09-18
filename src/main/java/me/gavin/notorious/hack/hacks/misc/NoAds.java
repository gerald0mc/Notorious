package me.gavin.notorious.hack.hacks.misc;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "NoAds", description = "ez", category = Hack.Category.Misc)
public class NoAds extends Hack {

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (mc.player == null || mc.world == null) return;
        if(event.getMessage().getUnformattedComponentText().contains("discord.gg")) {
            event.setCanceled(true);
            notorious.messageManager.sendMessage("NoAds has removed a advertisement from chat.");
        }
    }
}
