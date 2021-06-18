package me.gavin.notorious.util;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtil {

    private static Boolean found = false;
    private static int i;

    public static void checkSlots(Item item) {
        for(i = 9; i <= 36; i++) {
            if(Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem().equals(item)) {
                found = true;
                break;
            }
        }
    }

    public static boolean isChestEmpty(final ContainerChest c) {
        for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
            final ItemStack slot = c.getLowerChestInventory().getStackInSlot(i);
            if (slot != null) {
                return false;
            }
        }
        return true;
    }

    public static int getItemSlot(Item items) {
        for (int i = 0; i < 36; i++) {
            final Item item = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();
            if (item == items) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }

    public static void moveItemToSlot(Integer startSlot, Integer endSlot) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, endSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
    }
}
