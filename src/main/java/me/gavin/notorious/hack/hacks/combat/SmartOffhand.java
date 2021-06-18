package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@RegisterHack(name = "SmartOffhand", description = "Automates offhand use.", category = Hack.Category.Combat, bind = Keyboard.KEY_I)
public class SmartOffhand extends Hack {

    @RegisterSetting
    public final ModeSetting offhandMode = new ModeSetting("OffhandMode", "Crystal", "Crystal", "Totem", "Gapple");
    @RegisterSetting
    public final NumSetting health = new NumSetting("HealthToSwitch", 4f, 1f, 36f, 1f);

    public int slot;

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        switch (offhandMode.getMode()) {
            case "Crystal":{
                if(mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                    slot = InventoryUtil.getItemSlot(Items.END_CRYSTAL);
                    if (slot != -1 && mc.player.getHealth() >= health.getValue()) {
                        switchToShit();
                    }
                }
                if(mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                    slot = InventoryUtil.getItemSlot(Items.TOTEM_OF_UNDYING);
                    if (slot != -1 && mc.player.getHealth() <= health.getValue()) {
                        switchToShit();
                    }
                }
            } case "Totem":{
                if(mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
                    slot = InventoryUtil.getItemSlot(Items.TOTEM_OF_UNDYING);
                    if (slot != -1 && mc.player.getHealth() <= health.getValue()) {
                        switchToShit();
                    }
                }
            } case "Gapple":{
                if(mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE) {
                    slot = InventoryUtil.getItemSlot(Items.GOLDEN_APPLE);
                    if (slot != -1 && mc.player.getHealth() >= health.getValue()) {
                        switchToShit();
                    }
                }
                if(mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
                    slot = InventoryUtil.getItemSlot(Items.TOTEM_OF_UNDYING);
                    if (slot != -1 && mc.player.getHealth() <= health.getValue()) {
                        switchToShit();
                    }
                }
            }
        }
    }

    public void switchToShit() {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.updateController();
    }
}
