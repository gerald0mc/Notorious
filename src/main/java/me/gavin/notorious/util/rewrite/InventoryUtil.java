package me.gavin.notorious.util.rewrite;

import me.gavin.notorious.util.Instance;
import net.minecraft.block.Block;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil implements Instance {
    /* NOTE TO DEVS!!!
        - In the old findItem method, the minimum was 0 and the maximum was 36
        - In the new one, it should be 0 and 35 if you're looking through the whole inventory
        - In the new one, it should be 0 and 9 if you're looking through the hotbar
        - In the new one, it should be 100 and 103 if you're looking through the armor slots
     */

    /* Getting item to use in moveItemToSlot (minimum - maximum)
        - Crafting Output (0 - 0)
        - Crafting Input (1 - 4)
        - Armor (5 - 8)
        - Whole Inventory (9 - 35)
        - Hotbar (36 - 44)
        - Offhand Slot (45 - 45)
     */

    public static int findItem(Item item, int minimum, int maximum){
        for (int i = minimum; i <= maximum; i++){
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == item) return i;
        }

        return -1;
    }

    public static int findBlock(Block block, int minimum, int maximum){
        for (int i = minimum; i <= maximum; i++){
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemBlock){
                ItemBlock item = (ItemBlock) stack.getItem();
                if (item.getBlock() == block) return i;
            }
        }

        return -1;
    }

    public static void switchToSlot(final int slot, final boolean silent) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }

    public static void moveItemToSlot(Integer startSlot, Integer endSlot) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, endSlot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, mc.player);
    }
}
