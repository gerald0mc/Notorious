package me.gavin.notorious.hack.hacks.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@RegisterHack(name = "AutoTotem", description = "Automatically places a totem in your offhand.", category = Hack.Category.Combat, bind = Keyboard.KEY_V)
public class AutoTotem extends Hack {

    @RegisterSetting
    public final NumSetting health = new NumSetting("HealthToSwitch", 4f, 1f, 36f, 1f);

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + health.getValue() + ChatFormatting.RESET + "]";
    }

    public int slot;

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if(mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            slot = InventoryUtil.getItemSlot(Items.TOTEM_OF_UNDYING);
            if (slot != -1 && mc.player.getHealth() <= health.getValue()) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.updateController();
            }
        }
    }
}
