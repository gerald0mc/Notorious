package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.util.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@RegisterHack(name = "AutoArmor", description = "ez", category = Hack.Category.Combat)
public class AutoArmor extends Hack {

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if(mc.currentScreen instanceof GuiContainer)
            return;

        boolean hasBoots = mc.player.inventory.armorInventory.get(0).isEmpty();
        boolean hasLegs = mc.player.inventory.armorInventory.get(1).isEmpty();
        boolean hasChest = mc.player.inventory.armorInventory.get(2).isEmpty();
        boolean hasHelmet = mc.player.inventory.armorInventory.get(3).isEmpty();

        if(!hasBoots || !hasLegs || !hasChest || !hasHelmet) {
            for(int slot = 0; slot < mc.player.inventory.armorInventory.size(); ++slot) {
                Item itemSlot = mc.player.inventoryContainer.inventorySlots.get(slot).getStack().getItem();
                if(itemSlot instanceof ItemArmor) {
                    ItemArmor itemArmor = (ItemArmor) itemSlot;
                    if(!hasBoots && itemArmor.armorType == EntityEquipmentSlot.FEET) {
                        InventoryUtil.moveItemToSlot(slot, 8);
                        hasBoots = true;
                    }
                    if(!hasLegs && itemArmor.armorType == EntityEquipmentSlot.LEGS) {
                        InventoryUtil.moveItemToSlot(slot, 7);
                        hasLegs = true;
                    }
                    if(!hasChest && itemArmor.armorType == EntityEquipmentSlot.CHEST) {
                        InventoryUtil.moveItemToSlot(slot, 6);
                        hasChest = true;
                    }
                    if(!hasHelmet && itemArmor.armorType == EntityEquipmentSlot.HEAD) {
                        InventoryUtil.moveItemToSlot(slot, 5);
                        hasHelmet = true;
                    }
                }
            }
        }
    }
}
