package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@RegisterHack(name = "TotemPopCounter", description = "Counts totem pops", category = Hack.Category.Chat)
public class TotemPopCounter extends Hack {

    private final Map<String, Integer> popMap = new HashMap<>();

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

    private void handlePop(EntityPlayer player) {
        if (!popMap.containsKey(player.getName())) {
            notorious.messageManager.sendRemovableMessage(player.getName() + " has popped a totem", player.getEntityId());
            popMap.put(player.getName(), 1);
        } else {
            popMap.put(player.getName(), popMap.get(player.getName()) + 1);
            notorious.messageManager.sendRemovableMessage(player.getName() + " has popped " + ChatFormatting.GREEN + popMap.get(player.getName()) + ChatFormatting.RESET + " totems", player.getEntityId());
        }
    }

    @SubscribeEvent
    public void onTick(PlayerLivingUpdateEvent event) {
        for (EntityPlayer player : mc.world.playerEntities) {
            if ((player.isDead || !player.isEntityAlive() || player.getHealth() <= 0) && popMap.containsKey(player.getName())) {
                final String s = popMap.get(player.getName()) == 1 ? "" : "s";
                notorious.messageManager.sendRemovableMessage(player.getName() + " has died after popping " + ChatFormatting.GREEN + popMap.get(player.getName()) + ChatFormatting.RESET + " totem" + s, player.getEntityId());
                popMap.remove(player.getName());
            }
        }
    }
}