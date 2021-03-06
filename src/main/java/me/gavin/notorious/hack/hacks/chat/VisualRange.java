package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@RegisterHack(name = "VisualRange", description = "Sends a message in chat when a player enters your range.", category = Hack.Category.Chat)
public class VisualRange extends Hack {

    private List<String> peopleInArea;
    private List<String> peopleNearbyNew;
    private List<String> peopleToRemove;

    @Override
    public void onEnable() {
        peopleInArea = new ArrayList<>();
        peopleToRemove = new ArrayList<>();
    }

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        peopleNearbyNew = new ArrayList<>();
        List<EntityPlayer> playerEntities = mc.world.playerEntities;
        for(Entity e : playerEntities) {
            if(e.getName().equals(mc.player.getName())) continue;
            peopleNearbyNew.add(e.getName());
        }
        if(peopleNearbyNew.size() > 0) {
            for(String name : peopleNearbyNew) {
                if(!peopleInArea.contains(name)) {
                    notorious.messageManager.sendMessage(ChatFormatting.RED + name + ChatFormatting.RESET + " has entered your Visual Range");
                    peopleInArea.add(name);
                }
            }
        }
        if(peopleInArea.size() > 0) {
            for(String name : peopleInArea) {
                if(!peopleNearbyNew.contains(name)) {
                    peopleToRemove.add(name);
                    notorious.messageManager.sendMessage(ChatFormatting.RED + name + ChatFormatting.RESET + " has left your Visual Range");
                }
            }
            if(peopleToRemove.size() > 0) {
                for(String name : peopleToRemove) {
                    peopleInArea.remove(name);
                }
                peopleToRemove.clear();
            }
        }
    }
}