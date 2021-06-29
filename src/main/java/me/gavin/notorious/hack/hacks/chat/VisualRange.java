package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.HashSet;
import java.util.Set;

@RegisterHack(name = "VisualRange", description = "Sends a message in chat when a player enters your range.", category = Hack.Category.Chat)
public class VisualRange extends Hack {

    private Set<Entity> players = new HashSet<Entity>();

    @Override
    public void onEnable() {
        players.clear();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        for(Entity e : mc.world.loadedEntityList){
            if(!(e instanceof EntityPlayer) || players.contains(e))
                continue;
            notorious.messageManager.sendMessage(ChatFormatting.RED + e.getName() + ChatFormatting.RESET + " has entered your range at X: " + ChatFormatting.RED + ChatFormatting.BOLD + e.getPosition().getX() + ChatFormatting.RESET + " Y: " + ChatFormatting.RED + ChatFormatting.BOLD +  e.getPosition().getY() + ChatFormatting.RESET + " Z: " + ChatFormatting.RED + ChatFormatting.BOLD + e.getPosition().getZ() + ChatFormatting.RESET + "!");
            players.add(e);
        }
    }
}