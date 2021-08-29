package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "InstantHeal", description = "ez", category = Hack.Category.Combat)
public class InstantHeal extends Hack {

    @RegisterSetting
    public final NumSetting packetAmount = new NumSetting("PacketAmount", 10, 1, 70, 1);
    @RegisterSetting
    public final NumSetting startHealth = new NumSetting("StartHealth", 10, 1, 20, 1);

    int lastSlot = 0;
    boolean sendingPackets = false;
    boolean switchedItem = false;

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        int gAppleSlot = findGoldenApple();
        if(gAppleSlot != -1) {
            if(mc.player.getHealth() < (double) startHealth.getValue()) {
                if(mc.player.inventory.currentItem != gAppleSlot)
                    lastSlot = mc.player.inventory.currentItem;
                mc.player.onGround = true;
                mc.player.connection.sendPacket(new CPacketHeldItemChange(gAppleSlot));
                switchedItem = true;
            }
        }
    }

    public int findGoldenApple() {
        for(int i = 0; i < 9; i++) {
            if(mc.player.inventoryContainer.getSlot(36 +i).getHasStack() && mc.player.inventoryContainer.getSlot(36 + i).getStack().getItem() instanceof ItemAppleGold) {
                return i;
            }
        }
        return -1;
    }
}
