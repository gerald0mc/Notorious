package me.gavin.notorious.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.event.events.TotemPopEvent;
import me.gavin.notorious.hack.hacks.chat.TotemPopCounter;
import me.gavin.notorious.hack.hacks.misc.FakePlayer;
import me.gavin.notorious.stuff.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class TotemPopListener implements IMinecraft {

    public final Map<String, Integer> popMap = new HashMap<>();
    private final Notorious notorious = Notorious.INSTANCE;

    public TotemPopListener() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 35) {
                final Entity entity = packet.getEntity(mc.world);
                if (entity instanceof EntityPlayer) {
                    if (entity.equals(mc.player))
                        return;

                    final EntityPlayer player = (EntityPlayer) entity;
                    handlePop(player);
                }
            }
        }
    }

    public void handlePop(EntityPlayer player) {
        if (!popMap.containsKey(player.getName())) {
            MinecraftForge.EVENT_BUS.post(new TotemPopEvent(player.getName(), 1, player.getEntityId()));
            popMap.put(player.getName(), 1);
        } else {
            popMap.put(player.getName(), popMap.get(player.getName()) + 1);
            MinecraftForge.EVENT_BUS.post(new TotemPopEvent(player.getName(), popMap.get(player.getName()), player.getEntityId()));
        }
    }

    @SubscribeEvent
    public void onTick(PlayerLivingUpdateEvent event) {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player == notorious.hackManager.getHack(FakePlayer.class).fakePlayer)
                continue;

            if (player != mc.player && popMap.containsKey(player.getName())) {
                if ((player.isDead || !player.isEntityAlive() || player.getHealth() <= 0)) {
                    notorious.hackManager.getHack(TotemPopCounter.class).onDeath(player.getName(), popMap.get(player.getName()), player.getEntityId());
                    popMap.remove(player.getName());
                }
            }
        }
    }
}