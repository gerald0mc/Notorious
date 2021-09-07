package dev.notorious.client.managers;

import dev.notorious.client.event.impl.SendPacketEvent;
import dev.notorious.client.util.IMinecraft;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerManager implements IMinecraft {
    private boolean switching = false;
    private boolean sneaking = false;
    private int slot;

    public PlayerManager(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPacketSend(SendPacketEvent event){
        if (event.getPacket() instanceof CPacketEntityAction){
            CPacketEntityAction packet = (CPacketEntityAction) event.getPacket();
            if (packet.getAction() == CPacketEntityAction.Action.START_SNEAKING){
                setSneaking(true);
            } else if (packet.getAction() == CPacketEntityAction.Action.STOP_SNEAKING){
                sneaking = false;
            }
        }

        if (event.getPacket() instanceof CPacketHeldItemChange){
            CPacketHeldItemChange packet = (CPacketHeldItemChange) event.getPacket();
            slot = packet.getSlotId();
        }
    }

    public void switchSlot(int slot, boolean silent) {
        setSwitching(true);

        if (silent) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        } else {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.player.inventory.currentItem = slot;
        }

        setSwitching(false);
    }

    public int findItem(Item item, int minimum, int maximum) {
        for (int i = minimum; i <= maximum; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == item) {
                return i;
            }
        }

        return -1;
    }

    public int findBlock(Block block, int minimum, int maximum) {
        for (int i = minimum; i <= maximum; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemBlock) {
                ItemBlock item = (ItemBlock) stack.getItem();
                if (item.getBlock() == block) {
                    return i;
                }
            }
        }

        return -1;
    }

    public Item getHeldItem(){
        return mc.player.inventory.getStackInSlot(slot).getItem();
    }

    public boolean isSwitching() {
        return switching;
    }

    public void setSwitching(boolean switching) {
        this.switching = switching;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
