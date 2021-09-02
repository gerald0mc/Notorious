package me.gavin.notorious.hack.hacks.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "Quiver", description = "Automatically places a totem in your offhand.", category = Hack.Category.Combat)
public class Quiver extends Hack{

    @RegisterSetting
    public final NumSetting tickDelay = new NumSetting("HoldTime", 3, 0, 8, 0.5f);

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + tickDelay.getValue() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.getItemInUseMaxCount() >= tickDelay.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.cameraYaw, -90f, true));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem());
        }
    }
}