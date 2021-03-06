package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.mixin.mixins.accessor.IMinecraftMixin;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "ToggleXP", description = "Ez", category = Hack.Category.Player)
public class ToggleXP extends Hack {
    @RegisterSetting
    public final BooleanSetting footXP = new BooleanSetting("FootXP", true);

    private int serverSlot = -1;

    @Override
    public void onDisable() {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
        serverSlot = -1;
    }

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        int getSlot = getXPSlot();
        if (getSlot != -1) {
            if (serverSlot == -1) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(getSlot));
                serverSlot = getSlot;
            } else {
                if (mc.player.inventory.getStackInSlot(serverSlot).getItem() != Items.EXPERIENCE_BOTTLE) {
                    serverSlot = -1;
                } else {
                    if(footXP.isEnabled()) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90.0f, mc.player.onGround));
                    }
                    ((IMinecraftMixin) mc).setRightClickDelayTimerAccessor(0);
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                }
            }
        }
    }

    private int getXPSlot() {
        final Item item = Items.EXPERIENCE_BOTTLE;
        final Minecraft mc = Minecraft.getMinecraft();
        int itemSlot = mc.player.getHeldItemMainhand().getItem() == item ? mc.player.inventory.currentItem : -1;
        if (itemSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == item) {
                    itemSlot = l;
                }
            }
        }

        return itemSlot;
    }
}
