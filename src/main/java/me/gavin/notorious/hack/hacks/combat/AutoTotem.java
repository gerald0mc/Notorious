package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @skidded but i also added delay and health
 * and honestly just completly rewrote
 * https://github.com/PianoPenguin471/ScratchHax/blob/fc1a7d19597fee1d93caeaabb57b291e41ff8107/ScratchHax/src/main/java/pianopenguin471/scratchhax/mods/combat/AutoTotem.java
 * ^^^ (original for context)
 */

@RegisterHack(name = "AutoTotem", description = "Automatically puts a totem in your offhand.", category = Hack.Category.Combat)
public class AutoTotem extends Hack {

    @RegisterSetting
    public final NumSetting health = new NumSetting("Health", 4f, 0.1f, 35f, 0.1f);
    @RegisterSetting
    public final NumSetting delay = new NumSetting("Delay", 2, 0.1f, 5, 0.1f);

    public Boolean found = false;
    public int i;

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if(mc.player == null)
            return;
        InventoryUtil.checkSlots(Items.TOTEM_OF_UNDYING);
        if(!mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {
            if(!(mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) && found && mc.player.ticksExisted % delay.getValue() == 0.0 && mc.player.getHealth() <= health.getValue()) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
            }
        }
    }
}
