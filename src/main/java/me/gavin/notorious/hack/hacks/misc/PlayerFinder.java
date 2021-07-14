package me.gavin.notorious.hack.hacks.misc;

import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@RegisterHack(name = "PlayerFinder", description = "ez", category = Hack.Category.Misc)
public class PlayerFinder extends Hack {

    @RegisterSetting
    public final NumSetting amountPerTick = new NumSetting("AmountPerTick", 2, 0, 5, 1);

    @SubscribeEvent
    public void onUpdate(TickEvent event) {
        if (mc.player.inPortal && mc.player.getRidingEntity() instanceof EntityBoat) {
            if (mc.player.inventory.getCurrentItem().getItem().equals(Items.MAP))
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(mc.player.getPosition(), EnumFacing.UP, EnumHand.MAIN_HAND, 0, -1337.77f, 0));
            for (int i = 0; i < amountPerTick.getValue(); i++) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, -1337.77D, mc.player.posZ, false));
                mc.player.connection.sendPacket(new CPacketSteerBoat(false, true));
            }
        }
        for (Entity entity : mc.world.playerEntities) {
            if (!entity.getName().equalsIgnoreCase(mc.player.getName())) {
                notorious.messageManager.sendMessage("Found a play at " + entity.getPosition());
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketMaps) {
            ((SPacketMaps) event.getPacket()).setMapdataTo(new MapData("haha i get ur coords"));
        }
        if (event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketEntityTeleport) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onUpdate(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketConfirmTeleport || event.getPacket() instanceof CPacketPlayerTryUseItem) {
            event.setCanceled(true);
        }
    }
}
