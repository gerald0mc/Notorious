package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.TotemPopEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "TotemPopCounter", description = "Counts totem pops", category = Hack.Category.Chat)
public class TotemPopCounter extends Hack {

    public void onDeath(String name, int pops, int entId) {
        final String s = pops == 1 ? "" : "s";
        notorious.messageManager.sendRemovableMessage(ChatFormatting.RED + name + ChatFormatting.RESET + " has died after popping " + ChatFormatting.GREEN + pops + ChatFormatting.RESET + " totem" + s, entId);
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if (event.getPopCount() == 1) {
            notorious.messageManager.sendRemovableMessage(ChatFormatting.RED + event.getName() + ChatFormatting.RESET + " has popped a totem", event.getEntityId());
        } else {
            notorious.messageManager.sendRemovableMessage(ChatFormatting.RED + event.getName() + ChatFormatting.RESET + " has popped " + ChatFormatting.GREEN + event.getPopCount() + ChatFormatting.RESET + " totems", event.getEntityId());
        }
    }
}