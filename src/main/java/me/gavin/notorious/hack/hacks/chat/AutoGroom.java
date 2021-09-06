package me.gavin.notorious.hack.hacks.chat;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.TickTimer;
import me.gavin.notorious.util.TimerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@RegisterHack(name = "AutoGroom", description = "SyndicateNA simulator", category = Hack.Category.Chat)
public class AutoGroom extends Hack {

    @RegisterSetting
    public final NumSetting delay = new NumSetting("Delay", 10, 1, 15, 1);

    private List<String> peopleInArea;
    private List<String> peopleNearbyNew;
    private List<String> peopleToRemove;
    private TickTimer timer = new TickTimer();

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
                    if (timer.hasTicksPassed((long) (delay.getValue() * 20))) {
                        mc.player.sendChatMessage("/msg " + name + " hewwo wittle kitten, come be my wittle discord girl? | discord.gg/BU9g9HgGBa");
                        timer.reset();
                    }
                    peopleInArea.add(name);
                }
            }
        }
        if(peopleInArea.size() > 0) {
            for(String name : peopleInArea) {
                if(!peopleNearbyNew.contains(name)) {
                    peopleToRemove.add(name);
                    if (timer.hasTicksPassed((long) (delay.getValue() * 20))) {
                        mc.player.sendChatMessage("/msg " + name + " ow no pwease dont weave me my wittle kitten UWU");
                        timer.reset();
                    }
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
