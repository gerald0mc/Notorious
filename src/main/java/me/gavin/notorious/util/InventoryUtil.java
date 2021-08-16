package me.gavin.notorious.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Integer> getItemInventory(final Item item) {
        final List<Integer> ints = new ArrayList<>();
        for (int i = 9; i < 36; ++i) {
            final Item target = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock)item).getBlock().equals(item)) {
                ints.add(i);
            }
        }
        if (ints.size() == 0) {
            ints.add(-1);
        }
        return ints;
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

    public static int checkSlotsBlock(final Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            final Block block;
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock && (block = ((ItemBlock)stack.getItem()).getBlock()) == blockIn) {
                return i;
            }
        }
        return -1;
    }

    public static void switchToSlot(final int slot) {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(slot));
        Minecraft.getMinecraft().player.inventory.currentItem = slot;
        Minecraft.getMinecraft().playerController.updateController();
    }

    public static void switchToHotbarSlot(int slot, boolean silent) {
        if (Minecraft.getMinecraft().player.inventory.currentItem == slot || slot < 0) {
            return;
        }
        if (silent) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(slot));
            Minecraft.getMinecraft().playerController.updateController();
        } else {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(slot));
            Minecraft.getMinecraft().player.inventory.currentItem = slot;
            Minecraft.getMinecraft().playerController.updateController();
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

    public static void moveItemToSlot(Integer startSlot, Integer endSlot) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, endSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
    }

    public static void moveArmorToSlot(Integer startSlot, Integer endSlot) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, endSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
    }
}
