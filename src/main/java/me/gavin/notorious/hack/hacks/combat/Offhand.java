package me.gavin.notorious.hack.hacks.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * @author gerald0mc
 * @since like 6/10/21
 */

@RegisterHack(name = "Offhand", description = "Automates offhand use.", category = Hack.Category.Combat)
public class Offhand extends Hack {

    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Strict", "Strict", "Smart");
    @RegisterSetting
    public final ModeSetting offhandMode = new ModeSetting("OffhandMode", "Crystal",  "Crystal", "Gapple");
    @RegisterSetting
    public final NumSetting health = new NumSetting("HealthToSwitch", 14.0f, 0.5f, 36.0f, 0.5f);

    public int slot;

    @Override
    public String getMetaData() {
        String heldItem = "";
        if(mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL)
            heldItem = "EndCrystal";
        if(mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE)
            heldItem = "Gapple";
        if(mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)
            heldItem = "Totem";
        return " [" + ChatFormatting.GRAY + "Mode: " + mode.getMode() + ChatFormatting.RESET + " | " + ChatFormatting.GRAY + "Holding: " + heldItem + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onUpdate(TickEvent event) {
        String itemName;
        if(offhandMode.getMode().equals("Crystal") && mode.getMode().equals("Smart") && mc.player.getHealth() > health.getValue()) {
            slot = InventoryUtil.getItemSlot(Items.END_CRYSTAL);
        }else if(offhandMode.getMode().equals("Gapple") && mode.getMode().equals("Smart") && mc.player.getHealth() > health.getValue()) {
            slot = InventoryUtil.getItemSlot(Items.GOLDEN_APPLE);
        }else if(mc.player.getHealth() < health.getValue() || mode.getMode().equals("Strict")){
            slot = InventoryUtil.getItemSlot(Items.TOTEM_OF_UNDYING);
        }
        if(offhandMode.getMode().equals("Crystal") && mode.getMode().equals("Smart"))
            if(mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL && mc.player.getHealth() > health.getValue()) {
                if (slot != -1)
                    switchToShit();
            }else if(mc.player.getHealth() < health.getValue()) {
                if(slot != -1)
                    switchToShit();
            }
        if(offhandMode.getMode().equals("Gapple") && mode.getMode().equals("Smart"))
            if(mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL && mc.player.getHealth() > health.getValue()) {
                if (slot != -1)
                    switchToShit();
            }else if(mc.player.getHealth() < health.getValue()) {
                if(slot != -1)
                    switchToShit();
            }
        if(mode.getMode().equals("Strict")) {
            if(mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
                if(slot != -1)
                    switchToShit();
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
