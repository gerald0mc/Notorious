package me.gavin.notorious.hack.hacks.misc;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Map;

@RegisterHack(name = "EGapFinder", description = "ez", category = Hack.Category.Misc)
public class EGapFinder extends Hack {

    @RegisterSetting
    public final BooleanSetting egaps = new BooleanSetting("EGaps", true);
    @RegisterSetting
    public final BooleanSetting crapples = new BooleanSetting("Crapples", true);
    @RegisterSetting
    public final BooleanSetting mending = new BooleanSetting("Mending", true);
    @RegisterSetting
    public final BooleanSetting prot = new BooleanSetting("Protection", true);
    @RegisterSetting
    public final BooleanSetting bprot = new BooleanSetting("BProtection", true);
    @RegisterSetting
    public final BooleanSetting feather = new BooleanSetting("Feather", true);
    @RegisterSetting
    public final BooleanSetting unb = new BooleanSetting("Unbreaking", true);
    @RegisterSetting
    public final BooleanSetting effi = new BooleanSetting("Efficiency", true);
    @RegisterSetting
    public final BooleanSetting sharp = new BooleanSetting("Sharp", true);
    @RegisterSetting
    public final BooleanSetting books = new BooleanSetting("AllBooks", true);

    public void onTick() {
        EntityPlayer entityPlayer = null;
        if (!mc.world.loadedTileEntityList.isEmpty()) {
            entityPlayer = mc.world.playerEntities.get(0);
            for (TileEntity tileEntity : mc.world.loadedTileEntityList) {
                if (tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest)tileEntity;
                    chest.fillWithLoot(entityPlayer);
                    for (byte b = 0; b < chest.getSizeInventory(); b++) {
                        ItemStack itemStack = chest.getStackInSlot(b);
                        if (itemStack.getItem() == Items.GOLDEN_APPLE)
                            if (itemStack.getItemDamage() == 1) {
                                if (this.egaps.isEnabled())
                                    notorious.messageManager.sendMessage("Dungeon Chest with god gapple at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            } else if (itemStack.getItemDamage() != 1 && this.crapples.isEnabled()) {
                                notorious.messageManager.sendMessage("Dungeon Chest with crapple at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            }
                        if (itemStack.getItem() == Items.ENCHANTED_BOOK && EnchantmentHelper.getEnchantments(itemStack) != null) {
                            ArrayList<String> arrayList = new ArrayList();
                            for (Map.Entry entry : EnchantmentHelper.getEnchantments(itemStack).entrySet()) {
                                Enchantment enchantment = (Enchantment)entry.getKey();
                                Integer integer = (Integer)entry.getValue();
                                arrayList.add(enchantment.getTranslatedName(integer.intValue()));
                            }
                            String str = "";
                            for (String str1 : arrayList) {
                                if (arrayList.indexOf(str1) != arrayList.size() - 1) {
                                    str = String.valueOf((new StringBuilder()).append(str).append(str1).append(", "));
                                    continue;
                                }
                                str = String.valueOf((new StringBuilder()).append(str).append(str1));
                            }
                            if (this.mending.isEnabled() && str.contains("Mending"))
                                notorious.messageManager.sendMessage("Dungeon Chest with Mending at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            if (this.prot.isEnabled() && str.contains("Protection IV"))
                                notorious.messageManager.sendMessage("Dungeon Chest with Prot 4 at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            if (this.bprot.isEnabled() && str.contains("Blast Protection IV"))
                                notorious.messageManager.sendMessage("Dungeon Chest with Blast Prot 4 at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            if (this.unb.isEnabled() && str.contains("Unbreaking III"))
                                notorious.messageManager.sendMessage("Dungeon Chest with Unbreaking 3 at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            if (this.feather.isEnabled() && str.contains("Feather Falling IV"))
                                notorious.messageManager.sendMessage("Dungeon Chest with Feather Falling 4 at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            if (this.effi.isEnabled() && (str.contains("Efficiency IV") || str.contains("Efficiency V")))
                                notorious.messageManager.sendMessage("Dungeon Chest with Efficiency 4/5 at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            if (this.sharp.isEnabled() && (str.contains("Sharpness IV") || str.contains("Sharpness V")))
                                notorious.messageManager.sendMessage("Dungeon Chest with Sharpness 4/5 at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                            if (this.books.isEnabled())
                                notorious.messageManager.sendMessage("Dungeon Chest with Enchanted Books at: " + chest.getPos().getX() + " " + chest.getPos().getY() + " " + chest.getPos().getZ());
                        }
                    }
                }
            }
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityMinecartContainer) {
                    EntityMinecartContainer minecart = (EntityMinecartContainer)entity;
                    if (minecart.getLootTable() != null) {
                        minecart.addLoot(entityPlayer);
                        for (byte b = 0; b < minecart.itemHandler.getSlots(); b++) {
                            ItemStack itemStack = minecart.itemHandler.getStackInSlot(b);
                            if (itemStack.getItem() == Items.GOLDEN_APPLE)
                                if (itemStack.getItemDamage() == 1) {
                                    if (this.egaps.isEnabled())
                                        notorious.messageManager.sendMessage("Minecart with God Gapple at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                                } else if (itemStack.getItemDamage() != 1 && this.crapples.isEnabled()) {
                                    notorious.messageManager.sendMessage("Minecart with Crapple at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                                }
                            if (itemStack.getItem() == Items.ENCHANTED_BOOK && this.books.isEnabled() && EnchantmentHelper.getEnchantments(itemStack) != null) {
                                ArrayList<String> arrayList = new ArrayList();
                                for (Map.Entry entry : EnchantmentHelper.getEnchantments(itemStack).entrySet()) {
                                    Enchantment enchantment = (Enchantment)entry.getKey();
                                    Integer integer = (Integer)entry.getValue();
                                    arrayList.add(enchantment.getTranslatedName(integer.intValue()));
                                }
                                String str = "";
                                for (String str1 : arrayList) {
                                    if (arrayList.indexOf(str1) != arrayList.size() - 1) {
                                        str = String.valueOf((new StringBuilder()).append(str).append(str1).append(", "));
                                        continue;
                                    }
                                    str = String.valueOf((new StringBuilder()).append(str).append(str1));
                                }
                                if (this.mending.isEnabled() && str.contains("Mending"))
                                    notorious.messageManager.sendMessage("Minecart with Mending at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                                if (this.prot.isEnabled() && str.contains("Protection IV"))
                                    notorious.messageManager.sendMessage("Minecart with Protection 4 at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                                if (this.bprot.isEnabled() && str.contains("Blast Protection IV"))
                                    notorious.messageManager.sendMessage("Minecart with Blast Protection 4 at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                                if (this.unb.isEnabled() && str.contains("Unbreaking III"))
                                    notorious.messageManager.sendMessage("Minecart with Unbreaking 3 at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                                if (this.feather.isEnabled() && str.contains("Feather Falling IV"))
                                    notorious.messageManager.sendMessage("Minecart with Feather Falling 4 at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                                if (this.effi.isEnabled() && (str.contains("Efficiency IV") || str.contains("Efficiency V")))
                                    notorious.messageManager.sendMessage("Minecart with Efficiency 4/5 at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                                if (this.sharp.isEnabled() && (str.contains("Sharpness IV") || str.contains("Sharpness V")))
                                    notorious.messageManager.sendMessage("Minecart with Sharpness 4/5 at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                                if (this.books.isEnabled())
                                    notorious.messageManager.sendMessage("Minecart with Enchanted Book at: " + minecart.posX + " " + minecart.posY + " " + minecart.posZ);
                            }
                        }
                    }
                }
            }
        }
    }
}
