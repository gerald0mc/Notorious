package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.util.rewrite.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@RegisterHack(name = "AutoTotem", description = "ez", category = Hack.Category.Combat)
public class AutoTotem extends Hack {

    @Override
    public void onEnable() {
        notorious.messageManager.sendMessage("Please make sure you have a totem in your hotbar for this to work.");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (mc.player == null || mc.world == null) return;

        int slot;
        slot = InventoryUtil.findItem(Items.TOTEM_OF_UNDYING, 0, 45);
        if(mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING)
            if(slot != -1)
                InventoryUtil.moveItemToSlot(slot, 44);
    }
}