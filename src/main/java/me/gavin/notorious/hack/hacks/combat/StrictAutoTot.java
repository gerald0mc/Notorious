package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.util.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@RegisterHack(name = "StrictAutoTot", description = "ez", category = Hack.Category.Combat)
public class StrictAutoTot extends Hack {

    @Override
    public void onEnable() {
        notorious.messageManager.sendMessage("Please make sure you have a totem in your hotbar for this to work.");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent event) {
        int slot;
        slot = InventoryUtil.getItemSlot(Items.TOTEM_OF_UNDYING);
        if(mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING)
            if(slot != -1)
                InventoryUtil.moveItemToSlot(slot, 45);
    }
}
