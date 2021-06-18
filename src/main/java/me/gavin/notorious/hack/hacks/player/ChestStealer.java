package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@RegisterHack(name = "ChestStealer", description = "Steals items out of chests", category = Hack.Category.Player, bind = Keyboard.KEY_F)
public class ChestStealer extends Hack {

    @RegisterSetting
    public final NumSetting delay = new NumSetting("Delay", 5f,   0.1f, 10f, 0.1f);

    @SubscribeEvent
    public void onEvent(PlayerLivingUpdateEvent event) {
        if(mc.player.openContainer instanceof ContainerChest) {
            final ContainerChest chest = (ContainerChest) mc.player.openContainer;
            for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
                mc.playerController.windowClick(chest.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
                if(isChestEmpty(chest))
                    mc.player.closeScreen();
            }
        }
    }

    private boolean isChestEmpty(final ContainerChest chest) {
        for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
            final ItemStack slot = chest.getLowerChestInventory().getStackInSlot(i);
            if(slot != null) {
                return false;
            }
        }
        return true;
    }
}
