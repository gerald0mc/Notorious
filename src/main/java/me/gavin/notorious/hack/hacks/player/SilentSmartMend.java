package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.rewrite.DamageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@RegisterHack(name = "SmartMend", description = "ez", category = Hack.Category.Player)
public class SilentSmartMend extends Hack {

    @RegisterSetting private final BooleanSetting silent = new BooleanSetting("Silent", true);
    @RegisterSetting private final NumSetting closestEnemy = new NumSetting("Enemy", 8, 1, 20, 1);
    @RegisterSetting private final NumSetting helmetThreshold = new NumSetting("Helmet%",80, 1, 100, 1);
    @RegisterSetting private final NumSetting chestThreshold = new NumSetting("Chest%",80, 1, 100, 1);
    @RegisterSetting private final NumSetting legThreshold = new NumSetting("Leg%",80, 1, 100, 1);
    @RegisterSetting private final NumSetting bootThreshold = new NumSetting("Boot%",80, 1, 100, 1);

//    public void onTick() {
//        int getSlot = getXPSlot();
//        if(getSlot != -1 && silent.isEnabled()) {
//            final ItemStack helm = mc.player.inventoryContainer.getSlot(5).getStack();
//            if (!helm.isEmpty) {
//                final int helmDamage = DamageUtil.getRoundedDamage(helm);
//                if (helmDamage >= this.helmetThreshold.getValue()) {
//                    this.takeOffSlot(5);
//                }
//            }
//            final ItemStack chest = mc.player.inventoryContainer.getSlot(6).getStack();
//            if (!chest.isEmpty) {
//                final int chestDamage = DamageUtil.getRoundedDamage(chest);
//                if (chestDamage >= this.chestThreshold.getValue()) {
//                    this.takeOffSlot(6);
//                }
//            }
//            final ItemStack legging = mc.player.inventoryContainer.getSlot(7).getStack();
//            if (!legging.isEmpty) {
//                final int leggingDamage = DamageUtil.getRoundedDamage(legging);
//                if (leggingDamage >= this.legThreshold.getValue()) {
//                    this.takeOffSlot(7);
//                }
//            }
//            final ItemStack feet = mc.player.inventoryContainer.getSlot(8).getStack();
//            if (!feet.isEmpty) {
//                final int bootDamage = DamageUtil.getRoundedDamage(feet);
//                if (bootDamage >= this.bootThreshold.getValue()) {
//                    this.takeOffSlot(8);
//                }
//            }
//            return;
//        }
//    }

    private int getXPSlot() {
        final Item item = Items.EXPERIENCE_BOTTLE;
        final Minecraft mc = Minecraft.getMinecraft();
        int itemSlot = mc.player.getHeldItemMainhand().getItem() == item ? mc.player.inventory.currentItem : -1;
        if (itemSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == item) {
                    itemSlot = l;
                }
            }
        }

        return itemSlot;
    }
}
